package Lab04;

/*
Отрывок рассказа:

Как и ожидали Фуксия и Селедочка, невесомость возникла, как только кристалл и магнит были сближены на достаточное
расстояние. Коротышки, присутствовавшие при этом опыте, в тот же момент отделились от дна пещеры и поднялись кверху.
Плавая под потолком пещеры в самых разнообразных позах, они всячески старались спуститься вниз, но попытки их были
малоуспешны. Находясь в громоздких скафандрах, они не могли точно рассчитать свои телодвижения и использовать
реактивные силы для перемещения в пространстве.
Общее недоумение вызвал тот факт, что сам Знайка, а также профессор Звездочкин в силу каких-то причин не подверглись
действию невесомости и как ни в чем не бывало продолжали работать внизу. Они переносили прибор невесомости с места
на место, отходили от него в дальние уголки пещеры, проверяя при помощи пружинных весов изменение силы тяжести в
разных местах. Все спрашивали Знайку и Звездочкина, почему на них не действует невесомость, но Знайка и Звездочкин
только посмеивались втихомолку и делали вид, что не слышат вопросов. Натешившись вдоволь, они признались, что нашли
антилунит, который и позволяет им сохранить вес. Выключив прибор невесомости, в результате чего все коротышки
моментально опустились вниз, Знайка вытряхнул из своего рюкзака несколько мелких камней. Все с интересом принялись
разглядывать их. Камни были твердые, плотные, по виду напоминавшие кремень, но в отличие от кремня они были не
темно-серого, а яркого фиолетового цвета и к тому же обладали какой-то энергией, в силу которой притягивались
друг к другу, подобно тому, как притягиваются наэлектризованные предметы или кусочки намагниченного железа.
Знайка сказал, что им стоило большого труда отколоть эти камешки от огромнейшей глыбы, найденной в глубине пещеры,
так как антилунит чрезвычайно твердое вещество.

*/


import Lab03.characters.MainCharacter;
import Lab03.characters.MinorCharacter;
import Lab03.characters.abstracts.Character;
import Lab03.places.Cave;
import Lab03.things.Antilunite;
import Lab03.things.Material;
import Lab03.things.SpecialMaterial;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.Attraction;
import Lab03.things.properties.Color;
import Lab03.things.properties.Hardness;
import Lab03.things.properties.UniqueAbility;
import Lab04.characters.PrequelMainCharacter;
import Lab04.characters.PrequelMinorCharacter;
import Lab04.places.WorkWithPrequel;
import Lab04.places.exceptions.UnexpectedExperimentResult;
import Lab04.things.abstracts.AbstractEquipment;
import Lab04.things.properties.Size;
import Lab04.things.properties.Weight;
import Lab04.utils.abstracts.CharactersMessagesPrint;

import java.util.ArrayList;
import java.util.List;

