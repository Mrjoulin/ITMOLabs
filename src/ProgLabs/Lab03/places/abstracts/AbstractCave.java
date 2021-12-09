package Lab03.places.abstracts;

import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.InCavePosition;

import java.util.HashMap;
import java.util.Objects;

public abstract class AbstractCave {
    private final HashMap<InCavePosition, AbstractMaterial> materialsInCave = new HashMap<>();

    public abstract void fullCave();

    public boolean isSomethingInPosition(InCavePosition position) {
        return materialsInCave.containsKey(position) && materialsInCave.get(position) != null;
    }

    public AbstractMaterial getMaterialInPosition(InCavePosition position) {
        return isSomethingInPosition(position) ? materialsInCave.get(position) : null;
    }

    public HashMap<InCavePosition, AbstractMaterial> getMaterialsInCave() {
        return materialsInCave;
    }

    public void addMaterialInCave(InCavePosition position, AbstractMaterial material) {
        this.materialsInCave.put(position, material);
    }

    @Override
    public String toString() {
        return "Таинственная пещера с разными материалами";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AbstractCave)) return false;
        AbstractCave abstractCave = (AbstractCave) obj;
        return Objects.equals(materialsInCave, abstractCave.materialsInCave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialsInCave);
    }
}
