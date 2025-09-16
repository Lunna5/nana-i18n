package dev.lunna.nana.translator.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtil {
    public static <T> T[][] divideArray(T[] array, int parts) {
        int len = array.length;
        int chunkSize = (len + parts - 1) / parts;

        // Create the result array: T[parts][]
        @SuppressWarnings("unchecked")
        T[][] result = (T[][]) Array.newInstance(array.getClass(), parts);

        for (int i = 0; i < parts; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, len);
            if (start < len) {
                // Use Arrays.copyOfRange to create a sub-array of the same type
                result[i] = Arrays.copyOfRange(array, start, end);
            } else {
                // Create an empty array of type T[]
                result[i] = Arrays.copyOfRange(array, 0, 0);
            }
        }
        return result;
    }
}
