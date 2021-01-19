package com.bichler.astudio.editor.pubsub.nodes.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Deprecated
public class PubSubConnectionSerializer implements JsonSerializer {

	@Override
	public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
//		JsonObject jsonconnection = new JsonObject();
//
//		jsonconnection.addProperty("Id", src.getId());
//
//        return jsonMerchant;
		return null;
	}
}
