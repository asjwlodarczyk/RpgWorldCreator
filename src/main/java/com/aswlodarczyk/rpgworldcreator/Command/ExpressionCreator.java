package com.aswlodarczyk.rpgworldcreator.Command;

import com.aswlodarczyk.rpgworldcreator.World.WorldState;
import com.aswlodarczyk.rpgworldcreator.Communication.Input;
import com.aswlodarczyk.rpgworldcreator.Communication.Output;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.function.Function;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public class ExpressionCreator {
    private final WorldState worldState;
    private final Input userInput;
    private final Output out;

    public ExpressionCreator(WorldState worldState, Input userInput, Output out) {
        this.worldState = worldState;
        this.userInput = userInput;
        this.out = out;
    }

    public void newExpression() {
        handleResult(createExpression());
    }

    private void handleResult(Either<String, Tuple2<String, String>> maybeNewExpression) {
        if (maybeNewExpression.isRight()) {
            worldState.updateCommands(maybeNewExpression.get()._1,
                    () -> out.println(maybeNewExpression.get()._2));
            out.println("New expression added.");
        } else {
            out.println(maybeNewExpression.getLeft() + "\n" + "Creation of expression aborted.");
        }
    }

    private Either<String, Tuple2<String, String>> createExpression() {
        return constructEither(passExpressionCommand())
                .flatMap(addExpression);
    }

    private Either<String, String> constructEither(Option<String> maybeCommand) {
        return newCommand.apply(maybeCommand);
    }

    private Option<String> passExpressionCommand() {
        return userInput.tryToReadWithQuestion("Command (max 100 characters)");
    }

    private final Function<Option<String>, Either<String, String>> newCommand =
        maybeCommand -> maybeCommand.isDefined()
                ? checkCommand(maybeCommand.get())
                : Left("Passing command failed.");

    private Either<String, String> checkCommand(String command) {
        return !worldState.accessCommand(command).isDefined()
                ? filterCommandByLength(command)
                : Left("The command exists already.");
    }

    private Either<String, String> filterCommandByLength(String command) {
        return command.length() > 101
                ? Left("Command too long.")
                : command.length() == 0
                    ? Left("Command too short.")
                    : Right(command);
    }

    private final Function<String, Either<String, Tuple2<String, String>>> addExpression =
        command -> updateExpression(command, passExpression());

    private Either<String, Tuple2<String, String>> updateExpression(
            String command, Option<String> maybeExpression) {
        return maybeExpression.isDefined()
                ? filterExpressionByLength(command, maybeExpression.get())
                : Left("Passing expression failed.");
    }

    private Option<String> passExpression() {
        return userInput.tryToReadWithQuestion("Expression (max 250 characters");
    }

    private Either<String, Tuple2<String, String>> filterExpressionByLength(
            String command, String expression) {
        return expression.length() > 251
                ? Left("Expression too long.")
                : expression.length() == 0
                    ? Left("Expression too short")
                    : Right(Tuple.of(command, expression));
    }
}
