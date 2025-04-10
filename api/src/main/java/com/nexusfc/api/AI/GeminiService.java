package com.nexusfc.api.AI;

import com.nexusfc.api.AI.Response.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String API_KEY;
    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String request(String command) {
        String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

        String requestBody = String.format("""
        {
          "contents": [{
            "parts":[{"text": "%s"}]
          }]
        }
        """, command);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        GeminiResponse response = restTemplate.postForObject(URL, request, GeminiResponse.class);

        assert response != null;
        return response.getCandidates().getFirst().getContent().getParts().getFirst().getText();
    }

}
