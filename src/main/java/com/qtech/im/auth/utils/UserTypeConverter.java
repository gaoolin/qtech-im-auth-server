package com.qtech.im.auth.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 08:34:10
 * desc   :
 */

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, Character> {
    @Override
    public Character convertToDatabaseColumn(UserType userType) {
        if (userType == null) {
            return null;
        }
        return userType.getCode();
    }

    @Override
    public UserType convertToEntityAttribute(Character character) {
        if (character == null) {
            return null;
        }
        try {
            return UserType.fromCode(character);
        } catch (IllegalArgumentException e) {
            return UserType.UNKNOWN;
        }
    }
}
