package locales

import java.util.*

class Resource_hu : ListResourceBundle()  {

    override fun getContents(): Array<Array<Any>> {
        return arrayOf<Array<Any>>(
            arrayOf("authorizationLabelMessage", "A folytatáshoz jelentkezzen be"),
            arrayOf("startRegisztrációingButtonMessage", "Regisztráció"),
            arrayOf("loginButtonMessage", "Belépés"),
            arrayOf("loginTextFieldMessage", "Felhasználónév"),
            arrayOf("passwordTextFieldMessage", "Jelszó"),
            arrayOf("registration.authorizationLabel", "Feliratkozás"),
            arrayOf("registration.loginTextField", "Felhasználónév"),
            arrayOf("registration.passwordTextField", "Jelszó"),
            arrayOf("registration.startLoginButton", "Vissza a bejelentkezéshez"),
            arrayOf("registration.loginButton", "Regisztrálj"),
            arrayOf("tablebuttonMessage", "asztal"),
            arrayOf("visualizationbuttonMessage", "Megjelenítés"),
            arrayOf("commandsbuttonMessage", "Parancsok"),
            arrayOf("tableViewHeaderMessage", "A gyűjteményben lévő útvonalak táblázata"),
            arrayOf("idColumnMessage", "Id"),
            arrayOf("authorColumnMessage", "Szerző"),
            arrayOf("dateColumnMessage", "Létrehozás dátuma"),
            arrayOf("nameColumnMessage", "Név"),
            arrayOf("coordinatesColumnMessage", "Koordináták"),
            arrayOf("fromColumnMessage", "Tól től"),
            arrayOf("toColumnMessage", "Nak nek"),
            arrayOf("distanceColumnMessage", "Távolság"),
            arrayOf("dialogueWindow.HeaderMessage", "Útvonal információk"),
            arrayOf("dialogueWindow.authorLabelMessage", "Szerző"),
            arrayOf("dialogueWindow.nameLabelMessage", "Név"),
            arrayOf("dialogueWindow.coordinatesLabelMessage", "Koordináták"),
            arrayOf("dialogueWindow.fromLabelMessage", "Tól től"),
            arrayOf("dialogueWindow.toLabelMessage", "Nak nek"),
            arrayOf("dialogueWindow.distanceLabelMessage", "Távolság"),
            arrayOf("dialogueWindow.saveButtonMessage", "Megment"),
            arrayOf("dialogueWindow.editButtonMessage", "Szerkesztés"),
            arrayOf("dialogueWindow.cancelButtonMessage", "Megszünteti"),
            arrayOf("dialogueWindow.saveButtonMessage", "Megment"),
            arrayOf("dialogueWindow.deleteButtonMessage", "Töröl"),
            arrayOf("addWindow.titleLabel", "Új útvonal hozzáadása"),
            arrayOf("addWindow.addButtonMessage", "Hozzáadás"),
            arrayOf("addWindow.withoutComparing", "Összehasonlítás nélkül"),
            arrayOf("addWindow.ifMax", "Ha max"),
            arrayOf("addWindow.ifMin", "Ha min"),
            arrayOf("visualizationLabel", "Útvonalak térképe"),
            arrayOf("commands.commandsLabel", "Parancsok az útvonalakkal való munkához"),
            arrayOf("commands.infoButton", "Információ"),
            arrayOf("commands.addButton", "Útvonal hozzáadása"),
            arrayOf("commands.deleteButton", "Az összes útvonal törlése"),
            arrayOf("commands.showButton", "Az összes útvonal megjelenítése"),
            arrayOf("commands.helpButton", "Segítség"),
            arrayOf("commands.executeScriptButton", "Szkript végrehajtása fájlból"),
            arrayOf("commands.findByNamesButton", "Útvonalak keresése név szerint"),
            arrayOf("commands.clearConsoleButton", "Tiszta konzol"),
            arrayOf("commands.consoleTextField", "Írja be a parancsot"),
            arrayOf("commands.consoleLabel", "Konzol"),
            arrayOf("commands.fileChooserTitle", "Válassza ki a szkriptet tartalmazó fájlt"),
            arrayOf(
                "commands.updateMessage",
                "Az útvonalhasználat frissítéséhez \"Táblázat\" or \"Vizualizáció\" section"
            ),
            arrayOf("commands.deleteConfirmMessage", "Biztosan törölni szeretné az összes objektumot?"),
            arrayOf("commands.findByNameMessage", "Írja be azt a sort, amellyel az útvonalneveknek kezdődniük kell:"),
            arrayOf("profile.profileLabel", "Profil"),
            arrayOf("profile.languageLabel", "Nyelv"),
            arrayOf("profile.changePasswordLabel", "Jelszó módosítása"),
            arrayOf("profile.saveLanguageButton", "Megment"),
            arrayOf("profile.savePasswordButton", "Megment"),
            arrayOf("profile.exitButton", "Kijelentkezés"),
            arrayOf("profile.changePassword", "Új jelszó")
        )


    }
}