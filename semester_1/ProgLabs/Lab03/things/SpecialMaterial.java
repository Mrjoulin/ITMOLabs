package Lab03.things;

import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.abstracts.AbstractSpecialMaterial;
import Lab03.things.properties.Attraction;
import Lab03.things.properties.Color;
import Lab03.things.properties.Hardness;
import Lab03.things.properties.UniqueAbility;
import Lab03.utils.CompareMaterials;
import Lab03.utils.interfaces.ComparableMaterials;

import java.util.HashMap;

public class SpecialMaterial extends AbstractSpecialMaterial {
    private final HashMap<String, String> comparedMaterials = new HashMap<>();

    public SpecialMaterial(String name, Color color, Hardness hardness,
                           Attraction attraction, UniqueAbility uniqueAbility) {
        this.setName(name);
        this.setColor(color);
        this.setHardness(hardness);
        this.setAttraction(attraction);
        this.setUniqueAbility(uniqueAbility);
    }

    @Override
    public void compare(AbstractMaterial anotherMaterial) {
        if (this == anotherMaterial) return;

        String anotherMaterialName = anotherMaterial.getName();
        String compare;

        if (comparedMaterials.containsKey(anotherMaterialName)) {
            compare = comparedMaterials.get(anotherMaterialName);
        } else {
            ComparableMaterials compareMaterials = new CompareMaterials(this, anotherMaterial);
            compare = compareMaterials.getCompareMaterialsText();

            comparedMaterials.put(anotherMaterialName, compare);
        }

        if (!compare.isEmpty()) System.out.println(compare);
    }
}
