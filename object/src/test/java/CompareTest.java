import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Test
    @DisplayName("정수형 참조 비교")
    void compareInteger() {
        //given
        final Integer number1 = 2024;
        final Integer number2 = 2024;

        //when
        final boolean areEqual = (number1 == number2);

        //then
        assertEquals(areEqual, false);
    }

    @Test
    @DisplayName("문자열 참조 비교")
    void compareString() {
        String test1 = "testString";
        String test2 = "testString";
        String test3 = new String("testString");
        String test4 = new String("testString");

        assertEquals(System.identityHashCode(test1), System.identityHashCode(test2));
        assertNotEquals(System.identityHashCode(test3), System.identityHashCode(test4));
    }

    @Test
    void basicCompareInteger() {
        // given
        final Integer number1 = 100;  // before : 2000
        final Integer number2 = 100;  // before : 2000
        
        // when
        final boolean areEqual = (number1 == number2);

        // then
        assertEquals(areEqual, true);
    }
}
