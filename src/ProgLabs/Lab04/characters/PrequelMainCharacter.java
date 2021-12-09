package Lab04.characters;

import Lab03.characters.abstracts.Character;
import Lab03.things.SpecialMaterial;
import Lab03.things.properties.Attraction;
import Lab03.things.properties.UniqueAbility;
import Lab03.utils.CompareCharacters;
import Lab03.utils.interfaces.ComparableCharactersPosition;
import Lab03.utils.properties.CharactersActions;
import Lab04.characters.properties.Expectation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PrequelMainCharacter extends Character {
    private final HashMap<List<SpecialMaterial>, Expectation> expectations = new HashMap<>();

    public PrequelMainCharacter(String name, boolean singular) {
        setName(name);
        setSingular(singular);
    }

    public PrequelMainCharacter(String name) {
        this(name, true);
    }

    @Override
    public String reactOnChangedWeightlessness(Character[] characters) {
        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (!charactersOnOtherPositionNames.isEmpty()) {
            // TODO change object state

            String action = CharactersActions.STAY.getAction(!charactersOnOtherPositionNames.contains(" и "));

            return String.format(
                    "<name>, что %s %s %s.",
                    charactersOnOtherPositionNames, action, getPosition().changed()
            );
        }

        return null;
    }

    public String expectationFromMaterialsInteraction(SpecialMaterial first, SpecialMaterial second) {
        List<SpecialMaterial> materialsPair = Arrays.asList(first, second);
        List<SpecialMaterial> reverseMaterialsPair = Arrays.asList(second, first);
        Expectation expect;

        if (expectations.containsKey(materialsPair) || expectations.containsKey(reverseMaterialsPair)) {
            expect = expectations.containsKey(materialsPair) ?
                    expectations.get(materialsPair) : expectations.get(reverseMaterialsPair);
        } else {
            if (
                    first.getAttraction() == Attraction.STRONG &&
                            second.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY ||
                            first.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY &&
                                    second.getAttraction() == Attraction.STRONG
            ) {
                // Interaction of material with changing gravity with material with strong attraction
                expect = Expectation.EMERGENCE_WEIGHTLESSNESS;
            } else if (
                    first.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY &&
                            second.getUniqueAbility() == UniqueAbility.KEEP_WEIGHT ||
                            first.getUniqueAbility() == UniqueAbility.KEEP_WEIGHT &&
                                    second.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY
            ) {
                // Interaction of material with changing gravity with material with keeping weight
                expect = Expectation.EVERYTHING_EXPLODE;
            } else {
                expect = Expectation.NOTHING_HAPPENED;
            }

            expectations.put(materialsPair, expect);
        }

        return String.format(
            "<name> ожитает, что при взимодействии %s и %s %s",
            first.getName(), second.getName(), expect
        );
    }

    public HashMap<List<SpecialMaterial>, Expectation> getExpectations() {
        return expectations;
    }
}
