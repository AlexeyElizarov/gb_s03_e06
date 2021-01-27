package ru.geekbrains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MyArrayCheckTest {

    @ParameterizedTest
    @MethodSource("arrayCheckData")
    public void testArrayCheck(boolean expected, List<Integer> tested) {
        Assertions.assertEquals(expected, MyArray.check(tested));
    }

    private static Stream<Arguments> arrayCheckData() {
        return Stream.of(Arguments.arguments(true, Arrays.asList(1, 2, 3)),
                Arguments.arguments(true, Arrays.asList(3, 4, 5)),
                Arguments.arguments(false, Arrays.asList(5, 6, 7)),
                Arguments.arguments(true, Arrays.asList(1, 2, 4, 5, 1)));
    }
}
