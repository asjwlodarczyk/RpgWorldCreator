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

public class LocationCreator {
    private final WorldState worldState;
    private final Input userInput;
    private final Output out;

    public LocationCreator(WorldState worldState, Input userInput, Output out) {
        this.worldState = worldState;
        this.userInput = userInput;
        this.out = out;
    }

    public void newLocation() {
        if (worldState.getCurrentLocation().hasFullSetOfExits()) {
            out.print("There is full set of exits from this location already.");
        } else {
            handleResult(createLocation());
        }
    }

    private void handleResult(Either<String, Location> maybeNewLocation) {
        if (maybeNewLocation.isRight()) {
            worldState.updateLocations(maybeNewLocation.get());
            out.println("Location successfully created.");
        } else {
            out.println(maybeNewLocation.getLeft() + "\n" + "Creation of location aborted.");
        }
    }

    private Either<String, Location> createLocation() {
        return constructEither(passDirection())
                .flatMap(newCoordinates)
                .flatMap(newLocation)
                .flatMap(addShortDescription)
                .flatMap(addLongDescription);
    }

    private Either<String, Direction> constructEither(Option<String> maybeDirection) {
        return newDirection.apply(maybeDirection);
    }

    private Option<String> passDirection() {
        return userInput.tryToReadWithQuestion("Direction");
    }

    private final Function<Option<String>, Either<String, Direction>> newDirection =
        maybeDirection -> maybeDirection.isDefined()
                ? checkDirection(maybeDirection.get())
                : Left("Passing direction failed.");

    private Either<String, Direction> checkDirection(String direction) {
        return isProperFormat(direction)
                ? Right(convertIntoDirection(direction))
                : Left("No such direction. There must be one of: " +
                    Direction.directionsInOneString());
    }

    private boolean isProperFormat(String direction) {
        return Direction.directionsAsStrings().contains(direction);
    }

    private Direction convertIntoDirection(String direction) {
        return Direction.fromString(direction);
    }

    private final Function<Direction,
                           Either<String, Tuple2<Direction,
                                                 Tuple2<Integer, Integer>>>> newCoordinates = this::checkExits;

    private Either<String, Tuple2<Direction,
                                  Tuple2<Integer, Integer>>> checkExits(Direction direction) {
        Location location = worldState.getCurrentLocation();
        return !location.hasExit(direction)
                ? Right(Tuple.of(direction, location.mapWithDirection(direction)))
                : Left("Current location has the exit " + direction + " already.");
    }

    private final Function<Tuple2<Direction, Tuple2<Integer, Integer>>,
                           Either<String, Location>> newLocation =
            directionAndCoordinates -> !isPlacementOccupied(directionAndCoordinates._2)
                    ? Right(new Location(directionAndCoordinates._2)
                                    .updateExits(Direction.invert(directionAndCoordinates._1)))
                    : Left("There is location at given direction already. " +
                            "But you can 'connect locations' if you want.");

    private boolean isPlacementOccupied(Tuple2<Integer, Integer> coordinates) {
        return worldState.isPlacementOccupied(coordinates);
    }

    private final Function<Location, Either<String, Location>> addShortDescription =
            location -> newShortDescription(location, passShortDescription());

    private Either<String, Location> newShortDescription(
            Location location, Option<String> maybeDescription) {
        return maybeDescription.isDefined()
                    ? filterShortDescriptionByLength(location, maybeDescription.get())
                    : Left("Passing description failed.");
    }

    private Option<String> passShortDescription() {
        return userInput.tryToReadWithQuestion("Short description (max 50 characters)");
    }

    private Either<String, Location> filterShortDescriptionByLength(
            Location location, String description) {
        return description.length() > 51
                ? Left("Description too long.")
                : description.length() == 0
                    ? Left("Description too short.")
                    : Right(location.updateShortDescription(description));

    }

    private final Function<Location, Either<String, Location>> addLongDescription =
            location -> newLongDescription(location, passLongDescription());

    private Either<String, Location> newLongDescription(
            Location location, Option<String> maybeDescription) {
        return maybeDescription.isDefined()
                ? filterLongDescriptionByLength(location, maybeDescription.get())
                : Left("Passing description failed.");
    }

    private Option<String> passLongDescription() {
        return userInput.tryToReadWithQuestion("Long description (max 250 characters)");
    }

    private Either<String, Location> filterLongDescriptionByLength(
            Location location, String description) {
        return description.length() > 251
                ? Left("Description too long.")
                : description.length() == 0
                    ? Left("Description too short.")
                    : Right(location.updateLongDescription(description));
    }
}

