package com.gmitaros.robotichoover.controller;

import com.gmitaros.robotichoover.model.HooverRequestPayload;
import com.gmitaros.robotichoover.model.HooverResponsePayload;
import com.gmitaros.robotichoover.service.CleaningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/clean")
public class CleaningController {

    private final CleaningService cleaningService;

    @PostMapping
    public HooverResponsePayload cleanRoom(@Valid @RequestBody HooverRequestPayload inputPayload) {
        return cleaningService.process(inputPayload);
    }
}
