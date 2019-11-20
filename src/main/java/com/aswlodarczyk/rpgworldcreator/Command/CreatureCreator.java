package com.aswlodarczyk.rpgworldcreator.Command;

import com.aswlodarczyk.rpgworldcreator.World.WorldState;
import com.aswlodarczyk.rpgworldcreator.Communication.Input;
import com.aswlodarczyk.rpgworldcreator.Communication.Output;
import com.aswlodarczyk.rpgworldcreator.World.Creature;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.function.Function;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public class CreatureCreator {
    private final WorldState worldState;
    private final Input userInput;
    private final Output out;

    public CreatureCreator(WorldState worldState, Input userInput, Output out) {
        this.worldState = worldState;
        this.userInput = userInput;
        this.out = out;
    }

    public void newCreature() {
        handleResult(createCreature());
    }

    private void handleResult(Either<String, Creature> maybeNewCreature) {
        if (maybeNewCreature.isRight()) {
            worldState.updateCreatures(maybeNewCreature.get());
            out.println("Creature successfully created and attached to the current location.");
        } else {
            out.println(maybeNewCreature.getLeft() + "\n" + "Creation of creature aborted.");
        }
    }

    private Either<String, Creature> createCreature() {
        return constructEither(passName())
                .flatMap(addDescription)
                .flatMap(addExpressions);
    }

    private Either<String, Creature> constructEither(Option<String> maybeName) {
        return newName.apply(maybeName);
    }

    private Option<String> passName() {
        return userInput.tryToReadWithQuestion("Name (max 50 characters)");
    }

    private final Function<Option<String>, Either<String, Creature>> newName =
        maybeNewName -> maybeNewName.isDefined()
                ? filterNameByLength(maybeNewName.get())
                : Left("Passing name failed.");

    private Either<String, Creature> filterNameByLength(String name) {
        return name.length() > 51
                ? Left("Name too long.")
                : name.length() == 0
                    ? Left("Name too short.")
                    : Right(new Creature(name));
    }

    private final Function<Creature, Either<String, Creature>> addDescription =
            creature -> newDescription(creature, passDescription());

    private Either<String, Creature> newDescription(
            Creature creature, Option<String> maybeDescription) {
        return maybeDescription.isDefined()
                ? filterDescriptionByLength(creature, maybeDescription.get())
                : Left("Passing description failed.");
    }

    private Option<String> passDescription() {
        return userInput.tryToReadWithQuestion("Description (max 250 characters)");
    }

    private Either<String, Creature> filterDescriptionByLength(
            Creature creature, String description) {
        return description.length() > 251
                ? Left("Description too long")
                : description.length() == 0
                ? Left("Description too short.")
                : Right(creature.updateDescription(description));
    }

    private final Function<Creature, Either<String, Creature>> addExpressions =
        creature -> updateExpressions(Right(creature));

    private Either<String, Creature> updateExpressions(
            Either<String, Creature> maybeCreature) {
        return maybeCreature.isRight()
                ? newExpression(maybeCreature)
                : maybeCreature;
    }

    private Either<String, Creature> newExpression(
            Either<String, Creature> maybeCreature) {
        Option<Boolean> maybeYesOrNoAnswer =
                userInput.tryToReadYesNoWithQuestion(
                        kindOfQuestion(maybeCreature.get()));
        return maybeYesOrNoAnswer.isDefined()
                ? maybeYesOrNoAnswer.get()
                    ? updateExpressions(maybeCreature.flatMap(attachExpression))
                    : maybeCreature
                : Left("Attaching expressions failed.");
    }

    private String kindOfQuestion(Creature creature) {
        return creature.hasExpressions()
                ? "Do you want to attach more expressions?"
                : "Do you want to attach expression to this creature?";
    }

    private final Function<Creature, Either<String, Creature>> attachExpression =
        creature -> {
            Option<String> maybeNewExpression = passExpression();
            return maybeNewExpression.isDefined()
                    ? filterExpressionByLength(creature, maybeNewExpression.get())
                    : Left("Passing expression failed.");
        };

    private Option<String> passExpression() {
        return userInput.tryToReadWithQuestion("Expression (max 250 characters)");
    }

    private Either<String, Creature> filterExpressionByLength(
            Creature creature, String expression) {
        return expression.length() > 251
                ? Left("Expression too long")
                : expression.length() == 0
                    ? Left("Expression too short.")
                    : Right(creature.updateSingleExpression(expression));
    }
}
