package Lab03.utils.abstracts;

import Lab03.characters.abstracts.Character;
import Lab03.utils.properties.CharactersActions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class CharactersMessagesPrint {
    private static final String defaultFormatText = "<name>";

    public static void optimizePrintCharactersMessages(Character[] characters, String[] messages, String formatText) {
        HashMap<String, LinkedList<Character>> optimizedMessages = new HashMap<>();

        for (int i = 0; i < Math.min(characters.length, messages.length); i++) {
            if (messages[i] != null) {
                if (optimizedMessages.containsKey(messages[i])) {
                    // Action has been before
                    optimizedMessages.get(messages[i]).add(characters[i]);
                } else {
                    // Add new action and character to list
                    optimizedMessages.put(messages[i], new LinkedList<>(Arrays.asList(characters[i])));
                }
            }
        }

        for (String message: optimizedMessages.keySet()) {
            LinkedList<Character> messageCharacters = optimizedMessages.get(message);
            boolean manyCharactersInMessage = messageCharacters.size() > 1;

            String text = message.replaceAll(
                    formatText,
                    CharactersNames.getCharactersNames(messageCharacters)
            );

            if (manyCharactersInMessage) {
                for (CharactersActions action: CharactersActions.values()) {
                    if (text.contains(action.getAction(true))) {
                        text = text.replaceAll(
                                action.getAction(true),
                                action.getAction(false)
                        );
                    }
                }
            }

            System.out.println(text);
        }
    }

    public static void optimizePrintCharactersMessages(Character[] characters, String[] messages) {
        optimizePrintCharactersMessages(characters, messages, defaultFormatText);
    }

    public static void optimizePrintCharactersMessages(Character[] characters, List<String> messages) {
        optimizePrintCharactersMessages(characters, messages.toArray(new String[0]), defaultFormatText);
    }
}
