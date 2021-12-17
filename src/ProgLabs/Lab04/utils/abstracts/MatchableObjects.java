package Lab04.utils.abstracts;

import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.abstracts.AbstractSpecialMaterial;
import Lab04.characters.properties.Expectation;
import Lab04.places.properties.ExperimentResult;
import Lab04.utils.exceptions.NotEqualKeysInMapsException;

import java.util.HashMap;
import java.util.List;

public abstract class MatchableObjects {
    private final HashMap<List<AbstractSpecialMaterial>, Expectation> firstObject;
    private final HashMap<List<AbstractSpecialMaterial>, ExperimentResult> secondObject;

    public MatchableObjects(
            HashMap<List<AbstractSpecialMaterial>, Expectation> firstObject,
            HashMap<List<AbstractSpecialMaterial>, ExperimentResult> secondObject
    ) {
        if (firstObject != null && secondObject != null) {
            this.firstObject = firstObject;
            this.secondObject = secondObject;
        } else {
            throw new NullPointerException("Matchable objects must be not null!");
        }
    }

    public abstract boolean match();
    public abstract Expectation getNotMatchingFirst();
    public abstract ExperimentResult getNotMatchingSecond();

    public class UnionObjects {
        public HashMap<List<AbstractSpecialMaterial>, Boolean> compare(){
            HashMap<List<AbstractSpecialMaterial>, Boolean> comparedUnionObjects = new HashMap<>();

            for (List<AbstractSpecialMaterial> materialsPair: firstObject.keySet()) {
                if (secondObject.containsKey(materialsPair)) {
                    Expectation expectation = firstObject.get(materialsPair);
                    ExperimentResult result = secondObject.get(materialsPair);

                    Boolean compare = expectation.name().equals(result.name());

                    comparedUnionObjects.put(materialsPair, compare);
                } else {
                    String materialsNames = MaterialsNames.getMaterialsNames(
                            materialsPair.toArray(new AbstractMaterial[0]), "а"
                    );

                    throw new NotEqualKeysInMapsException(
                            "Было сделано предположение по взаимодействию " + materialsNames +
                            ", но не был проведён опыт. Невозможно сравнить предположение с результатом."
                    );
                }
            }

            return comparedUnionObjects;
        }
    }
}
