package catalog;

import catalog.classes.Audio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AudioTest {

    Audio audio;

    @Test
    public void createAudioWithValidValuesTest() {
        audio = new Audio("Ez a város", List.of("Aurora"), List.of("Vigi"), 1990, 3);

        assertEquals("Ez a város", audio.getTitle());
        assertEquals(List.of("Aurora"), audio.getPerformers());
        assertEquals(List.of("Vigi"), audio.getComposers());
        assertEquals(1990, audio.getYearOfPublication());
        assertEquals(3, audio.getQuantity());
    }

    @Test
    public void createAudioWithInvalidValuesTest() {

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio("", List.of("Aurora"), List.of("Vigi"), 1990, 3));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio(null, List.of("Aurora"), List.of("Vigi"), 1990, 3));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio("Ez a város", null, List.of("Vigi"), 1990, 3));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio("Ez a város", List.of("Aurora"), null, 1990, 3));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio("Ez a város", List.of("Aurora"), List.of("Vigi"), 999, 3));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio("Ez a város", List.of("Aurora"), List.of("Vigi"),
                        LocalDate.now().getYear() + 1, 3));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Audio("Ez a város", List.of("Aurora"), List.of("Vigi"), 1990, -1));
        assertEquals(IllegalArgumentException.class, expected.getClass());
    }
}