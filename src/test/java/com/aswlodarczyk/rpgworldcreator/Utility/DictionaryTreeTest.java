package com.aswlodarczyk.rpgworldcreator.Utility;

import io.vavr.control.Option;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DictionaryTreeTest {

    @Test
    public void should_allow_finding_a_necessary_value() {
        //given
        DictionaryTree<String> tree = new DictionaryTree<String>().extend("a", "abc");

        //when
        String toBeFound = tree.access("a").get();

        //then
        Assert.assertEquals(toBeFound, "abc");
    }

    @Test
    public void should_be_extensible_with_new_values() {
        //given
        DictionaryTree<String> tree = new DictionaryTree<String>().extend("a", "abc");

        //when
        DictionaryTree updated = tree.extend("b", "bcd");

        //then
        Assert.assertEquals(updated.access("b").get(), "bcd");
    }

    @Test
    public void should_return_empty_when_no_match_found() {
        //given
        DictionaryTree<String> tree = new DictionaryTree<String>().extend("a", "abc");

        //when
        Option<String> noMatch = tree.access("b");

        //then
        Assert.assertEquals(noMatch, Option.none());
    }
}
