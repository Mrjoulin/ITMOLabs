package Lab02;

import Lab02.pokemons.names.*;
import ru.ifmo.se.pokemon.Battle;

// Create UML

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Zapdos zapdos = new Zapdos("Лох позорный", 1);
        Stunky stunky = new Stunky("Прыщавый ублюдок", 1);
        Starly starly = new Starly("Мамкоебщик", 1);
        Staravia staravia = new Staravia("Лоликонщик отбитый", 1);
        Staraptor staraptor = new Staraptor("ХАЙП", 1);
        Skuntank skuntank = new Skuntank("Эмо Кид", 1);

        // Add pokemons to Ally
        b.addAlly(zapdos);
        b.addAlly(starly);
        b.addAlly(staraptor);
        // Add pokemons to Foe
        b.addFoe(stunky);
        b.addFoe(staravia);
        b.addFoe(skuntank);
        // Run battle
        b.go();
    }
}
