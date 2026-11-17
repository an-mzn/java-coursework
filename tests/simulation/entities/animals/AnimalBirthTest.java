package simulation.entities.animals;

import org.junit.jupiter.api.Test;
import simulation.common.Location;
import simulation.field.Field;
import simulation.weather.WeatherCondition;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class AnimalBirthTest {
    static class TestAnimal extends Animal {
        TestAnimal(Location location, Gender gender) { super(location, 10, 100, 1, 1, gender, Color.BLACK); }
        void tick(Field field) { updatePregnancy(field, loc -> new TestAnimal(loc, Gender.FEMALE)); }
        @Override public void act(Field current, Field next, float hour, WeatherCondition weather) { }
    }
    @Test void birthsMatchLitterSize() {
        var field = new Field(3, 3);
        var mother = new TestAnimal(new Location(1, 1), Gender.FEMALE);
        mother.impregnate(3);
        mother.tick(field);
        assertEquals(3, field.getEntities().size());
    }
    @Test void birthsAreLimitedByAvailableSpace() {
        var field = new Field(2, 2);
        var mother = new TestAnimal(new Location(0, 0), Gender.FEMALE);
        mother.impregnate(8);
        mother.tick(field);
        assertEquals(3, field.getEntities().size());
    }
    @Test void maleCannotBecomePregnant() {
        var animal = new TestAnimal(new Location(0, 0), Gender.MALE);
        assertThrows(RuntimeException.class, () -> animal.impregnate(1));
    }
}
