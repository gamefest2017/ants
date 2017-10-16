package com.ibm.sk.ff.gui.common.mapper;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public enum Mapper {

	INSTANCE;

	public <T> String pojoToJson(final T check) {
		String result = "";
		try {
			result = new ObjectMapper().writer().writeValueAsString(check);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public <T> T jsonToPojo(final String json, final Class<T> targetClass) {
		T ret = null;
		try {
			ret = new ObjectMapper().readValue(json.getBytes(), targetClass);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
