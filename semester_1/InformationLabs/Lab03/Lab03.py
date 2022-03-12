import re

# Set Russian vowels
VOWELS = "АЕЁИОУЫЭЮЯаеёиоуыэюя"
# Get Russian consonants (go throw alphabet and if character not in vowels - it's consonant)
CONSONANTS = "".join(map(
    lambda cur: chr(cur), filter(lambda c: chr(c) not in VOWELS, range(ord(VOWELS[0]), ord(VOWELS[-1])))
))


def task_one(text: str) -> int:
    # Variant 0-1-1 :<)

    re_smile = r"(\s|^):<\)"

    return len(re.findall(re_smile, text)) if isinstance(text, str) else 0


def task_two(text: str) -> str:
    # Variant 5

    # Check if text is incorrect
    if not isinstance(text, str) or not text:
        return ""

    # Remove special characters

    special_chars = "!,./:;'\"')(-\n"
    for char in special_chars:
        text = text.replace(char, " ")

    # Split text and remove empty strings

    split_text = list(filter(lambda x: x, text.split()))

    # Create regular expressions for word with two vowels and counting consonants

    re_two_vowels = re.compile("[а-яА-Я]*([%s]|^)([%s]){2}([%s]|$)[а-яА-Я]*" % (CONSONANTS, VOWELS, CONSONANTS))
    re_consonants = re.compile("^([%s]*[%s]?){3}[%s]*$" % (VOWELS, CONSONANTS, VOWELS))

    # Find all matched words

    result = []

    for i in range(len(split_text) - 1):
        if re_two_vowels.match(split_text[i]) and re_consonants.match(split_text[i + 1]):
            result.append(split_text[i])

    # Return founded words, each on new line

    return "\n".join(result)


def task_three(text: str) -> str:
    # Variant 1

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

    re_only_one_unique_vowel = re.compile(r"^[%s]*([%s])([%s]|(\1))*$" % (CONSONANTS, VOWELS, CONSONANTS))

    # Find all matched words

    result = filter(lambda word: re_only_one_unique_vowel.match(word), split_text)

    # Sort founded words by they length and return each on new line

    return "\n".join(sorted(result, key=lambda cur: len(cur)))
