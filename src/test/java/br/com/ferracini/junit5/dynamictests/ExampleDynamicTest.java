package br.com.ferracini.junit5.dynamictests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * @author lferracini
 * @project = junit5-advanced
 * @since <pre>12/09/2020</pre>
 */
public class ExampleDynamicTest {

    @TestFactory
    List<DynamicTest> simple() {

        return Arrays.asList(
                dynamicTest("first", () -> Assertions.assertTrue(true)),
                dynamicTest("second", Assertions::fail)
        );
    }
    Random random = new Random();

    @TestFactory @DisplayName("Your machine will die when ran this crap test")
    Stream<DynamicTest> withStreamm(){
        return Stream.generate(random::nextInt)
                .filter(n -> n<1000000000)
                .map(n -> dynamicTest("is positive? " + n, () -> isPositive(n)));
    }

    private void isPositive(Integer n) {
        Assertions.assertTrue(n > 0);
    }

}
