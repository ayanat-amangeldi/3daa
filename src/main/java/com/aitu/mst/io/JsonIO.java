package com.aitu.mst.io;

import com.aitu.mst.dto.InputData;
import com.aitu.mst.dto.OutputData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonIO {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static InputData readInput(String resourceOrPath) throws IOException {
        // 1) пробуем как ресурс из classpath (src/main/resources)
        InputStream is = JsonIO.class.getClassLoader().getResourceAsStream(resourceOrPath);
        if (is != null) {
            try (is) {
                return MAPPER.readValue(is, InputData.class);
            }
        }
        // 2) иначе читаем как путь на диске
        try (Reader r = Files.newBufferedReader(Path.of(resourceOrPath), StandardCharsets.UTF_8)) {
            return MAPPER.readValue(r, InputData.class);
        }
    }

    public static void writeOutput(OutputData data, String outPath) throws IOException {
        try (Writer w = Files.newBufferedWriter(Path.of(outPath), StandardCharsets.UTF_8)) {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(w, data);
        }
    }
}
