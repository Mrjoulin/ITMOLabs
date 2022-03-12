package Lab03.things.abstracts;

import Lab03.things.properties.UniqueAbility;

public abstract class AbstractSpecialMaterial extends AbstractMaterial {
    private UniqueAbility uniqueAbility;

    public UniqueAbility getUniqueAbility() {
        return uniqueAbility;
    }

    public void setUniqueAbility(UniqueAbility uniqueAbility) {
        this.uniqueAbility = uniqueAbility;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s цвета, %s, его кусочки %s друг к другу, %s",
                getName(), getColor(), getHardness(), getAttraction(), uniqueAbility
        );
    }
}
