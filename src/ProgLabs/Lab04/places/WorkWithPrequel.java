package Lab04.places;

import Lab03.characters.abstracts.Character;
import Lab03.places.Work;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.abstracts.AbstractSpecialMaterial;
import Lab03.things.properties.Attraction;
import Lab03.things.properties.UniqueAbility;
import Lab03.utils.abstracts.CharactersNames;
import Lab04.characters.properties.Expectation;
import Lab04.characters.properties.ExtendedCharactersActions;
import Lab04.places.exceptions.UnexpectedExperimentResult;
import Lab04.utils.abstracts.CharactersMessagesPrint;
import Lab04.places.properties.ExperimentResult;
import Lab04.utils.abstracts.MatchableObjects;
import Lab04.utils.abstracts.MaterialsNames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WorkWithPrequel extends Work {
    private final HashMap<List<AbstractSpecialMaterial>, ExperimentResult> experimentsResult = new HashMap<>();
    private boolean weightlessnessInResult = false;

    public WorkWithPrequel(Character[] mainCharacters, Character[] minorCharacters) {
        super(mainCharacters, minorCharacters);
    }

    public void makeAnExperiment(AbstractSpecialMaterial first, AbstractSpecialMaterial second) {
        if (first == null || second == null)
            throw new NullPointerException("Some of materials are null! Unable to make an experiment");

        if (first.equals(second))
            System.out.println("Даны два одинаковых материала!");

        List<AbstractSpecialMaterial> materialsPair = Arrays.asList(first, second);
        List<AbstractSpecialMaterial> reverseMaterialsPair = Arrays.asList(second, first);
        ExperimentResult result;

        // If we already done this experiment
        if (experimentsResult.containsKey(materialsPair) || experimentsResult.containsKey(reverseMaterialsPair)) {
            result = experimentsResult.containsKey(materialsPair) ?
                    experimentsResult.get(materialsPair) : experimentsResult.get(reverseMaterialsPair);
        } else {
            // Check some cases
            if (
                    first.getAttraction() != null && first.getAttraction() == Attraction.STRONG &&
                    second.getUniqueAbility() != null && second.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY ||
                    first.getUniqueAbility() != null && first.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY &&
                    second.getAttraction() != null && second.getAttraction() == Attraction.STRONG
            ) {
                // Interaction of material with changing gravity with material with strong attraction
                result = ExperimentResult.EMERGENCE_WEIGHTLESSNESS;
            } else if (
                    first.getUniqueAbility() != null && first.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY &&
                    second.getUniqueAbility() != null && second.getUniqueAbility() == UniqueAbility.KEEP_WEIGHT ||
                    first.getUniqueAbility() != null && first.getUniqueAbility() == UniqueAbility.KEEP_WEIGHT &&
                    second.getUniqueAbility() != null && second.getUniqueAbility() == UniqueAbility.CHANGE_GRAVITY
            ) {
                // Interaction of material with changing gravity with material with keeping weight
                result = ExperimentResult.BLACK_HOLE_APPEARED;
            } else {
                result = ExperimentResult.NOTHING_HAPPENED;
            }

            experimentsResult.put(materialsPair, result);
        }

        String materialsName = MaterialsNames.getMaterialsNames(materialsPair.toArray(new AbstractMaterial[0]), "а");

        System.out.printf(
                "В результате эксперимента, при взимодействии %s %s.\n",
                materialsName, result
        );
    }

    public void charactersExperimentConclusions(
            Character[] characters, HashMap<List<AbstractSpecialMaterial>, Expectation> expectations
    ) throws UnexpectedExperimentResult {

        if (characters == null) throw new NullPointerException("Characters must be an array, null given");
        if (expectations == null) return;

        // Create match Expectations with experiment local class
        // It can be anonymous, but lab 4 requires locals too :(

        class MatchExpectationsWithExperiment extends MatchableObjects {
            private HashMap<List<AbstractSpecialMaterial>, Boolean> comparedUnionObjects;

            public MatchExpectationsWithExperiment(
                    HashMap<List<AbstractSpecialMaterial>, Expectation> firstObject,
                    HashMap<List<AbstractSpecialMaterial>, ExperimentResult> secondObject
            ) {
                super(firstObject, secondObject);
            }

            @Override
            public boolean match() {
                UnionObjects unionObjects = new UnionObjects();
                comparedUnionObjects = unionObjects.compare();

                return !comparedUnionObjects.containsValue(Boolean.FALSE);
            }

            @Override
            public Expectation getNotMatchingFirst(){
                for (List<AbstractSpecialMaterial> materials: comparedUnionObjects.keySet())
                    if (!comparedUnionObjects.get(materials)) return expectations.get(materials);
                return null;
            }

            @Override
            public ExperimentResult getNotMatchingSecond(){
                for (List<AbstractSpecialMaterial> materials: comparedUnionObjects.keySet())
                    if (!comparedUnionObjects.get(materials)) return experimentsResult.get(materials);
                return null;
            }
        }

        MatchableObjects matchableObjects = new MatchExpectationsWithExperiment(expectations, experimentsResult);

        // Match expectations with experiment

        boolean allExpectationsRight = matchableObjects.match();

        String charactersNames = CharactersNames.getCharactersNames(characters);
        boolean singular = !charactersNames.contains(" и ");

        if (allExpectationsRight) {
            StringBuilder materialsNames = new StringBuilder();
            StringBuilder resultsNames = new StringBuilder();

            // Make strings with materials names and results
            for (List<AbstractSpecialMaterial> materials: experimentsResult.keySet()) {
                String curMaterialsNames = MaterialsNames.getMaterialsNames(
                        materials.toArray(new AbstractMaterial[0]), "а"
                );

                if (materialsNames.length() > 0) materialsNames.append("; ");
                materialsNames.append(curMaterialsNames);
                resultsNames.append(experimentsResult.get(materials)).append(",");

                weightlessnessInResult |= experimentsResult.get(materials) == ExperimentResult.EMERGENCE_WEIGHTLESSNESS;
            }

            String action = ExtendedCharactersActions.EXPECTED.getAction(singular);

            System.out.printf(
                    "Как и %s %s, %s при взаимодействии %s.\n",
                    action, charactersNames, resultsNames, materialsNames
            );
        } else {
            // Something went wrong
            throw new UnexpectedExperimentResult(
                    matchableObjects.getNotMatchingFirst(), matchableObjects.getNotMatchingSecond()
            );
        }
    }

    public void consequencesOfTheExperiment(){
        if (weightlessnessInResult) {
            // All characters exposed to changed weightlessness and react on that
            List<String> messages = new ArrayList<>();

            for (Character character : getAllCharacters())
                messages.add(character.exposedToChangedWeightlessness());

            CharactersMessagesPrint.optimizePrintCharactersMessages(getAllCharacters(), messages);

            messages = new ArrayList<>();

            for (Character character : getAllCharacters())
                messages.add(character.reactOnChangedWeightlessness(getAllCharacters()));

            CharactersMessagesPrint.optimizePrintCharactersMessages(getAllCharacters(), messages);

            // All characters were used as a result of experience, so we reset the result
            weightlessnessInResult = false;
        }
    }


    public boolean isWeightlessnessInResult() {
        return weightlessnessInResult;
    }

    public HashMap<List<AbstractSpecialMaterial>, ExperimentResult> getExperimentsResult() {
        return experimentsResult;
    }
}
