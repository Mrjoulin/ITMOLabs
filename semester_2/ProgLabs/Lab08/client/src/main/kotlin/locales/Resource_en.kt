package locales

import java.util.*

class Resource_en : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> {
        return arrayOf<Array<Any>>(
            //login
            arrayOf("authorizationLabelMessage", "Login to continue"),
            arrayOf("startRegisteringButtonMessage", "Register"),
            arrayOf("loginButtonMessage", "Login"),
            arrayOf("loginTextFieldMessage", "Username"),
            arrayOf("passwordTextFieldMessage", "Password"),
            //register
            arrayOf("registration.authorizationLabel", "Signing up"),
            arrayOf("registration.loginTextField", "Username"),
            arrayOf("registration.passwordTextField", "Password"),
            arrayOf("registration.startLoginButton", "Back to login"),
            arrayOf("registration.loginButton", "Sign up"),
            //menu
            arrayOf("tablebuttonMessage", "Table"),
            arrayOf("visualizationbuttonMessage", "Visualization"),
            arrayOf("commandsbuttonMessage", "Commands"),
            //table
            arrayOf("tableViewHeaderMessage", "Table of routes in collection"),
            arrayOf("idColumnMessage", "Id"),
            arrayOf("authorColumnMessage", "Author"),
            arrayOf("dateColumnMessage", "Creation Date"),
            arrayOf("nameColumnMessage", "Name"),
            arrayOf("coordinatesColumnMessage", "Coordinates"),
            arrayOf("fromColumnMessage", "From"),
            arrayOf("toColumnMessage", "To"),
            arrayOf("distanceColumnMessage", "Distance"),
            //dialogue window
            arrayOf("dialogueWindow.HeaderMessage", "Route information"),
            arrayOf("dialogueWindow.authorLabelMessage", "Author"),
            arrayOf("dialogueWindow.nameLabelMessage", "Name"),
            arrayOf("dialogueWindow.coordinatesLabelMessage", "Coordinates"),
            arrayOf("dialogueWindow.fromLabelMessage", "From"),
            arrayOf("dialogueWindow.toLabelMessage", "To"),
            arrayOf("dialogueWindow.distanceLabelMessage", "Distance"),
            arrayOf("dialogueWindow.saveButtonMessage", "Save"),
            arrayOf("dialogueWindow.editButtonMessage", "Edit"),
            arrayOf("dialogueWindow.cancelButtonMessage", "Cancel"),
            arrayOf("dialogueWindow.saveButtonMessage", "Save"),
            arrayOf("dialogueWindow.deleteButtonMessage", "Delete"),
            //visualization
            arrayOf("visualizationLabel", "Routes map"),
            //commands
            arrayOf("commands.commandsLabel", "Commands for working with the routes"),
            arrayOf("commands.infoButton", "Information"),
            arrayOf("commands.addButton", "Add route"),
            arrayOf("commands.deleteButton", "Delete all routes"),
            arrayOf("commands.showButton", "Show all routes"),
            arrayOf("commands.helpButton", "Help"),
            arrayOf("commands.executeScriptButton", "Execute script from file"),
            arrayOf("commands.findByNamesButton", "Find routes by name"),
            arrayOf("commands.clearConsoleButton", "Clear console"),
            arrayOf("commands.consoleTextField", "Enter the command"),
            arrayOf("commands.consoleLabel", "Console"),
            arrayOf("commands.fileChooserTitle", "Choose the file with the script"),
            arrayOf("commands.updateMessage", "To update route use \"Table\" or \"Visualization\" section\n"),
            arrayOf("commands.deleteConfirmMessage", "Do you really want to delete all objects?\n"),
            arrayOf("commands.findByNameMessage", "Enter the line that the route names should start with:\n"),
            //profile
            arrayOf("profile.profileLabel", "Profile"),
            arrayOf("profile.languageLabel", "Language"),
            arrayOf("profile.changePasswordLabel", "Change password"),
            arrayOf("profile.saveLanguageButton", "Save"),
            arrayOf("profile.savePasswordButton", "Save"),
            arrayOf("profile.exitButton", "Log out"),
            arrayOf("profile.changePassword", "New password")

        )
    }
}