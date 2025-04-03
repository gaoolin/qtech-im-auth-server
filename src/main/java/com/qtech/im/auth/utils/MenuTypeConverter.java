package com.qtech.im.auth.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 11:52:07
 * desc   :
 */

@Converter(autoApply = true)
public class MenuTypeConverter implements AttributeConverter<MenuType, Character> {
    @Override
    public Character convertToDatabaseColumn(MenuType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public MenuType convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return MenuType.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            return MenuType.UNKNOWN;
        }
    }
}
