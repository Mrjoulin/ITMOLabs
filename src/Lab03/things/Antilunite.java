package Lab03.things;

import Lab03.things.properties.Attraction;
import Lab03.things.properties.Color;
import Lab03.things.properties.Hardness;
import Lab03.things.properties.UniqueAbility;

public class Antilunite extends Material {
    private final UniqueAbility uniqueAbility = UniqueAbility.KEEP_WEIGHT;

    public Antilunite() {
        super("Антилунит", Color.BRIGHT_PURPLE, Hardness.HIGH, Attraction.ORDINARY);
    }

    public UniqueAbility getUniqueAbility() {
        return uniqueAbility;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s цвета, %s, его кусочки %s друг к другу, %s",
                getName(), getColor(), getHardness(), getAttraction(), uniqueAbility
        );
    }
}
