package com.aswlodarczyk.rpgworldcreator.World;

import com.aswlodarczyk.rpgworldcreator.Utility.DictionaryTree;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.aswlodarczyk.rpgworldcreator.World.Direction.N;
import static com.aswlodarczyk.rpgworldcreator.World.Direction.S;

public class WorldStateTest {

    private WorldState worldState;

    @BeforeMethod
    public void setWorldState() {
        worldState = new WorldState();
        worldState.initializeWorld(
                new DictionaryTree<>(), HashMap.empty(), new Location(Tuple.of(0,0)));
    }

    @Test
    public void should_be_extensible_with_new_location() {
        //given
        Location newLocation = new Location(Tuple.of(1,1)).updateExits(S);

        //when
        worldState.updateLocations(newLocation);

        //then
        Assert.assertTrue(worldState.isPlacementOccupied(Tuple.of(1,1)));
        Assert.assertEquals(worldState.getCurrentLocation().getExits().get(0), N);
    }

    @Test
    public void should_return_description_of_location_when_move_to_it_and_set_it_as_currentLocation() {
        //given
        Location locationToMoveTo = new Location(Tuple.of(0, 1))
                .updateExits(S)
                .updateShortDescription("New fantastic location")
                .updateLongDescription("etc.");
        worldState.updateLocations(locationToMoveTo);

        //when
        String description = worldState.move(N);

        //then
        Assert.assertEquals(description, locationToMoveTo.basicDescription());
        Assert.assertTrue(locationToMoveTo == worldState.getCurrentLocation());
    }
}