public class MainStory {
    public static void main(String[] args) {
        // Create places
        Cave cave = new Cave();
        cave.fullCave();

        WorkWithPrequel work;

        // Create prequel characters
        PrequelMainCharacter fuksia = new PrequelMainCharacter("Фуксия");
        PrequelMainCharacter seledochka = new PrequelMainCharacter("Селёдочка");

        PrequelMinorCharacter korotishki = new PrequelMinorCharacter("Коротышки", false);

        // Create arrays with main and minor prequel characters
        PrequelMainCharacter[] mainPrequelCharacters = {seledochka, fuksia};
        PrequelMinorCharacter[] minorPrequelCharacters = {korotishki};

        // Add some equipment to minor characters
        AbstractEquipment equipment = new AbstractEquipment("скафандре", Size.BIG, Weight.HEAVY){
            @Override
            public String toString() {
                return "громоздком скафандре";
            }
        };

        korotishki.addEquipment(equipment);

        // Add characters to Work

        work = new WorkWithPrequel(mainPrequelCharacters, minorPrequelCharacters);

        // Create materials for prequel

        SpecialMaterial lunit = new SpecialMaterial(
                "Лунит", Color.WHITE, Hardness.HIGH, Attraction.ORDINARY, UniqueAbility.CHANGE_GRAVITY
        );

        SpecialMaterial magnet = new SpecialMaterial(
                "Магнит", Color.WHITE, Hardness.LOW, Attraction.STRONG, null
        );

        //
        // ----- Start prequel ------
        //

        // Main characters expectation of interaction

        List<String> messages = new ArrayList<>();

        for (PrequelMainCharacter mainCharacter: mainPrequelCharacters) {
            messages.add(
                mainCharacter.expectationFromMaterialsInteraction(lunit, magnet)
            );
        }

        CharactersMessagesPrint.optimizePrintCharactersMessages(mainPrequelCharacters, messages);

        // Do an experiment on the work and make a conclusion
        boolean noUnexpectedResults = true;

        try {
            work.makeAnExperiment(lunit, magnet);

            work.charactersExperimentConclusions(mainPrequelCharacters, fuksia.getExpectations());
        } catch (UnexpectedExperimentResult e) {
            System.out.println(e.getMessage());

            noUnexpectedResults = false;
        }

        // Fuksia and Seledochka don't participate in further story
        work.removeMainCharacters(fuksia, seledochka);

        if (noUnexpectedResults) {
            work.consequencesOfTheExperiment();

            // Minor characters try to get down
            messages = new ArrayList<>();
            List<PrequelMinorCharacter> staidMinorCharacters = new ArrayList<>();

            for (PrequelMinorCharacter minorCharacter: minorPrequelCharacters) {
                messages.add(minorCharacter.tryToGetDown());
                if (!minorCharacter.isSuccessfulTryToGetDown()) staidMinorCharacters.add(minorCharacter);
            }

            CharactersMessagesPrint.optimizePrintCharactersMessages(minorPrequelCharacters, messages);

            // All characters who can't get down, try to use reactive forces
            messages = new ArrayList<>();

            for (PrequelMinorCharacter staidMinorCharacter: staidMinorCharacters)
                messages.add(staidMinorCharacter.useReactiveForcesToGetDown());

            CharactersMessagesPrint.optimizePrintCharactersMessages(
                    staidMinorCharacters.toArray(new Character[0]), messages
            );
        }

        // Korotishki in further story will be others ("Все")
        work.removeMinorCharacters(korotishki);

        //
        // ------ End of the prequel ------
        //

        // Create main story characters
        MainCharacter znayka = new MainCharacter("Знайка");
        MainCharacter zvezdochkin = new MainCharacter("Звёздочкин");

        MinorCharacter others = new MinorCharacter("Все", false);

        // Create arrays with main and minor characters
        MainCharacter[] mainCharacters = {zvezdochkin, znayka};
        MinorCharacter[] minorCharacters = {others};

        // Add materials to characters
        others.addMaterialsToBackpack(
                new Material("Кремень", Color.GRAY, Hardness.HIGH, Attraction.NO),
                new Material("Кусочки намагниченного железа", Color.WHITE, Hardness.MEDIUM, Attraction.ORDINARY)
        );

        // Add characters to the work
        work.addMainCharacters(mainCharacters);
        work.addMinorCharacters(minorCharacters);

        //
        // ------ Start main story ------
        //

        // Part 1

        // Main characters explore cave (without messages here, character will tell about the find at the end)
        work.exploreCave(cave, mainCharacters);

        work.startWorking();

        if (others.isMisunderstanding()) {
            messages = new ArrayList<>();

            for (MainCharacter mainCharacter: mainCharacters) {
                String message = mainCharacter.decideToTellOtherCharacters(work.getAllCharacters());
                messages.add(message);
            }

            CharactersMessagesPrint.optimizePrintCharactersMessages(mainCharacters, messages);

            others.setMisunderstanding(false);
        }

        work.stopWorking();

        // Part 2

        Antilunite znaykasAntilunite = znayka.popAntiluniteWithMessage();
        others.seeNewMaterial(znaykasAntilunite);

        if (others.isInterested()) {
            // Start compare with another materials
            for (AbstractMaterial material: others.getBackpack())
                znaykasAntilunite.compare(material);
        }

        znayka.tellCharactersAboutHowTheyFoundMaterial(work.getAllCharacters(), znaykasAntilunite);

        //
        // ------ End of the main story ------
        //
    }
}
