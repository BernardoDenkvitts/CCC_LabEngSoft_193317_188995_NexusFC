package com.nexusfc.api.AI;

import com.nexusfc.api.FakeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GeminiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiService geminiService;

    @Test
    void shouldDecideAWinner() throws IOException {
        GeminiResponse fakeResponse = FakeResponse.create("fixtures/gemini-response.json", GeminiResponse.class);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(GeminiResponse.class))).thenReturn(fakeResponse);

        String result = geminiService.request("Responda apenas com 0");
        assertThat(result).isEqualTo("0\n");
    }

}
