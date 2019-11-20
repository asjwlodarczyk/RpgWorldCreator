package com.aswlodarczyk.rpgworldcreator.Command;

import com.aswlodarczyk.rpgworldcreator.World.WorldState;
import com.aswlodarczyk.rpgworldcreator.Communication.Output;
import com.aswlodarczyk.rpgworldcreator.World.Direction;

public class ControlCommands {
    private final WorldState worldState;
    private final Output out;

    public ControlCommands(WorldState worldState, Output out) {
        this.worldState = worldState;
        this.out = out;
    }

    public void look() {
        out.println(worldState.getCurrentLocation().fullDescription());
    }

    public void move(Direction direction) {
        out.println(worldState.move(direction));
    }
}
