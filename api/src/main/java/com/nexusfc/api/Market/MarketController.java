package com.nexusfc.api.Market;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexusfc.api.Market.Dto.TransactionRequestDTO;
import com.nexusfc.api.Market.Dto.TransactionResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/market")
public class MarketController {
    
    private final MarketService service;

    public MarketController(MarketService service) {
        this.service = service;
    }

    @PostMapping("/buy")
    public ResponseEntity<TransactionResponseDTO> buyProfessionalPlayer(@RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(service.buyPlayer(request.getUserId(), request.getPlayerId()));
    }

    @PostMapping("/sell")
    public ResponseEntity<TransactionResponseDTO> sellProfessionalPlayer(@RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(service.sellPlayer(request.getUserId(), request.getPlayerId()));
    }

}
