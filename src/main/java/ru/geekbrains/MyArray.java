package ru.geekbrains;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class MyArray {

    public static List<Integer> filter(List<Integer> array_in) {
        reverse(array_in);
        List<Integer> array_out = new ArrayList<>();
        for (Integer i: array_in) {
            if (i != 4) {
                array_out.add(i);
            } else {
                reverse(array_out);
                return array_out;
            }
        }
        throw new RuntimeException();
    }

    public static boolean check(List<Integer> array_in) {
        for (Integer i: array_in) {
            if (i == 1 || i == 4) {
                return true;
            }
        }
        return false;
    }

}
