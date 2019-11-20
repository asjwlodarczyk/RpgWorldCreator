package com.aswlodarczyk.rpgworldcreator.World;

import com.aswlodarczyk.rpgworldcreator.Utility.DictionaryTree;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;

public class WorldState {
    private DictionaryTree<Runnable> commands;
    private HashMap<Tuple2<Integer, Integer>, Location> locations;
    private Location currentLocation;

    public WorldState() {}

    public void initializeWorld(DictionaryTree<Runnable> commands,
                                HashMap<Tuple2<Integer, Integer>, Location> locations,
                                Location currentLocation)
    {
        this.commands = commands;
        this.locations = locations;
        this.currentLocation = currentLocation;
    }

    public Option<Runnable> accessCommand(String command) {
        return commands.access(command);
    }

    public void updateCommands(String command, Runnable expression) {
        commands = commands.extend(command, expression);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isPlacementOccupied(Tuple2<Integer, Integer> placement) {
        return locations.containsKey(placement);
    }

    public Option<Location> fetchLocationAt(Tuple2<Integer, Integer> coordinates) {
        return locations.get(coordinates);
    }

    public void updateLocations(Location newLocation) {
        currentLocation = currentLocation.updateExits(Direction.invert(newLocation.getExits().get(0)));
        locations = locations.put(currentLocation.getCoordinates(), currentLocation);
        locations = locations.put(newLocation.getCoordinates(), newLocation);
    }

    public void connectLocations(Location next, Location current) {
        currentLocation = current;
        locations = locations.put(currentLocation.getCoordinates(), currentLocation);
        locations = locations.put(next.getCoordinates(), next);
    }

    public void updateCreatures(Creature newCreature) {
        currentLocation = currentLocation.attachCreature(newCreature);
        locations = locations.put(currentLocation.getCoordinates(), currentLocation);
    }

    public String move(Direction direction) {
        if (currentLocation.hasExit(direction)) {
            Option<Location> maybeMovement = locations.get(currentLocation.mapWithDirection(direction));
            if (maybeMovement.isDefined()) {
                currentLocation = maybeMovement.get();
                return currentLocation.basicDescription();
            } else {
                return "No location to move";
            }
        } else {
            return "No such exit.";
        }
    }
}
