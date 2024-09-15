package com.gmitaros.robotichoover.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gmitaros.robotichoover.model.HooverResponsePayload;
import com.gmitaros.robotichoover.model.Position;
import com.gmitaros.robotichoover.model.HooverRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CleaningService {

    private static final Logger logger = LoggerFactory.getLogger(CleaningService.class);


    public HooverResponsePayload process(HooverRequestPayload input) {
        logger.debug("Processing input: {}", input);

        final int roomXSize = input.getRoomSize().get(0);
        final int roomYSize = input.getRoomSize().get(1);

        final Position hooverPosition = new Position(input.getCoords().get(0), input.getCoords().get(1));

        final Set<Position> patchesPosition = new HashSet<>();
        for (List<Integer> patch : input.getPatches()) {
            patchesPosition.add(new Position(patch.get(0), patch.get(1)));
        }

        int cleanedPatches = 0;

        if (patchesPosition.remove(hooverPosition)) {
            cleanedPatches++;
        }

        for (char instruction : input.getInstructions().toCharArray()) {
            moveHoover(instruction, hooverPosition, roomXSize, roomYSize);
            if (patchesPosition.remove(hooverPosition)) {
                cleanedPatches++;
            }
        }

        final HooverResponsePayload response = new HooverResponsePayload(List.of(hooverPosition.getX(), hooverPosition.getY()), cleanedPatches);
        logger.debug("Cleaning process result: {}", response);
        return response;
    }

    private void moveHoover(char instruction, Position hoover, int roomX, int roomY) {
        int deltaX = 0;
        int deltaY = 0;

        switch (instruction) {
            case 'N':
                deltaY = 1;
                break;
            case 'S':
                deltaY = -1;
                break;
            case 'E':
                deltaX = 1;
                break;
            case 'W':
                deltaX = -1;
                break;
            default:
                logger.warn("Invalid instruction encountered: {}", instruction);
                return;
        }

        int newX = hoover.getX() + deltaX;
        int newY = hoover.getY() + deltaY;

        // Check boundaries
        if (newX >= 0 && newX < roomX && newY >= 0 && newY < roomY) {
            hoover.setX(newX);
            hoover.setY(newY);
            logger.debug("Moved to {}", hoover);
        } else {
            logger.debug("Hoover attempted to move outside boundaries to ({}, {}). Current {}", newX, newY, hoover);
        }
    }
}
