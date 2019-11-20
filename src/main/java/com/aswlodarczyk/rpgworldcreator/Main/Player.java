package com.aswlodarczyk.rpgworldcreator.Main;

import com.aswlodarczyk.rpgworldcreator.Communication.Input;
import com.aswlodarczyk.rpgworldcreator.Communication.Output;
import com.aswlodarczyk.rpgworldcreator.Communication.UserInput;
import com.aswlodarczyk.rpgworldcreator.World.WorldState;
import io.vavr.control.Option;

public class Player {
    private final WorldState worldState;
    private final Input userInput;
    private final Output out;

    public Player(WorldState worldState, UserInput userInput, Output out) {
        this.worldState = worldState;
        this.userInput = userInput;
        this.out = out;
    }

    public void play() {
        while (true) {
            Option<String> maybeCommand = userInput.tryToReadCommand();
            if (maybeCommand.isDefined()) {
                Option<Runnable> command = worldState.accessCommand(maybeCommand.get());
                if (command.isDefined()) {
                    command.get().run();
                } else {
                    out.println("No such command.");
                }
            } else {
                out.println("Reading command failed.");
            }
        }
    }
}
