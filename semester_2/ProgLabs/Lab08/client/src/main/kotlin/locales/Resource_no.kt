package locales

import java.util.*

class Resource_no : ListResourceBundle()  {

    override fun getContents(): Array<Array<Any>> {
        return arrayOf<Array<Any>>(
            arrayOf("authorizationLabelMessage", "Logg på for å fortsette"),
            arrayOf("startRegistrereingButtonMessage", "Registrere"),
            arrayOf("loginButtonMessage", "Logg Inn"),
            arrayOf("loginTextFieldMessage", "Brukernavn"),
            arrayOf("passwordTextFieldMessage", "Passord"),
            arrayOf("registration.authorizationLabel", "Registrere seg"),
            arrayOf("registration.loginTextField", "Brukernavn"),
            arrayOf("registration.passwordTextField", "Passord"),
            arrayOf("registration.startLoginButton", "Tilbake til innlogging"),
            arrayOf("registration.loginButton", "Melde deg på"),
            arrayOf("tablebuttonMessage", "Bord"),
            arrayOf("visualizationbuttonMessage", "Visualisering"),
            arrayOf("commandsbuttonMessage", "Kommandoer"),
            arrayOf("tableViewHeaderMessage", "Tabell over ruter i samling"),
            arrayOf("idColumnMessage", "Id"),
            arrayOf("authorColumnMessage", "Forfatter"),
            arrayOf("dateColumnMessage", "Opprettelsesdato"),
            arrayOf("nameColumnMessage", "Navn"),
            arrayOf("coordinatesColumnMessage", "Koordinater"),
            arrayOf("fromColumnMessage", "Fra"),
            arrayOf("toColumnMessage", "Til"),
            arrayOf("distanceColumnMessage", "Avstand"),
            arrayOf("dialogueWindow.HeaderMessage", "Ruteinformasjon"),
            arrayOf("dialogueWindow.authorLabelMessage", "Forfatter"),
            arrayOf("dialogueWindow.nameLabelMessage", "Navn"),
            arrayOf("dialogueWindow.coordinatesLabelMessage", "Koordinater"),
            arrayOf("dialogueWindow.fromLabelMessage", "Fra"),
            arrayOf("dialogueWindow.toLabelMessage", "Til"),
            arrayOf("dialogueWindow.distanceLabelMessage", "Avstand"),
            arrayOf("dialogueWindow.saveButtonMessage", "Lagre"),
            arrayOf("dialogueWindow.editButtonMessage", "Redigere"),
            arrayOf("dialogueWindow.cancelButtonMessage", "Avbryt"),
            arrayOf("dialogueWindow.saveButtonMessage", "Lagre"),
            arrayOf("dialogueWindow.deleteButtonMessage", "Slett"),
            arrayOf("addWindow.titleLabel", "Legg til ny rute"),
            arrayOf("addWindow.addButtonMessage", "Legge til"),
            arrayOf("addWindow.withoutComparing", "Uten å sammenligne"),
            arrayOf("addWindow.ifMax", "Hvis maks"),
            arrayOf("addWindow.ifMin", "Hvis min"),
            arrayOf("visualizationLabel", "Rutekart"),
            arrayOf("commands.commandsLabel", "Kommandoer for arbeid med rutene"),
            arrayOf("commands.infoButton", "Informasjon"),
            arrayOf("commands.addButton", "Legg til rute"),
            arrayOf("commands.deleteButton", "Slett alle ruter"),
            arrayOf("commands.showButton", "Vis alle ruter"),
            arrayOf("commands.helpButton", "Hjelp"),
            arrayOf("commands.executeScriptButton", "Kjør skript fra fil"),
            arrayOf("commands.findByNamesButton", "Finn ruter etter navn"),
            arrayOf("commands.clearConsoleButton", "Tydelig konsoll"),
            arrayOf("commands.consoleTextField", "Skriv inn kommandoen"),
            arrayOf("commands.consoleLabel", "Konsoll"),
            arrayOf("commands.fileChooserTitle", "Velg filen med skriptet"),
            arrayOf("commands.updateMessage", "For å oppdatere rutebruk \"Table\" or \"Visualization\" section"),
            arrayOf("commands.deleteConfirmMessage", "Vil du virkelig slette alle objekter?"),
            arrayOf("commands.findByNameMessage", "Skriv inn linjen som rutenavnene skal starte med:"),
            arrayOf("profile.profileLabel", "Profil"),
            arrayOf("profile.languageLabel", "Språk"),
            arrayOf("profile.changePasswordLabel", "Bytt passord"),
            arrayOf("profile.saveLanguageButton", "Lagre"),
            arrayOf("profile.savePasswordButton", "Lagre"),
            arrayOf("profile.exitButton", "Logg ut"),
            arrayOf("profile.changePassword", "Nytt passord")
        )
    }
}