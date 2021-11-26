package Lab03.places;

import Lab03.places.abstracts.AbstractCave;
import Lab03.things.Antilunite;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.InCavePosition;

public class Cave extends AbstractCave {
    @Override
    public void fullCave() {
        for (InCavePosition position: InCavePosition.values()) {
            if (position.isSpecial()) {
                AbstractMaterial antilunite = new Antilunite();

                addMaterialInCave(position, antilunite);
            } else {
                addMaterialInCave(position, null);
            }
        }
    }
}
