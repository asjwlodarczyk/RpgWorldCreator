package com.aswlodarczyk.rpgworldcreator.Main;

import com.aswlodarczyk.rpgworldcreator.Command.*;
import com.aswlodarczyk.rpgworldcreator.Utility.DictionaryTree;

import static com.aswlodarczyk.rpgworldcreator.World.Direction.*;

public class CommandTreeInitializer {

    public DictionaryTree<Runnable> buildCommandTree(LocationCreator locationCreator,
                                                     ExpressionCreator expressionCreator,
                                                     CreatureCreator creatureCreator,
                                                     LocationsJoiner locationsJoiner,
                                                     ControlCommands controlCommands) {
        return new DictionaryTree<Runnable>()
                .extend("new location", locationCreator::newLocation)
                .extend("new expression", expressionCreator::newExpression)
                .extend("new creature", creatureCreator::newCreature)
                .extend("connect locations", locationsJoiner::connectLocations)
                .extend("look", controlCommands::look)
                .extend("n", () -> controlCommands.move(N))
                .extend("ne", () -> controlCommands.move(NE))
                .extend("e", () -> controlCommands.move(E))
                .extend("se", () -> controlCommands.move(SE))
                .extend("s", () -> controlCommands.move(S))
                .extend("sw", () -> controlCommands.move(SW))
                .extend("w", () -> controlCommands.move(W))
                .extend("nw", () -> controlCommands.move(NW));
    }
}

