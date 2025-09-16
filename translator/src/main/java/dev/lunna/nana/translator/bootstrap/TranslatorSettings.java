package dev.lunna.nana.translator.bootstrap;

import dev.lunna.nana.translator.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

public record TranslatorSettings(
        File[] inputFiles,
        File outputDir,
        String prompt,
        boolean pupulate
) {
    public static TranslatorSettings parse(@NotNull final String input, @NotNull final String output, @NotNull final String promptPath, final boolean populate) {
        if (input.isBlank()) throw new IllegalArgumentException("Input cannot be blank");
        if (output.isBlank()) throw new IllegalArgumentException("Output cannot be blank");
        if (promptPath.isBlank()) throw new IllegalArgumentException("Prompt cannot be blank");

        File[] inputFiles;
        File outputDir;
        String prompt;

        final var inputFile = new File(input);
        if (!inputFile.exists()) throw new IllegalArgumentException("Input file or directory does not exist: " + input);
        if (!inputFile.canRead()) throw new IllegalArgumentException("Cannot read input file or directory: " + input);

        if (inputFile.isDirectory()) {
            final var files = new java.util.ArrayList<File>();
            FileUtil.getInputFilesRecursively(inputFile, files);
            if (files.isEmpty()) throw new IllegalArgumentException("No .cg files found in directory: " + input);
            inputFiles = files.toArray(new File[0]);
        } else {
            inputFiles = new File[]{inputFile};
        }

        final var outputDirFile = new File(output);
        if (!outputDirFile.exists()) {
            if (!outputDirFile.mkdirs()) {
                throw new IllegalArgumentException("Failed to create output directory: " + output);
            }
        }

        if (!outputDirFile.isDirectory()) throw new IllegalArgumentException("Output is not a directory: " + output);
        if (!outputDirFile.canWrite())
            throw new IllegalArgumentException("Cannot write to output directory: " + output);
        outputDir = outputDirFile;

        final StringBuilder promptBuilder = new StringBuilder();

        try (var inputStream = FileUtil.getResourceAsStream(promptPath)) {
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    promptBuilder.append(line).append(System.lineSeparator());
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read prompt from: " + promptPath, e);
        }

        prompt = promptBuilder.toString().trim();
        if (prompt.isBlank())
            throw new IllegalArgumentException("Prompt cannot be blank after reading from:" + promptPath);

        return new TranslatorSettings(inputFiles, outputDir, prompt, populate);
    }

    @Override
    public @NotNull String toString() {
        return "TranslatorSettings{" +
                "inputFiles=" + Arrays.toString(inputFiles) +
                ", outputDir=" + outputDir +
                ", prompt='" + prompt + '\'' +
                '}';
    }
}
