package com.github.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
	@Override
	public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
		jsonWriter.value(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z");
	}

	@Override
	public LocalDateTime read(JsonReader jsonReader) throws IOException {
		return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}
}
