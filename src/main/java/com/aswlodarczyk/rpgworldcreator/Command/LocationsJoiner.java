package com.aswlodarczyk.rpgworldcreator.Command;

import com.aswlodarczyk.rpgworldcreator.World.WorldState;
import com.aswlodarczyk.rpgworldcreator.Communication.Input;
import com.aswlodarczyk.rpgworldcreator.Communication.Output;
import com.aswlodarczyk.rpgworldcreator.World.Direction;
import com.aswlodarczyk.rpgworldcreator.World.Location;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.function.Function;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public class LocationsJoiner {
    private final WorldState worldState;
    private final Input userInput;
    private final Output out;

    public LocationsJoiner(WorldState worldState, Input userInput, Output out) {
        this.worldState = worldState;
        this.userInput = userInput;
        this.out = out;
    }

    public void connectLocations() {
        if (worldState.getCurrentLocation().hasFullSetOfExits()) {
            out.print("There is full set of exits from this location already.");
        } else {
            handleResult(createConnection());
        }
    }

    private void handleResult(Either<String, Tuple2<Location, Location>> maybeConnectedLocations) {
        if (maybeConnectedLocations.isRight()) {
            Tuple2<Location, Location> nextAndCurrent = maybeConnectedLocations.get();
            worldState.connectLocations(nextAndCurrent._1, nextAndCurrent._2);
            out.println("Locations successfully connected.");
        } else {
            out.println(maybeConnectedLocations.getLeft() + "\n" + "Connecting locations aborted.");
        }
    }

    private Either<String, Tuple2<Location, Location>> createConnection() {
        return constructEither(passDirection())
                .flatMap(checkDirectionFormat)
                .flatMap(checkExits)
                .flatMap(checkNeighborAvailability)
                .map(addNewDirectionsToLocations);
    }

    private Either<String, String> constructEither(Option<String> maybeDirection) {
        return maybeDirection.isDefined()
                ? Right(maybeDirection.get())
                : Left("Passing direction failed.");
    }

    private Option<String> passDirection() {
        return userInput.tryToReadWithQuestion("Direction");
    }

    private final Function<String, Either<String, Direction>> checkDirectionFormat =
        stringDirection -> isProperFormat(stringDirection)
            ? Right(Direction.fromString(stringDirection))
            : Left("Not acceptable direction! Pass one of: "
                    + Direction.directionsInOneString());

    private boolean isProperFormat(String direction) {
        return Direction.directionsAsStrings().contains(direction);
    }

    private final Function<Direction, Either<String, Direction>> checkExits =
        direction -> !doesCurrentLocationHasExit(direction)
            ? Right(direction)
            : Left("Locations are connected already.");

    private boolean doesCurrentLocationHasExit(Direction direction) {
        return worldState.getCurrentLocation().hasExit(direction);
    }

    private final Function<Direction, Either<String, Tuple2<Direction, Location>>>
            checkNeighborAvailability =
        direction -> {
            Option<Location> locationToConnect = fetchLocationAt(direction);
            return locationToConnect.isDefined()
                ? Right(Tuple.of(direction, locationToConnect.get()))
                : Left("No location at given direction.");
        };

    private Option<Location> fetchLocationAt(Direction direction) {
        return worldState.fetchLocationAt(
                worldState.getCurrentLocation().mapWithDirection(direction));
    }

    private final Function<Tuple2<Direction, Location>, Tuple2<Location, Location>>
            addNewDirectionsToLocations =
        directionAndLocation -> Tuple.of(
                directionAndLocation._2.updateExits(Direction.invert(directionAndLocation._1)),
                getCurrentLocationWithNewExit(directionAndLocation._1));

    private Location getCurrentLocationWithNewExit(Direction direction) {
        return worldState.getCurrentLocation().updateExits(direction);
    }
}
