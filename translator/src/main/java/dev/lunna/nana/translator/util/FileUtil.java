package dev.lunna.nana.translator.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class FileUtil {
    public static final Charset SHIFT_JIS = Charset.forName("Shift-JIS");
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    public static String classpathPrefix = "classpath:";
    public static String filePrefix = "file:";

    public static void getInputFilesRecursively(@NotNull final File dir, @NotNull List<File> files) {
        requireNonNull(dir);
        requireNonNull(files);
        if (!dir.isDirectory()) throw new IllegalArgumentException("Not a directory: " + dir.getAbsolutePath());

        for (File file : requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                getInputFilesRecursively(file, files);
            } else if (file.getName().endsWith(".cg")) {
                files.add(file);
            }
        }
    }

    @NotNull
    public static InputStream getResourceAsStream(@NotNull final String resourcePath) {
        requireNonNull(resourcePath);
        if (resourcePath.isBlank()) throw new IllegalArgumentException("Resource path cannot be blank");

        if (resourcePath.startsWith(classpathPrefix)) {
            final var path = resourcePath.substring(classpathPrefix.length());
            final var stream = FileUtil.class.getClassLoader().getResourceAsStream(path);
            if (stream == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            return stream;
        } else if (resourcePath.startsWith(filePrefix)) {
            final var path = resourcePath.substring(filePrefix.length());
            try {
                return new java.io.FileInputStream(path);
            } catch (java.io.FileNotFoundException e) {
                throw new IllegalArgumentException("File not found: " + resourcePath, e);
            }
        } else {
            log.error("Resource not found: {}", resourcePath);
            throw new IllegalArgumentException("Invalid resource path: " + resourcePath + ". Must start with 'classpath:' or 'file:'.");
        }
    }

    @NotNull
    public static String readFileToString(@NotNull final File file, @NotNull final Charset charset) {
        requireNonNull(file);
        requireNonNull(charset);
        if (!file.exists()) throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        if (!file.canRead()) throw new IllegalArgumentException("Cannot read file: " + file.getAbsolutePath());
        if (file.isDirectory()) throw new IllegalArgumentException("File is a directory: " + file.getAbsolutePath());

        try (var inputStream = new java.io.FileInputStream(file)) {
            return new String(inputStream.readAllBytes(), charset);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + file.getAbsolutePath(), e);
        }
    }
}
