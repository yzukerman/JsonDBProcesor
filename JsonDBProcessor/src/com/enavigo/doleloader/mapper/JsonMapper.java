package com.enavigo.doleloader.mapper;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonMapper {

	public Object mapJson(JsonNode tree);
}
