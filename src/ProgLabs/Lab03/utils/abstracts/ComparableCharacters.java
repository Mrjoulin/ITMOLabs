package Lab03.utils.abstracts;

import Lab03.characters.abstracts.Character;
import Lab03.utils.interfaces.ComparableCharactersPosition;

import java.util.Arrays;
import java.util.Objects;

public abstract class ComparableCharacters implements ComparableCharactersPosition {
    private Character myCharacter;
    private Character[] otherCharacters;

    public Character getMyCharacter() {
        return myCharacter;
    }

    public Character[] getOtherCharacters() {
        return otherCharacters;
    }

    public void setMyCharacter(Character myCharacter) {
        this.myCharacter = myCharacter;
    }

    public void setOtherCharacters(Character[] otherCharacters) {
        this.otherCharacters = otherCharacters;
    }

    @Override
    public String toString() {
        return "Сравним " + myCharacter + " с "+ Arrays.toString(otherCharacters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ComparableCharacters)) return false;
        ComparableCharacters comparableCharacters = (ComparableCharacters) obj;
        return Objects.equals(myCharacter, comparableCharacters.myCharacter) &&
                Arrays.equals(otherCharacters, comparableCharacters.otherCharacters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(myCharacter);
        result = 31 * result + Arrays.hashCode(otherCharacters);
        return result;
    }
}
