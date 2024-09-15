package com.gmitaros.robotichoover.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HooverResponsePayload {

    private List<Integer> coords;
    private int patches;

}
