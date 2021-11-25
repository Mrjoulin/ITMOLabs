package Lab03.utils.abstracts;

import Lab03.characters.abstracts.Character;
import Lab03.utils.CompareMaterials;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public abstract class CharactersNames {
    public static String getCharactersNames(LinkedList<Character> characters) {
        if (!characters.isEmpty()) {
            Character[] lastCharacters = {characters.pollLast(), characters.pollLast()};

            // Set characters names like: "<Name1>, <Name2>, ... , <NameN> и <Last Name in List>"

            StringBuilder names = new StringBuilder();
            for (Character character : characters) {
                names.append(character).append(", ");
            }

            names.append(lastCharacters[0]);
            if (lastCharacters[1] != null) names.append(" и ").append(lastCharacters[1]);

            return names.toString();
        }

        return null;
    }

    public static String getCharactersNames(Character[] characters) {
        LinkedList<Character> charactersLList = new LinkedList<>(Arrays.asList(characters));
        return getCharactersNames(charactersLList);
    }

    @Override
    public String toString() {
        return "Получим имена персонажей в одной строке";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        return obj instanceof CompareMaterials;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(toString());
    }
}
