package de.mrfrey.adsbmonitor.ui

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import org.bson.types.ObjectId
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class UiBackendApplication {

	@Bean
	Module objectIdModule() {
		def module = new SimpleModule("ObjectId")
		module.addSerializer(ObjectId, new ObjectIdSerializer())
		module.addDeserializer(ObjectId, new ObjectIdDeserializer())
		module
	}

	static void main(String[] args) {
		SpringApplication.run UiBackendApplication, args
	}
}

class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

	@Override
	ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		JsonNode node = p.codec.readTree(p)
		return new ObjectId(node.textValue())
	}
}

class ObjectIdSerializer extends JsonSerializer<ObjectId> {

	@Override
	void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		if (value == null) {
			gen.writeNull();
		} else {
			gen.writeString(value.toString())
		}
	}
}