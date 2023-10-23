from pyswip import Prolog
from functools import cmp_to_key

prolog = Prolog()
prolog.consult("Lab01.pro")


def query(msg: str) -> list:
    return list(prolog.query(msg))


def str_card(card: str) -> str:
    return f'card({card.replace(" ", ",")})'


all_cards = [dict(value=str(card['X']), suit=str(card['Y'])) for card in query("card(X, Y)")]
all_values = [str(val['X']) for val in query("value(X)")]
all_suits = [str(suit['X']) for suit in query("suit(X)")]
all_cards_str = [f"{card['value']} {card['suit']}" for card in all_cards]


def search_in_message(message: str, phrases: list[str]) -> int:
    for text in phrases:
        index = message.find(text)

        if index >= 0:
            return index + len(text)

    return -1


def extract_cards(message: str, start_index: int) -> list[str]:
    search_area = message[start_index:].split('.')[0]

    if ',' in search_area:
        potential_cards = [c.strip() for c in search_area.split(',')]
    elif ';' in search_area:
        potential_cards = [c.strip() for c in search_area.split(';')]
    else:
        return []

    for i in range(len(potential_cards)):
        card = potential_cards[i]

        if card in all_cards_str:
            continue

        sp_card = card.split()

        if len(sp_card) == 2 and sp_card[0] in all_values:
            for suit in all_suits:
                if not sp_card[1].startswith(suit[0]):
                    continue
                potential_cards[i] = f"{sp_card[0]} {suit}"
                break
            else:
                return potential_cards[:i]
        else:
            return potential_cards[:i]

    return potential_cards


def compare(card1: str, card2: str) -> int:
    card1_trump = bool(query(f'is_trump_suit({card1.split()[1]})'))
    card2_trump = bool(query(f'is_trump_suit({card2.split()[1]})'))
    val_compare = bool(query(f'vbeats({card1.split()[0]}, {card2.split()[0]})'))

    if card1_trump ^ card2_trump:
        return int(card1_trump) * 2 - 1

    return int(val_compare) * 2 - 1


def process_input(message: str) -> str:
    message = message.lower()

    my_cards_phrases = ["i have cards:", "my cards:"]
    my_cards_index = search_in_message(message, my_cards_phrases)

    if my_cards_index < 0:
        return "You should tell me card, that you have"

    my_cards = extract_cards(message, my_cards_index)
    my_cards.sort(key=cmp_to_key(compare))

    print("Your cards:", ", ".join(my_cards))

    given_cards_phrases = ["hit me with cards:", "give me cards:", "to beat cards:"]
    given_cards_index = search_in_message(message, given_cards_phrases)

    if given_cards_index < 0:
        return f"You should play with card: {my_cards[0]}"

    given_cards = extract_cards(message, given_cards_index)

    if len(given_cards) > min(len(my_cards), 4):
        return "Too much given cards to beat!"
    if len(set([card.split()[0] for card in given_cards])) > 1:
        return "All given cards should be with the same value!"
    if len(set(given_cards)) < len(given_cards):
        return "Given cards can't duplicate!"

    print("Given cards:", ", ".join(given_cards))

    used_cards = {}
    revert_by_trump = None

    for given_card in given_cards:
        for my_card in my_cards:
            if query(f'reverts({str_card(my_card)}, {str_card(given_card)})'):
                if not query(f'is_trump_suit({my_card.split()[1]})'):
                    return f"You should revert this cards with you card: {my_card}"
                revert_by_trump = my_card

            if query(f'beats({str_card(my_card)}, {str_card(given_card)})'):
                used_cards[given_card] = my_card
                my_cards.remove(my_card)
                break
        else:
            if revert_by_trump:
                return f"You should revert this cards with you trump card: {revert_by_trump}"
            return "You can't beat this cards. You should take them."

    # If we can beat and revert by trump, but in beat we use more trump cards
    if revert_by_trump and sum([bool(query(f'is_trump_suit({card.split()[1]})')) for card in used_cards.values()]) > 1:
        return f"You can beat this cards, but you should revert this cards with you trump card: {revert_by_trump}"

    return "You can beat this cards!\nYou should beat %s" % (
        ", ".join(
            f'{given_card} with {my_card}' for given_card, my_card in used_cards.items()
        )
    )


def main():
    try:
        while True:
            message = input("Input your message: ")

            if message == 'exit':
                return

            answer = process_input(message)
            print(answer)
    except EOFError:
        return


if __name__ == '__main__':
    print("Hello! This is fool game")
    print("You write me what cards you have, what cards you have to beat and i'll tell you!")
    print("To exit app write \"exit\"")

    main()
