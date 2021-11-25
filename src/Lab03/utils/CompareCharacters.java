package Lab03.utils;

import Lab03.characters.abstracts.Character;
import Lab03.utils.abstracts.CharactersNames;
import Lab03.utils.abstracts.ComparableCharacters;

import java.util.LinkedList;

public class CompareCharacters extends ComparableCharacters {
    public CompareCharacters(Character myCharacter, Character... otherCharacters) {
        this.setMyCharacter(myCharacter);
        this.setOtherCharacters(otherCharacters);
    }

    @Override
    public boolean characterOnMyPosition(Character character) {
        return getMyCharacter().getPosition() == character.getPosition();
    }

    @Override
    public LinkedList<Character> getCharactersOnSamePosition() {
        LinkedList<Character> charactersOnOtherPosition = new LinkedList<>();

        // Find all characters on my position
        for (Character character: getOtherCharacters()) {
            if (characterOnMyPosition(character))
                charactersOnOtherPosition.add(character);
        }

        return charactersOnOtherPosition;
    }

    @Override
    public LinkedList<Character> getCharactersOnOtherPosition() {
        LinkedList<Character> charactersOnOtherPosition = new LinkedList<>();

        // Find all characters on other position
        for (Character character: getOtherCharacters()) {
            if (!characterOnMyPosition(character))
                charactersOnOtherPosition.add(character);
        }

        return charactersOnOtherPosition;
    }

    @Override
    public String getCharactersOnSamePositionNames() {
        LinkedList<Character> charactersOnSamePosition = getCharactersOnSamePosition();

        return CharactersNames.getCharactersNames(charactersOnSamePosition);
    }

    @Override
    public String getCharactersOnOtherPositionNames() {
        LinkedList<Character> charactersOnOtherPosition = getCharactersOnOtherPosition();

        return CharactersNames.getCharactersNames(charactersOnOtherPosition);
    }
}
