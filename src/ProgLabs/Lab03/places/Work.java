package Lab03.places;

import Lab03.characters.abstracts.Character;
import Lab03.characters.properties.CharacterPosition;
import Lab03.characters.properties.Status;
import Lab03.places.abstracts.AbstractCave;
import Lab03.things.WeightlessnessDevice;
import Lab03.things.abstracts.AbstractWeightlessnessDevice;
import Lab03.things.properties.InCavePosition;
import Lab03.things.properties.SwitchState;
import Lab03.utils.abstracts.CharactersMessagesPrint;
import Lab03.utils.abstracts.CharactersNames;
import Lab03.utils.properties.CharactersActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Work {
    private final AbstractWeightlessnessDevice weightlessnessDevice = new WeightlessnessDevice();
    private Character[] mainCharacters;
    private Character[] minorCharacters;
    private Character[] allCharacters;

    public Work(Character[] mainCharacters, Character[] minorCharacters) {
        this.mainCharacters = mainCharacters;
        this.minorCharacters = minorCharacters;

        this.allCharacters = new Character[mainCharacters.length + minorCharacters.length];

        // Set all characters array
        for (int i = 0; i < allCharacters.length; i++)
            allCharacters[i] = i < minorCharacters.length ? minorCharacters[i] :
                    mainCharacters[i - minorCharacters.length];
    }

    public void exploreCave(AbstractCave cave, Character[] characters) {
        if (characters.length > 0) {
            // Every character
            for (Character character: characters) {
                // Explore all places in cave
                for (InCavePosition position: InCavePosition.values()) {
                    character.setInCavePosition(position);

                    // If he find something add to fondedMaterials and backpack
                    if (cave.isSomethingInPosition(position)) {
                        character.addFoundMaterialInCave(cave.getMaterialInPosition(position), position);
                    }
                }
            }

            // Move back to the center
            for (Character character: characters) {
                character.setInCavePosition(InCavePosition.CENTER);
            }
        }
    }

    public void startWorking() {
        if (weightlessnessDevice.getState() == SwitchState.OFF) {
            weightlessnessDevice.turnOn();

            List<String> messages = new ArrayList<>();
            List<Character> charactersOnFloor = new ArrayList<>();

            for (Character character: allCharacters) {
                String message = character.exposedToChangedWeightlessness();
                messages.add(message);

                if (character.getPosition() == CharacterPosition.ON_FLOOR)
                    charactersOnFloor.add(character);
            }

            CharactersMessagesPrint.optimizePrintCharactersMessages(allCharacters, messages);

            workingWithWeightlessnessDevice(charactersOnFloor.toArray(new Character[0]));

            messages = new ArrayList<>();

            for (Character character: allCharacters) {
                String message = character.reactOnChangedWeightlessness(allCharacters);
                messages.add(message);
            }

            CharactersMessagesPrint.optimizePrintCharactersMessages(allCharacters, messages);
        }
    }

    public void stopWorking() {
        if (weightlessnessDevice.getState() == SwitchState.ON) {
            weightlessnessDevice.turnOff();

            List<String> messages = new ArrayList<>();

            for (Character character: allCharacters) {
                String message = character.exposedToChangedWeightlessness();
                messages.add(message);
            }

            CharactersMessagesPrint.optimizePrintCharactersMessages(allCharacters, messages);
        }
    }

    private void workingWithWeightlessnessDevice(Character[] characters) {
        if (characters.length > 0) {
            String charactersNames = CharactersNames.getCharactersNames(characters);

            // Start working

            for (Character worker: characters) worker.setStatus(Status.WORKING);

            weightlessnessDevice.charactersMovesDevice(charactersNames, InCavePosition.INCOMPREHENSIBLE);

            for (Character worker: characters) worker.setInCavePosition(InCavePosition.CORNER);

            String action = CharactersActions.WALKED_AWAY.getAction(!charactersNames.contains(" и "));
            String appeal = charactersNames.contains(" и ") ? "Каждый из них" : "Он";

            System.out.printf("%s %s от прибора невесомости %s.\n", charactersNames, action, InCavePosition.CORNER);
            System.out.printf("%s %s.\n", appeal, Status.WORKING);

            // End working

            for (Character worker: characters) worker.setStatus(Status.CHILLING);
        }
    }

    public Character[] getMainCharacters() {
        return mainCharacters;
    }

    public Character[] getMinorCharacters() {
        return minorCharacters;
    }

    public Character[] getAllCharacters() {
        return allCharacters;
    }

    public void addMainCharacters(Character... charactersToAdd) {
        ArrayList<Character> mainCharactersList = new ArrayList(Arrays.asList(mainCharacters));
        ArrayList<Character> allCharactersList = new ArrayList(Arrays.asList(allCharacters));

        for (Character newCharacter : charactersToAdd) {
            mainCharactersList.add(newCharacter);
            allCharactersList.add(newCharacter);
        }

        mainCharacters = mainCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    public void addMinorCharacters(Character... charactersToAdd) {
        ArrayList<Character> minorCharactersList = new ArrayList(Arrays.asList(minorCharacters));
        ArrayList<Character> allCharactersList = new ArrayList(Arrays.asList(allCharacters));

        for (Character newCharacter : charactersToAdd) {
            minorCharactersList.add(newCharacter);
            allCharactersList.add(newCharacter);
        }

        minorCharacters = minorCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    public void removeMainCharacters(Character... charactersToRemove) {
        ArrayList<Character> mainCharactersList = new ArrayList(Arrays.asList(mainCharacters));
        ArrayList<Character> allCharactersList = new ArrayList(Arrays.asList(allCharacters));

        for (Character characterToRemove : charactersToRemove) {
            mainCharactersList.remove(characterToRemove);
            allCharactersList.remove(characterToRemove);
        }

        mainCharacters = mainCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    public void removeMinorCharacters(Character... charactersToRemove) {
        ArrayList<Character> minorCharactersList = new ArrayList(Arrays.asList(minorCharacters));
        ArrayList<Character> allCharactersList = new ArrayList(Arrays.asList(allCharacters));

        for (Character characterToRemove : charactersToRemove) {
            minorCharactersList.remove(characterToRemove);
            allCharactersList.remove(characterToRemove);
        }

        minorCharacters = minorCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    @Override
    public String toString() {
        return String.format(
                "%s работают с прибором невесомости в пещере.",
                CharactersNames.getCharactersNames(allCharacters)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (! (obj instanceof Work)) return false;
        Work work = (Work) obj;
        return Objects.equals(weightlessnessDevice, work.weightlessnessDevice) &&
                Arrays.equals(mainCharacters, work.mainCharacters) &&
                Arrays.equals(minorCharacters, work.minorCharacters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(weightlessnessDevice);
        result = 31 * result + Arrays.hashCode(mainCharacters);
        result = 31 * result + Arrays.hashCode(minorCharacters);
        return result;
    }
}
