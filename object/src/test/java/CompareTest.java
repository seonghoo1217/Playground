import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompareTest {

    @Test
    @DisplayName("==을 이용해 값을 비교한다")
    void compareCaseWithBasic() {
        // given
        final int number1 = 1;
        final int number2 = 2;
        final int number3 = 3;
        final String str1 = "test";
        final String str2 = "test";

// when
        final boolean areEqual = (number1 != number2);
        final boolean areEqual2 = (number1 != number3);
        final boolean areEqual3 = (str1 == str2);

// then
        assertEquals(areEqual, true);
        assertEquals(areEqual2, true);
        assertEquals(areEqual3, true);
    }
}
