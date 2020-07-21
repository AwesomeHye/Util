package kr.datasolution.ainer.patterndocumentgenerator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperUtils {

    public static ObjectMapper getCustomizedObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty print
        return objectMapper;
    }
}
