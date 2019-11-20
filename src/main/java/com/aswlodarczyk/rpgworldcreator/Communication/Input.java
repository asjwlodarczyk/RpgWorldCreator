package com.aswlodarczyk.rpgworldcreator.Communication;

import io.vavr.control.Option;

public interface Input {
    Option<String> tryToReadWithQuestion(String question);
    Option<String> tryToReadCommand();
    Option<Boolean> tryToReadYesNoWithQuestion(String question);
}
