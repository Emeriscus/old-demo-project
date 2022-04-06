package catalog;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static catalog.utils.Validators.*;
import static org.junit.jupiter.api.Assertions.*;


public class ValidatorsTest {

    @Test
    public void isBlankTest() {
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank("    "));
        assertFalse(isBlank("a"));
        assertFalse(isBlank("     a       "));
    }

    @Test
    public void isEmptyTest() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(List.of()));
        assertFalse(isEmpty(List.of("")));
        assertFalse(isEmpty(List.of("aaa")));
        assertFalse(isEmpty(List.of("aaa", "bbb")));
    }

    @Test
    public void isNumberTest() {

        assertTrue(isNumber("0"));
        assertTrue(isNumber("34"));
        assertFalse(isNumber(""));
        assertFalse(isNumber("dsfgsd"));
    }

    @Test
    public void isValidYearTest() {
        assertTrue(isValidYear(1000));
        assertTrue(isValidYear(LocalDate.now().getYear()));
        assertFalse(isValidYear(999));
        assertFalse(isValidYear(LocalDate.now().getYear() + 1));
    }

    @Test
    public void isValidQuantityTest() {
        assertTrue(isValidQuantity(1000));
        assertTrue(isValidQuantity(0));
        assertFalse(isValidQuantity(-1));
    }
}