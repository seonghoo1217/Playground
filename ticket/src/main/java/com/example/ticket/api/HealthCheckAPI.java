package com.example.ticket.api;

import com.example.ticket.exception.Code;
import com.example.ticket.exception.DataResponseDTO;
import com.example.ticket.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/health-check")
public class HealthCheckAPI {

    private final ErrorTest errorTest;

    @GetMapping
    public DataResponseDTO<String> healthCheckAPI(){
        return DataResponseDTO.of("PingPong");
    }

    @GetMapping("/internal")
    public DataResponseDTO<Object> onlyInternalError(){
        errorTest.intervalError();
        return DataResponseDTO.empty();
    }
}
