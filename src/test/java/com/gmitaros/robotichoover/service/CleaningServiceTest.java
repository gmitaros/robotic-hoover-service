package com.gmitaros.robotichoover.service;

import com.gmitaros.robotichoover.model.HooverRequestPayload;
import com.gmitaros.robotichoover.model.HooverResponsePayload;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CleaningServiceTest {

    private final CleaningService cleaningService = new CleaningService();

    @Test
    public void testCleaning() {
        HooverRequestPayload input = new HooverRequestPayload();
        input.setRoomSize(Arrays.asList(5, 5));
        input.setCoords(Arrays.asList(1, 2));
        input.setPatches(Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(2, 2),
                Arrays.asList(2, 3)
        ));
        input.setInstructions("NNESEESWNWW");

        HooverResponsePayload responsePayload = cleaningService.process(input);

        assertEquals(Arrays.asList(1, 3), responsePayload.getCoords());
        assertEquals(1, responsePayload.getPatches());
    }
}