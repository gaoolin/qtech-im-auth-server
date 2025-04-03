package com.qtech.im.auth.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/02 17:27:27
 * desc   :  自定义枚举转换器
 */
@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Character> {

    @Override
    public Character convertToDatabaseColumn(Status attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode(); // 将枚举转换为对应的 code
    }

    @Override
    public Status convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Status.fromCode(dbData); // 根据 code 获取对应的枚举
        } catch (IllegalArgumentException e) {
            return Status.UNKNOWN; // 如果 code 无效，返回 UNKNOWN
        }
    }
}
