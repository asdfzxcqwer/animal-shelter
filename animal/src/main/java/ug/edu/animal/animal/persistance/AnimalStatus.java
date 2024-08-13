package ug.edu.animal.animal.persistance;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AnimalStatus {
    AVAILABLE, UNAVAILABLE;

    public static boolean contains(String status) {
        return Arrays.stream(values()).map(Enum::name).anyMatch(Predicate.isEqual(status));
    }
}
