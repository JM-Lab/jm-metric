package kr.jm.metric.mutator;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigType;
import kr.jm.metric.config.mutator.field.DataType;
import kr.jm.metric.config.mutator.field.DateFormatConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMStream;
import kr.jm.utils.helper.JMString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static kr.jm.metric.config.mutator.field.FieldConfig.RAW_DATA;

@Slf4j
class FieldConfigHandler {

    private String mutatorId;
    private Map<String, MutatorConfigInterface> formatMutatorConfigMap;
    private FieldConfig fieldConfig;

    FieldConfigHandler(String mutatorId, FieldConfig fieldConfig) {
        this.mutatorId = mutatorId;
        this.fieldConfig = fieldConfig;
        this.formatMutatorConfigMap = new HashMap<>();
    }

    public Map<String, Object> applyFieldConfig(
            Map<String, Object> fieldObjectMap) {
        fieldObjectMap = new HashMap<>(fieldObjectMap);
        applyFormat(fieldObjectMap);
        applyRawData(fieldObjectMap);
        applyCombinedFields(fieldObjectMap);
        applyFormulaFields(fieldObjectMap);
        applyDateFormat(fieldObjectMap);
        applyDataType(fieldObjectMap);
        applyAlterFieldName(fieldObjectMap);
        applyIgnore(fieldObjectMap);
        applyCustom(fieldObjectMap);
        return fieldObjectMap;
    }

    private void applyCustom(Map<String, Object> fieldObjectMap) {
        JMOptional.getOptional(this.fieldConfig.getCustom())
                .ifPresent(fieldObjectMap::putAll);
    }

    private void applyAlterFieldName(Map<String, Object> fieldObjectMap) {
        JMOptional.getOptional(this.fieldConfig.getAlterFieldName())
                .stream().map(Map::entrySet).flatMap(Set::stream)
                .filter(entry -> fieldObjectMap.containsKey(entry.getKey()))
                .filter(entry -> JMString.isNotNullOrEmpty(entry.getValue()))
                .forEach(entry -> fieldObjectMap.put(entry.getValue(),
                        fieldObjectMap.remove(entry.getKey())));
    }

    private void applyDataType(Map<String, Object> fieldObjectMap) {
        JMStream.buildEntryStream(this.fieldConfig.getDataType())
                .filter(entry -> fieldObjectMap.containsKey(entry.getKey()))
                .forEach(entry -> fieldObjectMap.put(entry.getKey(),
                        transformToNumber(entry.getValue(),
                                fieldObjectMap.get(entry.getKey())
                                        .toString())));
    }

    private Object transformToNumber(DataType dataType, String data) {
        switch (dataType) {
            case NUMBER:
                return transformToNumber(data);
            case LONG:
                return transformToNumber(data).longValue();
            default:
                return data;
        }
    }

    private Double transformToNumber(String data) {
        return JMString.isNumber(data) ? Double
                .valueOf(data) : JMExceptionManager
                .handleExceptionAndReturn(log,
                        new RuntimeException("Wrong Number Format Occur !!!"),
                        "transformToNumber", () -> 0D, mutatorId, data);
    }

    private void applyDateFormat(Map<String, Object> fieldObjectMap) {
        JMStream.buildEntryStream(this.fieldConfig.getDateFormat())
                .filter(entry -> fieldObjectMap.containsKey(entry.getKey()))
                .forEach(entry -> applyDateFormat(entry.getKey(), entry
                        .getValue(), fieldObjectMap));
    }

    private void applyDateFormat(String field,
            DateFormatConfig dateFormatConfig,
            Map<String, Object> fieldObjectMap) {
        fieldObjectMap
                .put(Optional.ofNullable(dateFormatConfig.getNewFieldName())
                                .orElse(field),
                        dateFormatConfig.change(fieldObjectMap.get(field)));
    }

    private void applyIgnore(Map<String, Object> fieldObjectMap) {
        JMOptional.getOptional(this.fieldConfig.getIgnore())
                .ifPresent(ignoreList -> ignoreList
                        .forEach(fieldObjectMap::remove));
    }

    private void applyRawData(Map<String, Object> fieldObjectMap) {
        if (!this.fieldConfig.isRawData())
            fieldObjectMap.remove(RAW_DATA);
    }

    private void applyFormulaFields(Map<String, Object> fieldObjectMap) {
        JMOptional.getOptional(this.fieldConfig.getFormulaFields()).stream()
                .flatMap(Arrays::stream)
                .forEach(formulaFieldConfig -> Optional.ofNullable(
                        formulaFieldConfig.buildValue(fieldObjectMap))
                        .ifPresent(value -> fieldObjectMap
                                .put(formulaFieldConfig.getCombinedFieldName(),
                                        value)));
    }

    private void applyCombinedFields(Map<String, Object> fieldObjectMap) {
        JMOptional.getOptional(this.fieldConfig.getCombinedFields()).stream()
                .flatMap(Arrays::stream)
                .forEach(combinedFieldConfig -> fieldObjectMap
                        .put(combinedFieldConfig.getCombinedFieldName(),
                                combinedFieldConfig
                                        .buildValue(fieldObjectMap)));
    }

    private void applyFormat(Map<String, Object> fieldObjectMap) {
        Optional.ofNullable(this.fieldConfig.getFormat())
                .ifPresent(format -> format.forEach(
                        (field, fieldConfigMap) -> JMOptional
                                .getOptional(fieldObjectMap, field)
                                .map(Object::toString)
                                .map(targetString -> buildNestedFieldStringMap(
                                        getFormatMutatorConfig(field,
                                                fieldConfigMap), targetString))
                                .ifPresent(fieldObjectMap::putAll)));
    }

    private MutatorConfigInterface getFormatMutatorConfig(String field,
            Map<String, Object> fieldConfigMap) {
        return JMMap.getOrPutGetNew(this.formatMutatorConfigMap, field,
                () -> buildFieldConfig(fieldConfigMap));
    }

    private MutatorConfigInterface buildFieldConfig(
            Map<String, Object> fieldConfigMap) {
        return ConfigInterface.transformConfig(fieldConfigMap, MutatorConfigType
                .valueOf(fieldConfigMap.get("mutatorConfigType").toString())
                .getConfigClass());
    }

    private Map<String, Object> buildNestedFieldStringMap(
            MutatorConfigInterface mutatorConfig, String targetString) {
        return mutatorConfig.buildMutator().mutate(targetString);
    }

}
