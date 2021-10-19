import re


def task_one(text: str) -> int:
    # Variant 0-1-1 :<)

    re_smile = r"(\s|^):<\)"

    return len(re.findall(re_smile, text)) if isinstance(text, str) else 0


def task_two(text: str) -> str:
    # Variant 5
    # Set Russian vowels
    vowels = "АЕЁИОУЫЭЮЯаеёиоуыэюя"
    # Set Russian consonants (go throw alphabet and if character not in vowels - it's consonant)
    consonants = "".join(map(
        lambda cur: chr(cur), filter(lambda c: chr(c) not in vowels, range(ord(vowels[0]), ord(vowels[-1])))
    ))

    # Check if text is incorrect
    if not isinstance(text, str) or not text:
        return ""

    # Remove special characters

    special_chars = "!,./:;'\")(-\n"
    for char in special_chars:
        text = text.replace(char, " ")

    # Split text and remove empty strings

    split_text = list(filter(lambda x: x, text.split()))

    # Create regular expressions for word with two vowels and counting consonants

    re_two_vowels = re.compile("[а-яА-Я]*([%s]|^)([%s]{2})([%s]|$)[а-яА-Я]*" % (consonants, vowels, consonants))
    re_consonants = re.compile("([%s])" % consonants)

    # Find all matched words

    result = []

    for i in range(len(split_text) - 1):
        if re_two_vowels.match(split_text[i]) and len(re_consonants.findall(split_text[i + 1])) <= 3:
            result.append(split_text[i])

    # Return founded words, each on new line

    return "\n".join(result)


def task_three(text: str) -> str:
    # Variant 1
    # Set Russian vowels
    vowels = "АЕЁИОУЫЭЮЯаеёиоуыэюя"

    # Check if text is incorrect
    if not isinstance(text, str) or not text:
        return ""

    # Remove special characters

    special_chars = "!,./:;'\")(-\n"
    for char in special_chars:
        text = text.replace(char, " ")

    # Split text and remove empty strings

    split_text = list(filter(lambda x: x, text.split()))

    # Regular expressions to find all vowels in word

    re_one_vowel_in_word = re.compile("[%s]" % vowels)

    # Find all matched words

    result = []

    for word in split_text:
        # Find all vowels, remove duplicate values and check, that in word only one unique vowel
        if len(set(re_one_vowel_in_word.findall(word))) == 1:
            result.append(word)

    # Sort founded words by they length and return each on new line

    return "\n".join(sorted(result, key=lambda cur: len(cur)))
