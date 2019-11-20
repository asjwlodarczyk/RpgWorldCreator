package com.aswlodarczyk.rpgworldcreator.Communication;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Scanner;

import static io.vavr.API.*;

public class UserInput implements Input {
    private Scanner input;
    private Printer out;

    public UserInput(Printer out) {
        this.out = out;
        input = new Scanner(System.in);
    }

    @Override
    public Option<String> tryToReadWithQuestion(String question) {
        return Try.of(() -> readWithQuestion(question)).toOption();
    }

    @Override
    public Option<String> tryToReadCommand() {
        return Try.of(() -> readCommand()).toOption();
    }

    @Override
    public Option<Boolean> tryToReadYesNoWithQuestion(String question) {
        Option<String> maybeYesNo = tryToReadYesNo(question);
        return maybeYesNo.isDefined()
                ? Match(maybeYesNo.get()).of(
                        Case($(x -> x.equals("y")), Option.of(true)),
                        Case($(x -> x.equals("n")), Option.of(false)),
                        Case($(), Option.none()))
                : Option.none();
    }

    private Option<String> tryToReadYesNo(String question) {
        return Try.of(() -> readWithQuestion(question)).toOption();
    }

    private String readWithQuestion(String question) {
        out.print(question + ": ");
        return input.nextLine();
    }

    private String readCommand() {
        out.print("> ");
        return input.nextLine().toLowerCase();
    }
}
