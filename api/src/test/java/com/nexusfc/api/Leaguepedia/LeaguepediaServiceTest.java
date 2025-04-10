package com.nexusfc.api.Leaguepedia;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeaguepediaServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    LeaguepediaService service;

    @Test
    public void shouldFetchPlayerHistory() throws URISyntaxException, IOException {
        ClassPathResource resource = new ClassPathResource("fixtures/leaguepedia-player-history-response.json");
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        PlayerHistoryResponse fakeResponse = mapper.readValue(inputStream, PlayerHistoryResponse.class);

        when(restTemplate.getForObject(any(), eq(PlayerHistoryResponse.class))).thenReturn(fakeResponse);

        PlayerHistoryResponse historyResponse = service.fetchPlayerHistory("Faker");

        assertThat(historyResponse).isNotNull();

        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getLink()).isEqualTo("Faker");
        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getChampion()).isEqualTo("Viktor");
        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getVersus()).isEqualTo("KT Rolster");
        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getStandardName()).isEqualTo("LCK 2025 Rounds 1-2");

    }

}
