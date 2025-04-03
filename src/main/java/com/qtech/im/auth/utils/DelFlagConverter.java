package com.qtech.im.auth.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/02 17:32:47
 * desc   :
 */

@Converter(autoApply = true)
public class DelFlagConverter implements AttributeConverter<DelFlag, Character> {
    @Override
    public Character convertToDatabaseColumn(DelFlag delFlag) {
        if (delFlag == null) {
            return null;
        }
        return delFlag.getCode();
    }

    @Override
    public DelFlag convertToEntityAttribute(Character character) {
        if (character == null) {
            return null;
        }
        try {
            return DelFlag.fromCode(character);
        } catch (IllegalArgumentException e) {
            return DelFlag.UNKNOWN;
        }
    }
}
