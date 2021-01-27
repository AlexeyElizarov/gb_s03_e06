package ru.geekbrains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Test states:
 * 1. Green - everything is OK. Expected result is equal to actual
 * 2. Yellow - everything is OK. Expected result is not equal to actual
 * 3. Red - something went wrong (probably unexpected exception)
 */
public class MyArrayFilterTest {

    @ParameterizedTest
    @MethodSource("dataFilterOperation")
    public void shouldFilter(List<Integer> expected, List<Integer> tested) {
        Assertions.assertEquals(expected, MyArray.filter(tested));
    }


    private static Stream<Arguments> dataFilterOperation() {
        return Stream.of(
                Arguments.arguments(Arrays.asList(1, 7), Arrays.asList(1, 2, 4, 4, 2, 3, 4, 1, 7)),
                Arguments.arguments(new ArrayList<>(), Arrays.asList(1, 2, 4, 4, 2, 3, 4))

        );
    }

    @Test
    public void shouldThrowRuntimeException(){
        List<Integer> tested = Arrays.asList(1, 2, 3, 5);
        Assertions.assertThrows(RuntimeException.class, () -> MyArray.filter(tested));
    }
}
