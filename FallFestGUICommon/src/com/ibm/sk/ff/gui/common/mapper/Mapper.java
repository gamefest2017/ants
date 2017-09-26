package com.ibm.sk.ff.gui.common.mapper;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

public enum Mapper {

        INSTANCE;

        public <T> String pojoToJson(T check) {
                StringWriter sw = new StringWriter();
                try {
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(sw, check);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return sw.toString();
        }

        public <T> T jsonToPojo(String json, Class<T> targetClass) {
                T ret = null;
                try {
                        ret = new ObjectMapper().readValue(json.getBytes(), targetClass);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return ret;
        }

}
