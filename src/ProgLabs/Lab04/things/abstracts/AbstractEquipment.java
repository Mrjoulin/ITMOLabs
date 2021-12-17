package Lab04.things.abstracts;

import Lab04.characters.exceptions.NullOrEmptyStringException;
import Lab04.things.properties.Size;
import Lab04.things.properties.Weight;

import java.util.Objects;

public abstract class AbstractEquipment {
    private Weight equipmentWeight;
    private Size equipmentSize;
    private String name;

    public AbstractEquipment(String name, Size equipmentSize, Weight equipmentWeight) {
        if (name == null || name.equals(""))
            throw new NullOrEmptyStringException(String.format("Invalid name: %s", name));

        if (equipmentSize != null && equipmentWeight != null) {
            this.name = name;
            this.equipmentSize = equipmentSize;
            this.equipmentWeight = equipmentWeight;
        } else {
            throw new NullPointerException("Equipment size and weight must be not null!");
        }
    }


    public String getName() {
        return name;
    }

    public Size getEquipmentSize() {
        return equipmentSize;
    }

    public Weight getEquipmentWeight() {
        return equipmentWeight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEquipmentSize(Size equipmentSize) {
        this.equipmentSize = equipmentSize;
    }

    public void setEquipmentWeight(Weight equipmentWeight) {
        this.equipmentWeight = equipmentWeight;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", equipmentSize, equipmentWeight, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEquipment)) return false;
        AbstractEquipment that = (AbstractEquipment) o;
        return equipmentWeight == that.equipmentWeight &&
                equipmentSize == that.equipmentSize &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentWeight, equipmentSize, name);
    }
}
