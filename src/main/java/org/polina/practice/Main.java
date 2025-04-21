package org.polina.practice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String[] strings = {"parrot", "low", "cat", "corn", "grape", "stake", "banana"};
        Integer[] numbers = {1, 4, 62, 2, 6, 2, 1, 5, 7, 2, 3, 3, 5, 7, 5, 4, 1, 62, 7};
        Filter<String> endsWithVowelFilter = new Filter<String>() {
            @Override
            public String apply(String word) {
                if (word == null || word.isEmpty()) {
                    return null;
                }
                String vowel = "aeiou";
                char lastChar = Character.toLowerCase(word.charAt(word.length() - 1));
                return vowel.indexOf(lastChar) != -1 ? word : null;
            }
        };
        System.out.println(Arrays.toString(filter(strings, endsWithVowelFilter)));
        System.out.println(count(numbers));
    }

    public static String[] filter(String[] array, Filter<String> filter) {
        return Arrays.stream(array)
                .map(filter::apply)
                .filter(Objects::nonNull)
                .toArray(size -> Arrays.copyOf(array, size));
    }

    public static Map<Integer, Integer> count(Integer[] array) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Integer element : array) {
            if (map.containsKey(element)) {
                Integer value = map.get(element);
                value++;
                map.put(element, value);
            } else {
                map.put(element, 1);
            }
        }
        return map;
    }
}
