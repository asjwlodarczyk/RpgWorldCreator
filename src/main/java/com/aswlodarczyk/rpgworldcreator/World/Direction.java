package com.aswlodarczyk.rpgworldcreator.World;

import io.vavr.collection.List;

public enum Direction {

    N(0, 1),
    NE(1, 1),
    E(1, 0),
    SE(1, -1),
    S(-0, -1),
    SW(-1, -1),
    W(-1, 0),
    NW(-1, 1);

    private int x;
    private int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public static Direction fromString(String dir) {
        Direction direction = N;
        switch (dir) {
            case "n": direction = N; break;
            case "ne": direction = NE; break;
            case "e": direction = E; break;
            case "se": direction = SE; break;
            case "s": direction = S; break;
            case "sw": direction = SW; break;
            case "w": direction = W; break;
            case "nw": direction = NW; break;

            case "north": direction = N; break;
            case "northeast": direction = NE; break;
            case "east": direction = E; break;
            case "southeast": direction = SE; break;
            case "south": direction = S; break;
            case "southwest": direction = SW; break;
            case "west": direction = W; break;
            case "northwest": direction = NW; break;
        }
        return direction;
    }

    public static Direction invert(Direction direction) {
        if (direction == N) return S;
        else if (direction == S) return N;
        else if (direction == E) return W;
        else if (direction == W) return E;
        else if (direction == NE) return SW;
        else if (direction == SW) return NE;
        else if (direction == SE) return NW;
        else return SE;
    }

    private static final List<String> stringDirections =
            List.of("n", "ne", "e", "se", "s", "sw", "w", "nw",
                    "north", "northeast", "east", "southeast", "south",
                    "southwest", "west", "northwest");

    public static List<String> directionsAsStrings() {
        return stringDirections;
    }

    public static String directionsInOneString() {
        return stringDirections.intersperse(", ").fold("", (a, b) -> a + b);

    }
}
