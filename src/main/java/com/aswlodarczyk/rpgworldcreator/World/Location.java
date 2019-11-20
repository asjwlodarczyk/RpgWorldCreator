package com.aswlodarczyk.rpgworldcreator.World;

import io.vavr.Tuple2;
import io.vavr.collection.List;

public class Location {
    private final Tuple2<Integer, Integer> coordinates;
    private List<Direction> exits;
    private List<Creature> creatures;
    private String shortDescription;
    private String longDescription;
    private static final String exitsAre = "Exits: ";

    public Location(Tuple2<Integer, Integer> coordinates) {
        this.coordinates = coordinates;
        exits = List.empty();
        creatures = List.empty();
    }

    public Location updateShortDescription(String description) {
        shortDescription = description;
        return this;
    }

    public Location updateLongDescription(String description) {
        longDescription = description;
        return this;
    }

    public Location updateExits(Direction exit) {
        if (exits.isEmpty()) {
            exits = List.of(exit);
        } else {
            exits = exits.append(exit);
        }
        return this;
    }

    public Location attachCreature(Creature creature) {
        if (creatures.isEmpty()) {
            creatures = List.of(creature);
        } else {
            creatures = creatures.append(creature);
        }
        return this;
    }

    public boolean hasFullSetOfExits() {
        return exits.size() == 8;
    }

    public Tuple2<Integer, Integer> mapWithDirection(Direction direction) {
        return coordinates.map1(x -> x + direction.x()).map2(y -> y + direction.y());
    }

    public boolean hasExit(Direction direction) {
        return exits.contains(direction);
    }

    public List<Direction> getExits() {
        return exits;
    }

    public Tuple2<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    private String exits() {
        return exitsAre + exits.map(Direction::toString).intersperse(", ")
                .fold("", (accumulated, another) -> accumulated + another);
    }

    private String creatures() {
        return creatures.map(Creature::basicDescription).intersperse(", ")
                .fold("", (accumulated, another) -> accumulated + another);
    }

    public String basicDescription() {
        return shortDescription + "\n" + exits() + "\n" + creatures();
    }

    public String fullDescription() {
        return shortDescription + "\n" + longDescription + "\n" + exits() + "\n" + creatures();
    }

    @Override
    public String toString() {
        return coordinates + "\n" + shortDescription + "\n" + longDescription + "\n" + exits + "\n" + creatures;
    }
}
