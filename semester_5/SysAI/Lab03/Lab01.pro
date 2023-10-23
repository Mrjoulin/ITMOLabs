% достоинства карт
value(2).
value(3).
value(4).
value(5).
value(6).
value(7).
value(8).
value(9).
value(10).
value(jack).
value(queen).
value(king).
value(ace). 

% масти карт
suit(pikes).
suit(clover).
suit(hearts).
suit(diamonds). 

% Является ли козырной масть
is_trump_suit(S) :- S = hearts. 

% правило формирования карты в колоде
card(Value, Suit) :- value(Value), suit(Suit). 

% достоинства в порядке возрастания
greater(3, 2).
greater(4, 3).
greater(5, 4).
greater(6, 5).
greater(7, 6).
greater(8, 7).
greater(9, 8).
greater(10, 9).
greater(jack, 10).
greater(queen, jack).
greater(king, queen).
greater(ace, king). 

% правило, определяющее бьет ли достоинство X достоинство Y (рекурсивно)
vbeats(X, Y) :- greater(X, Y).
vbeats(X, Y) :- greater(X, Z), vbeats(Z, Y). 

% правило, определяющее бьет ли карта другую карту
beats(card(V1, S1), card(V2, S2)) :- S1 = S2, vbeats(V1, V2).
beats(card(V1, S1), card(V2, S2)) :- is_trump_suit(S1), S1 \= S2.

% правило, определяющее может ли карта перевести ход
reverts(card(V1, S1), card(V2, S2)) :- V1 = V2.

% правило, определяющее успешный ход
successful_move(card(V1, S1), card(V2, S2)) :- 
    beats(card(V1, S1), card(V2, S2)) ; 
    reverts(card(V1, S1), card(V2, S2)).