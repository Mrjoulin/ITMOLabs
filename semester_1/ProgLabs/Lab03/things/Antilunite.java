package Lab03.things;

import Lab03.things.properties.Attraction;
import Lab03.things.properties.Color;
import Lab03.things.properties.Hardness;
import Lab03.things.properties.UniqueAbility;

public class Antilunite extends SpecialMaterial {
    public Antilunite() {
        super(
                "Антилунит",
                Color.BRIGHT_PURPLE,
                Hardness.HIGH,
                Attraction.ORDINARY,
                UniqueAbility.KEEP_WEIGHT
        );
    }
}
