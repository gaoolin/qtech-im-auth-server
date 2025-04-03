package com.qtech.im.auth.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 08:32:05
 * desc   :
 */

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {
    @Override
    public Character convertToDatabaseColumn(Gender attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Gender.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            return Gender.UNKNOWN;
        }
    }
}
