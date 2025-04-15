package com.nexusfc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class FakeResponse {
    public static <T> T create(String filepath, Class<T> responseClass) throws IOException {
        ClassPathResource resource = new ClassPathResource(filepath);
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(inputStream, responseClass);
    }
}
