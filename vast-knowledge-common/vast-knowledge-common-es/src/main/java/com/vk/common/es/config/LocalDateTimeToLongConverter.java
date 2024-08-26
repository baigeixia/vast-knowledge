package com.vk.common.es.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.unit.DataUnit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeToLongConverter implements Converter<Date, LocalDateTime> {
    @Override
    public LocalDateTime convert(Date source) {
        return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
}
