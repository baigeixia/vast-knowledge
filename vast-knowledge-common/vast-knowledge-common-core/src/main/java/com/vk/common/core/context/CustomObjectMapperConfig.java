package com.vk.common.core.context;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class CustomObjectMapperConfig {

	private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

	@Bean
	@Primary
	public ObjectMapper serializingObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// ObjectMapper objectMapper = JsonMapper.builder()
		// 		.findAndAddModules()
		// 		.build();

		// ObjectMapper objectMapper = JsonMapper.builder()
		// 		.addModule(new JavaTimeModule())
		// 		.build();

		SimpleModule module = new SimpleModule();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

		module.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
		module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

		module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));


		module.addSerializer(Long.class, new Long2StringSerializer());
		objectMapper.registerModule(module);

		return objectMapper;
	}

	public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
		private final DateTimeFormatter formatter;

		public LocalDateTimeSerializer(DateTimeFormatter formatter) {
			this.formatter = formatter;
		}

		@Override
		public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeString(value.format(formatter));
		}
	}

	public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
		private final DateTimeFormatter formatter;

		public LocalDateSerializer(DateTimeFormatter formatter) {
			this.formatter = formatter;
		}

		@Override
		public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeString(value.format(formatter));
		}
	}

	public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {
		private final DateTimeFormatter formatter;

		public LocalTimeSerializer(DateTimeFormatter formatter) {
			this.formatter = formatter;
		}

		@Override
		public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeString(value.format(formatter));
		}
	}

	public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
		private final DateTimeFormatter formatter;

		public LocalDateDeserializer(DateTimeFormatter formatter) {
			this.formatter = formatter;
		}

		@Override
		public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return LocalDate.parse(p.getText(), formatter);
		}
	}

	public static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
		private final DateTimeFormatter formatter;

		public LocalTimeDeserializer(DateTimeFormatter formatter) {
			this.formatter = formatter;
		}

		@Override
		public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return LocalTime.parse(p.getText(), formatter);
		}
	}

	public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
		private final DateTimeFormatter formatter;

		public LocalDateTimeDeserializer(DateTimeFormatter formatter) {
			this.formatter = formatter;
		}

		@Override
		public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return LocalDateTime.parse(p.getText(), formatter);
		}
	}

	public static class Long2StringSerializer extends JsonSerializer<Long> {
		@Override
		public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			if (value != null) {
				gen.writeString(value.toString());
			}
		}
	}
}


