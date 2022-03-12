package Lab04.utils.abstracts;

import Lab03.things.abstracts.AbstractMaterial;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public abstract class MaterialsNames {
    public static String getMaterialsNames(LinkedList<AbstractMaterial> materials, String materialPostfix) {
        if (!materials.isEmpty()) {
            AbstractMaterial[] lastMaterials = {materials.pollLast(), materials.pollLast()};

            // Set materials names like: "<Name1>, <Name2>, ... , <NameN> и <Last Name in List>"

            StringBuilder names = new StringBuilder();
            for (AbstractMaterial material : materials) {
                names.append(material.getName()).append(materialPostfix).append(", ");
            }

            names.append(lastMaterials[0].getName()).append(materialPostfix);

            if (lastMaterials[1] != null)
                names.append(" и ").append(lastMaterials[1].getName()).append(materialPostfix);

            return names.toString();
        }

        return "";
    }

    public static String getMaterialsNames(AbstractMaterial[] materials, String materialPostfix) {
        LinkedList<AbstractMaterial> materialsLList = new LinkedList<>(Arrays.asList(materials));
        return getMaterialsNames(materialsLList, materialPostfix);
    }

    public static String getMaterialsNames(AbstractMaterial[] materials) {
        return getMaterialsNames(materials, "");
    }

    @Override
    public String toString() {
        return "Получим имена материалов в одной строке";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof MaterialsNames;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(toString());
    }
}
