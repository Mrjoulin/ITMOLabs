package Lab03.things.abstracts;

import Lab03.things.properties.Attraction;
import Lab03.things.properties.Color;
import Lab03.things.properties.Hardness;

import java.util.Objects;

public abstract class AbstractMaterial {
    private String name;
    private Color color;
    private Hardness hardness;
    private Attraction attraction;

    public abstract void compare(AbstractMaterial anotherMaterial);

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Hardness getHardness() {
        return hardness;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHardness(Hardness hardness) {
        this.hardness = hardness;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s цвета, %s, его кусочки %s друг к другу.",
                name, color, hardness, attraction
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (! (obj instanceof AbstractMaterial)) return false;
        AbstractMaterial material = (AbstractMaterial) obj;
        return name.equals(material.name) && color == material.color &&
                hardness == material.hardness && attraction == material.attraction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, hardness, attraction);
    }

}
