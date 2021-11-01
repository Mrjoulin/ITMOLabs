package Lab02.utils;

import ru.ifmo.se.pokemon.Type;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class DefaultStats {
    // Stats names in both file
    public static final String EXTENDS = "extends";
    public static final String TYPE = "type";

    // Stats names in pokemons file
    public static final String HP = "hp";
    public static final String ATTACK = "attack";
    public static final String DEFENSE = "defense";
    public static final String SPECIAL_ATTACK = "specialAttack";
    public static final String SPECIAL_DEFENSE = "specialDefense";
    public static final String SPEED = "speed";
    public static final String ATTACKS = "attacks";

    // Stats names in attacks file
    public static final String POWER = "power";
    public static final String ACCURACY = "accuracy";
    public static final String PRIORITY = "priority";
    public static final String HITS = "hits";
    public static final String OPPONENT_EFFECTS = "oppEffects";
    public static final String SELF_EFFECTS = "selfEffects";
    public static final String OPPONENT_DAMAGE = "oppDamage";
    public static final String SELF_DAMAGE = "selfDamage";
    public static final String DESCRIBE = "describe";

    private static final String pokemonsFileName = "Lab02/utils/pokemons.csv";
    private static final String attacksFileName = "Lab02/utils/attacks.csv";
    private static List<List<String>> pokemonsFileData = null;
    private static List<List<String>> attacksFileData = null;
    private static final int nameColumnIndex = 0;

    public DefaultStats() {
        pokemonsFileData = getFileData(pokemonsFileName);
        attacksFileData = getFileData(attacksFileName);
    }

    private static List<List<String>> getFileData(String fileName) {
        if (fileName.equals(pokemonsFileName) && pokemonsFileData != null) return pokemonsFileData;
        if (fileName.equals(attacksFileName) && attacksFileData != null) return attacksFileData;

        List<List<String>> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                data.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file: " + fileName);
            e.printStackTrace();
        }

        return data;
    }

    private static List<List<String>> checkNameInFile(String fileName, String className) {
        /*
            Return List of two Lists: keys and data
            If name not in the file return null
        */
        List<List<String>> fileData = getFileData(fileName);

        for (int i = 1; i < fileData.size(); i++) {
            if (fileData.get(i).get(nameColumnIndex).equals(className)) {
                List<List<String>> data = new ArrayList<>();

                data.add(fileData.get(0));
                data.add(fileData.get(i));

                return data;
            }
        }

        return null;
    }

    private static List<List<String>> checkPokemonInFile(String pokemonClassName) {
        return checkNameInFile(pokemonsFileName, pokemonClassName);
    }

    private static List<List<String>> checkAttackInFile(String attackClassName) {
        return checkNameInFile(attacksFileName, attackClassName);
    }

    private static HashMap<String, Double> createMapWithStats(List<String> keys, List<String> values) {
        HashMap<String, Double> resultMap = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            try {
                resultMap.put(keys.get(i), Double.parseDouble(values.get(i)));
            } catch(NumberFormatException ignored) {}
        }

        return resultMap;
    }

    private static List<List<String>> createListWithEffectsOrDamageInfo(
            List<String> keys, List<String> values, String effectsOrDamageName
        ) {

        int indexOfOppEffectParam = keys.indexOf(effectsOrDamageName);
        String[] attackEffects = values.get(indexOfOppEffectParam).split("/");
        List<List<String>> attackEffectsList = new ArrayList<>();

        for (String effectInfo: attackEffects) {
            if (!effectInfo.equals("null")) {
                attackEffectsList.add(Arrays.asList(effectInfo.split(";")));
            }
        }

        return attackEffectsList;
    }

    public static List<Type> getDefaultPokemonTypes(String pokemonClassName) {
        List<List<String>> pokemonData = checkPokemonInFile(pokemonClassName);

        if (pokemonData != null) {
            int indexOfTypeParam = pokemonData.get(0).indexOf(TYPE);
            String pokemonTypes = pokemonData.get(1).get(indexOfTypeParam);
            List<Type> types = new ArrayList<>();

            for (String pokemonType : pokemonTypes.split(";")) {
                types.add(Type.valueOf(pokemonType));
            }

            return types;
        }

        return null;
    }

    public static HashMap<String, Double> getDefaultPokemonStats(String pokemonClassName) {
        List<List<String>> pokemonData = checkPokemonInFile(pokemonClassName);

        if (pokemonData != null) {
            return createMapWithStats(pokemonData.get(0), pokemonData.get(1));
        }

        return null;
    }

    public static List<String> getDefaultPokemonAttacks(String pokemonClassName) {
        List<List<String>> pokemonData = checkPokemonInFile(pokemonClassName);

        if (pokemonData != null) {
            int indexOfAttacksParam = pokemonData.get(0).indexOf(ATTACKS);
            String pokemonAttacks = pokemonData.get(1).get(indexOfAttacksParam);

            if (!pokemonAttacks.equals("null")) {
                return Arrays.asList(pokemonAttacks.split(";"));
            }

            return null;
        }

        return null;
    }

    public static Type getDefaultAttackType(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            int indexOfTypeParam = attackData.get(0).indexOf(TYPE);
            String pokemonType = attackData.get(1).get(indexOfTypeParam);

            return Type.valueOf(pokemonType);
        }

        return null;
    }

    public static HashMap<String, Double> getDefaultAttackStats(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            return createMapWithStats(attackData.get(0), attackData.get(1));
        }

        return null;
    }

    public static List<List<String>> getDefaultAttackOpponentEffects(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            return createListWithEffectsOrDamageInfo(attackData.get(0), attackData.get(1), OPPONENT_EFFECTS);
        }

        return null;
    }

    public static List<List<String>> getDefaultAttackSelfEffects(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            return createListWithEffectsOrDamageInfo(attackData.get(0), attackData.get(1), SELF_EFFECTS);
        }

        return null;
    }

    public static List<String> getDefaultAttackOpponentDamage(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            List<List<String>> opponentDamage = createListWithEffectsOrDamageInfo(
                    attackData.get(0), attackData.get(1), OPPONENT_DAMAGE
            );
            // Can be only one type of damage for pokemon
            return opponentDamage.isEmpty() ? null : opponentDamage.get(0);
        }

        return null;
    }

    public static List<String> getDefaultAttackSelfDamage(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            List<List<String>> selfDamage = createListWithEffectsOrDamageInfo(
                    attackData.get(0), attackData.get(1), SELF_DAMAGE
            );
            // Can be only one type of damage for pokemon
            return selfDamage.isEmpty() ? null : selfDamage.get(0);
        }

        return null;
    }

    public static String getDefaultAttackDescribe(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            int indexOfDescribeParam = attackData.get(0).indexOf(DESCRIBE);

            return attackData.get(1).get(indexOfDescribeParam);
        }

        return null;
    }

    public static String getDefaultAttackExtends(String attackClassName) {
        List<List<String>> attackData = checkAttackInFile(attackClassName);

        if (attackData != null) {
            int indexOfExtendsParam = attackData.get(0).indexOf(EXTENDS);

            return attackData.get(1).get(indexOfExtendsParam);
        }

        return null;
    }
}
