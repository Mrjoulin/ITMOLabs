package Lab03.places;

import Lab03.characters.abstracts.Character;
import Lab03.characters.properties.CharacterPosition;
import Lab03.characters.properties.Status;
import Lab03.things.abstracts.AbstractWeightlessnessDevice;
import Lab03.things.WeightlessnessDevice;
import Lab03.things.properties.InCavePosition;
import Lab03.things.properties.SwitchState;
import Lab03.utils.abstracts.CharactersNames;

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

    private void workingWithWeightlessnessDevice(Character[] characters) {
        if (characters.length > 0) {
            String charactersNames = CharactersNames.getCharactersNames(characters);

            // Start working

            for (Character worker: characters) worker.setStatus(Status.WORKING);

            weightlessnessDevice.charactersMovesDevice(charactersNames, InCavePosition.INCOMPREHENSIBLE);

            for (Character worker: characters) worker.setInCavePosition(InCavePosition.CORNER);

            String action = charactersNames.contains(" и ") ? "отошли" : "отошёл";
            String appeal = charactersNames.contains(" и ") ? "Каждый из них" : "Он";

            System.out.printf("%s %s от прибора невесомости %s.\n", charactersNames, action, InCavePosition.CORNER);
            System.out.printf("%s %s.\n", appeal, Status.WORKING);

            // End working

            for (Character worker: characters) worker.setStatus(Status.CHILLING);
        }
    }

    public void startWorking() {
        if (weightlessnessDevice.getState() == SwitchState.OFF) {
            weightlessnessDevice.turnOn();

            List<Character> charactersOnFloor = new ArrayList<>();

            for (Character character: allCharacters) {
                character.exposedToChangedWeightlessness();

                if (character.getPosition() == CharacterPosition.ON_FLOOR)
                    charactersOnFloor.add(character);
            }

            workingWithWeightlessnessDevice(charactersOnFloor.toArray(new Character[0]));

            for (Character character: allCharacters)
                character.reactOnChangedWeightlessness(allCharacters);
        }
    }

    public void stopWorking() {
        if (weightlessnessDevice.getState() == SwitchState.ON) {
            weightlessnessDevice.turnOff();

            for (Character character: allCharacters)
                character.exposedToChangedWeightlessness();
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
        List<Character> mainCharactersList = Arrays.asList(mainCharacters);
        List<Character> allCharactersList = Arrays.asList(allCharacters);

        for (Character newCharacter : charactersToAdd) {
            mainCharactersList.add(newCharacter);
            allCharactersList.add(newCharacter);
        }

        mainCharacters = mainCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    public void addMinorCharacters(Character... charactersToAdd) {
        List<Character> minorCharactersList = Arrays.asList(minorCharacters);
        List<Character> allCharactersList = Arrays.asList(allCharacters);

        for (Character newCharacter : charactersToAdd) {
            minorCharactersList.add(newCharacter);
            allCharactersList.add(newCharacter);
        }

        minorCharacters = minorCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    public void removeMainCharacters(Character... charactersToRemove) {
        List<Character> mainCharactersList = Arrays.asList(mainCharacters);
        List<Character> allCharactersList = Arrays.asList(allCharacters);

        for (Character characterToRemove : charactersToRemove) {
            mainCharactersList.remove(characterToRemove);
            allCharactersList.remove(characterToRemove);
        }

        mainCharacters = mainCharactersList.toArray(new Character[0]);
        allCharacters = allCharactersList.toArray(new Character[0]);
    }

    public void removeMinorCharacters(Character... charactersToRemove) {
        List<Character> minorCharactersList = Arrays.asList(minorCharacters);
        List<Character> allCharactersList = Arrays.asList(allCharacters);

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
