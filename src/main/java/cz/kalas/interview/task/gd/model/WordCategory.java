package cz.kalas.interview.task.gd.model;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum WordCategory {
    NOUN("noun"),
    VERB("verb"),
    ADJECTIVE("adjective");

    WordCategory(String name) {
        this.name = name;
    }

    private final String name;

    // Thanks Joshua Bloch, Effective Java
    private static final Map<String, WordCategory> ENUM_MAP;

    static {
        ENUM_MAP = Stream.of(WordCategory.values())
                .collect(toMap(WordCategory::getName, identity()));
    }

    public String getName() {
        return name;
    }

    public static Optional<WordCategory> get(String name) {
        return Optional.ofNullable(ENUM_MAP.get(name));
    }

}
