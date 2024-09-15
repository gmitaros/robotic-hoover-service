package com.gmitaros.robotichoover.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HooverRequestPayload {

    @NotEmpty(message = "Room size cannot be empty")
    @Size(min = 2, max = 2)
    private List<Integer> roomSize;

    @NotEmpty(message = "Coords cannot be empty")
    @Size(min = 2, max = 2)
    private List<Integer> coords;

    @NotNull
    private List<List<Integer>> patches;

    @NotBlank(message = "Instructions cannot be blank")
    private String instructions;

}
