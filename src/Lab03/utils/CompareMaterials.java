package Lab03.utils;

import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.Attraction;
import Lab03.utils.interfaces.ComparableMaterials;

import java.util.Objects;

public class CompareMaterials implements ComparableMaterials {
    AbstractMaterial myMaterial;
    AbstractMaterial anotherMaterial;

    public CompareMaterials(AbstractMaterial myMaterial, AbstractMaterial anotherMaterial) {
        this.myMaterial = myMaterial;
        this.anotherMaterial = anotherMaterial;
    }

    @Override
    public String getCompareMaterialsText() {
        boolean sameColor = myMaterial.getColor() == anotherMaterial.getColor();
        boolean sameHardness = myMaterial.getHardness() == anotherMaterial.getHardness();
        boolean sameAttraction = myMaterial.getAttraction() == anotherMaterial.getAttraction();

        String compare = "";

        if (sameColor && sameHardness && sameAttraction) {
            compare = String.format(
                    "%s по всем параметрам был очень похож на %s: %s, %s цвета, его кусочки %s друг к дургу.",
                    myMaterial.getName(), anotherMaterial.getName(), myMaterial.getHardness(),
                    myMaterial.getColor(), myMaterial.getAttraction()
            );
        } else if (sameColor && sameHardness) {
            compare = String.format(
                    "%s был %s, по виду напоминал %s, он был тоже %s цвета.",
                    myMaterial.getName(), myMaterial.getHardness(), anotherMaterial.getName(), myMaterial.getColor()
            );
        }  else if (sameHardness && sameAttraction) {
            compare = String.format(
                    "%s был %s, по виду напоминал %s, его кусочки тоже %s друг к другу.",
                    myMaterial.getName(), myMaterial.getHardness(),
                    anotherMaterial.getName(), myMaterial.getAttraction()
            );
        } else if (sameColor) {
            compare = String.format(
                    "%s напоминал %s, он был тоже %s цвета.",
                    myMaterial.getName(), anotherMaterial.getName(), myMaterial.getColor()
            );

            if (sameAttraction && myMaterial.getAttraction() != Attraction.NO) {
                compare += String.format(
                        ", но помимо этого, его кусочки %s друг другу, как %s друг к другу кусочки %s.",
                        myMaterial.getAttraction(), myMaterial.getAttraction(), anotherMaterial.getName()
                );
            }
        } else if (sameHardness) {
            compare = String.format(
                    "%s был %s, по виду напоминал %s, но в отличие от его %s цвета, %s был %s.",
                    myMaterial.getName(), myMaterial.getHardness(), anotherMaterial.getName(),
                    anotherMaterial.getColor(), myMaterial.getName(), myMaterial.getColor()
            );
        } else if (sameAttraction && myMaterial.getAttraction() != Attraction.NO) {
            compare = String.format(
                    "%s обладал какой-то энергией, в силу которой его кусочки %s друг к другу, " +
                            "подобно тому, как %s %s.",
                    myMaterial.getName(), myMaterial.getAttraction(),
                    myMaterial.getAttraction(), anotherMaterial.getName()
            );
        }

        return compare;
    }

    @Override
    public String toString() {
        return "Сравним " + myMaterial + " c " + anotherMaterial;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CompareMaterials)) return false;
        CompareMaterials compareMaterials = (CompareMaterials) obj;
        return myMaterial.equals(compareMaterials.myMaterial) &&
                anotherMaterial.equals(compareMaterials.anotherMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myMaterial, anotherMaterial);
    }
}
