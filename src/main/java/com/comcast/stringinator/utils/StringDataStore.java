package com.comcast.stringinator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class StringDataStore {

    private static final String FILE_PATH = "seenStrings.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Save to file
    public void saveToFile(Map<String, AtomicInteger> seenStrings) throws IOException {
        Map<String, Integer> serializableMap = seenStrings.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
        objectMapper.writeValue(new File(FILE_PATH), serializableMap);
    }

    // Load from file
    public Map<String, AtomicInteger> loadFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ConcurrentHashMap<>();

        Map<String, Integer> rawMap = objectMapper.readValue(file, Map.class);
        return rawMap.entrySet().stream()
                .collect(Collectors.toConcurrentMap(
                        Map.Entry::getKey,
                        e -> new AtomicInteger(e.getValue())
                ));
    }
}

