package com.mulesoft.training;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;

public class ObjectMapperPretty extends ObjectMapper {

	public ObjectMapperPretty() {
		// TODO Auto-generated constructor stub
		this.enable(SerializationConfig.Feature.INDENT_OUTPUT);
	}

	public ObjectMapperPretty(JsonFactory jf) {
		super(jf);
	}

	public ObjectMapperPretty(JsonFactory jf, SerializerProvider sp,
			DeserializerProvider dp) {
		super(jf, sp, dp);
	}

	public ObjectMapperPretty(JsonFactory jf, SerializerProvider sp,
			DeserializerProvider dp, SerializationConfig sconfig,
			DeserializationConfig dconfig) {
		super(jf, sp, dp, sconfig, dconfig);
	}

}
