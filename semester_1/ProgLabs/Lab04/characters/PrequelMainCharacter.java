package Lab04.characters;

import Lab03.characters.abstracts.Character;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.abstracts.AbstractSpecialMaterial;
import Lab03.things.properties.Attraction;
import Lab03.things.properties.UniqueAbility;
import Lab03.utils.CompareCharacters;
import Lab03.utils.interfaces.ComparableCharactersPosition;
import Lab03.utils.properties.CharactersActions;
import Lab04.characters.exceptions.NullOrEmptyStringException;
import Lab04.characters.properties.Expectation;
import Lab04.characters.properties.ExtendedCharactersActions;
import Lab04.utils.abstracts.MaterialsNames;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PrequelMainCharacter extends Character {
    private final HashMap<List<AbstractSpecialMaterial>, Expectation> expectations = new HashMap<>();

    public PrequelMainCharacter(String name, boolean singular) {
        if (name != null && !name.equals("")) {
            setName(name);
            setSingular(singular);
        } else {
            throw new NullOrEmptyStringException(String.format("Invalid name: %s", name));
        }
    }

    public PrequelMainCharacter(String name) {
        this(name, true);
    }

    @Override
    public String reactOnChangedWeightlessness(Character[] characters) {
        if (characters == null) throw new NullPointerException("Characters must be an array, null given");

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (!charactersOnOtherPositionNames.isEmpty()) {
            String characterAction = ExtendedCharactersActions.SEE.getAction(isSingular());
            String otherAction = CharactersActions.ENDED_UP.getAction(!charactersOnOtherPositionNames.contains(" и "));

            return String.format(
                    "<name> %s, что %s %s %s.",
                    characterAction, charactersOnOtherPositionNames, otherAction, getPosition().changed()
            );
        }

        return null;
    }

    public String expectationFromMaterialsInteraction(AbstractSpecialMaterial first, AbstractSpecialMaterial second) {
        if (first == null || second == null)
            throw new NullPointerException("Some of materials are null! Unable to make expectation");

        if (first.equals(second))
            return "Даны два одинаковых материала!";

        List<AbstractSpecialMaterial> materialsPair = Arrays.asList(first, second);
        List<AbstractSpecialMaterial> reverseMaterialsPair = Arrays.asList(second, first);
        Expectation expect;

        // If we already predict result of interaction
        if (expectations.containsKey(materialsPair) || expectations.containsKey(reverseMaterialsPair)) {
            expect = expectations.containsKey(materialsPair) ?
                    expectations.get(materialsPair) : expectations.get(reverseMaterialsPair);
        } else {
            // Check some cases
            if (
                    first.getAttraction() != null && first.getAttraction() == Attraction.STRONG &&
                    second.getUniqueAbility() != null && second.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY ||
                    first.getUniqueAbility() != null && first.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY &&
                    second.getAttraction() != null && second.getAttraction() == Attraction.STRONG
            ) {
                // Interaction of material with changing gravity with material with strong attraction
                expect = Expectation.EMERGENCE_WEIGHTLESSNESS;
            } else if (
                    first.getUniqueAbility() != null && first.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY &&
                    second.getUniqueAbility() != null && second.getUniqueAbility() == UniqueAbility.KEEP_WEIGHT ||
                    first.getUniqueAbility() != null && first.getUniqueAbility() == UniqueAbility.KEEP_WEIGHT &&
                    second.getUniqueAbility() != null && second.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY
            ) {
                // Interaction of material with changing gravity with material with keeping weight
                expect = Expectation.EVERYTHING_EXPLODE;
            } else {
                expect = Expectation.NOTHING_HAPPENED;
            }

            expectations.put(materialsPair, expect);
        }

        String action = ExtendedCharactersActions.EXPECTED.getAction(isSingular());
        String materialsName = MaterialsNames.getMaterialsNames(materialsPair.toArray(new AbstractMaterial[0]), "а");

        return String.format(
            "<name> %s, что при взимодействии %s %s.",
            action, materialsName, expect
        );
    }

    public HashMap<List<AbstractSpecialMaterial>, Expectation> getExpectations() {
        return expectations;
    }
}
