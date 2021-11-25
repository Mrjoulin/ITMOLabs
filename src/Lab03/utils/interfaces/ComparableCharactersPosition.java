package Lab03.utils.interfaces;

import Lab03.characters.abstracts.Character;

import java.util.List;

public interface ComparableCharactersPosition {
    boolean characterOnMyPosition(Character character);
    List<Character> getCharactersOnSamePosition();
    List<Character> getCharactersOnOtherPosition();
    String getCharactersOnSamePositionNames();
    String getCharactersOnOtherPositionNames();
}
