package dev.lunna.nana.translator.test;

import dev.lunna.nana.translator.util.ArrayUtil;
import org.junit.jupiter.api.Test;

public class ArrayUtilTest {
    @Test
    public void divideStringArray() {
        String[] array = new String[] {
                "This is a test",
                "This is another test",
                "This is a third test",
                "This is a fourth test",
                "This is a fifth test",
                "This is a sixth test",
                "This is a seventh test",
                "This is an eighth test",
                "This is a ninth test",
                "This is a tenth test"
        };

        String[][] divided = ArrayUtil.divideArray(array, 3);

        assert divided.length == 3;
        assert divided[0].length == 4;
        assert divided[1].length == 4;
        assert divided[2].length == 2;
    }
}
