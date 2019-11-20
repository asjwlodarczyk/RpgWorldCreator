package com.aswlodarczyk.rpgworldcreator.Main;

import com.aswlodarczyk.rpgworldcreator.Command.*;
import com.aswlodarczyk.rpgworldcreator.Communication.Printer;
import com.aswlodarczyk.rpgworldcreator.Communication.UserInput;
import com.aswlodarczyk.rpgworldcreator.Utility.DictionaryTree;
import com.aswlodarczyk.rpgworldcreator.World.Location;
import com.aswlodarczyk.rpgworldcreator.World.WorldState;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;

public class Initializer {

    public void initialize() {
        Printer out = new Printer();
        UserInput userInput = new UserInput(out);
        WorldState worldState = new WorldState();
        LocationCreator locationCreator = new LocationCreator(worldState, userInput, out);
        ExpressionCreator expressionCreator = new ExpressionCreator(worldState, userInput, out);
        CreatureCreator creatureCreator = new CreatureCreator(worldState, userInput, out);
        LocationsJoiner locationsJoiner = new LocationsJoiner(worldState, userInput, out);
        ControlCommands controlCommands = new ControlCommands(worldState, out);
        CommandTreeInitializer commandTreeInitializer = new CommandTreeInitializer();
        DictionaryTree<Runnable> commandTree =
                commandTreeInitializer.buildCommandTree(
                        locationCreator, expressionCreator, creatureCreator, locationsJoiner, controlCommands);
        worldState.initializeWorld(commandTree, HashMap.empty(), new Location(Tuple.of(0, 0))
                .updateShortDescription("Start Location.")
                .updateLongDescription("Nothing else here. Forget about this location and build your world!"));
        Player player = new Player(worldState, userInput, out);
        player.play();
    }
}
