package com.gmitaros.robotichoover.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmitaros.robotichoover.model.HooverRequestPayload;
import com.gmitaros.robotichoover.model.HooverResponsePayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CleaningControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCleanRoomEndpoint() throws Exception {
        HooverRequestPayload input = new HooverRequestPayload();
        input.setRoomSize(Arrays.asList(5, 5));
        input.setCoords(Arrays.asList(1, 2));
        input.setPatches(Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(2, 2),
                Arrays.asList(2, 3)
        ));
        input.setInstructions("NNESEESWNWW");

        HooverResponsePayload expectedOutput = new HooverResponsePayload(Arrays.asList(1, 3), 1);


        mockMvc.perform(post("/clean")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    public void testInvalidInstructionsAreIgnored() throws Exception {
        HooverRequestPayload input = new HooverRequestPayload();
        input.setRoomSize(Arrays.asList(5, 5));
        input.setCoords(Arrays.asList(1, 2));
        input.setPatches(Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(2, 2),
                Arrays.asList(2, 3)
        ));
        input.setInstructions("NNXSE$ESWNWW"); // Contains invalid instructions 'X' and '$'

        HooverResponsePayload expectedOutput = new HooverResponsePayload(Arrays.asList(0, 3), 2);

        mockMvc.perform(post("/clean")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }


    @Test
    public void testBoundaryConditions() throws Exception {
        HooverRequestPayload input = new HooverRequestPayload();
        input.setRoomSize(Arrays.asList(3, 3));
        input.setCoords(Arrays.asList(0, 0));
        input.setPatches(Arrays.asList(
                Arrays.asList(0, 0),
                Arrays.asList(1, 1)
        ));
        input.setInstructions("NNNWWWSSSEEE");

        HooverResponsePayload expectedOutput = new HooverResponsePayload(Arrays.asList(2, 0), 1);

        mockMvc.perform(post("/clean")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    public void testLargeInputData() throws Exception {
        // Generate a large room and a long instruction string
        HooverRequestPayload input = new HooverRequestPayload();
        input.setRoomSize(Arrays.asList(1000, 1000));
        input.setCoords(Arrays.asList(0, 0));
        input.setPatches(Arrays.asList()); // No patches
        char[] instructions = new char[10000];
        Arrays.fill(instructions, 'N');
        input.setInstructions(new String(instructions));

        HooverResponsePayload expectedOutput = new HooverResponsePayload(Arrays.asList(0, 999), 0);

        mockMvc.perform(post("/clean")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    public void testValidationErrors() throws Exception {
        HooverRequestPayload input = new HooverRequestPayload();
        mockMvc.perform(post("/clean")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.instructions").value("Instructions cannot be blank"))
                .andExpect(jsonPath("$.errors.roomSize").value("Room size cannot be empty"))
                .andExpect(jsonPath("$.errors.patches").value("must not be null"))
                .andExpect(jsonPath("$.errors.coords").value("Coords cannot be empty"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/clean"));
    }

}