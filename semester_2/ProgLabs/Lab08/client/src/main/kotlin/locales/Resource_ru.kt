package locales

import java.util.*

class Resource_ru : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> {
        return arrayOf<Array<Any>>(
            //login
            arrayOf("authorizationLabelMessage", "Войдите, чтобы продолжить"),
            arrayOf("startRegisteringButtonMessage", "Зарегистрироваться"),
            arrayOf("loginButtonMessage", "Войти"),
            arrayOf("loginTextFieldMessage", "Логин"),
            arrayOf("passwordTextFieldMessage", "Пароль"),
            //register
            arrayOf("registration.authorizationLabel", "Регистрация"),
            arrayOf("registration.loginTextField", "Логин"),
            arrayOf("registration.passwordTextField", "Пароль"),
            arrayOf("registration.startLoginButton", "Войти"),
            arrayOf("registration.loginButton", "Зарегистрироваться"),
            //menu
            arrayOf("tablebuttonMessage", "Таблица"),
            arrayOf("visualizationbuttonMessage", "Визуализация"),
            arrayOf("commandsbuttonMessage", "Команды"),
            //table
            arrayOf("tableViewHeaderMessage", "Таблица маршрутов в коллекции"),
            arrayOf("idColumnMessage", "Id"),
            arrayOf("authorColumnMessage", "Автор"),
            arrayOf("dateColumnMessage", "Дата создания"),
            arrayOf("nameColumnMessage", "Имя"),
            arrayOf("coordinatesColumnMessage", "Координаты"),
            arrayOf("fromColumnMessage", "Откуда"),
            arrayOf("toColumnMessage", "Куда"),
            arrayOf("distanceColumnMessage", "Дистанция"),
            //dialogue window
            arrayOf("dialogueWindow.HeaderMessage", "Информация о маршруте"),
            arrayOf("dialogueWindow.authorLabelMessage", "Автор"),
            arrayOf("dialogueWindow.nameLabelMessage", "Имя"),
            arrayOf("dialogueWindow.coordinatesLabelMessage", "Координаты"),
            arrayOf("dialogueWindow.fromLabelMessage", "Откуда"),
            arrayOf("dialogueWindow.toLabelMessage", "Куда"),
            arrayOf("dialogueWindow.distanceLabelMessage", "Дистанция"),
            arrayOf("dialogueWindow.saveButtonMessage", "Сохранить"),
            arrayOf("dialogueWindow.editButtonMessage", "Редактировать"),
            arrayOf("dialogueWindow.cancelButtonMessage", "Отмена"),
            arrayOf("dialogueWindow.saveButtonMessage", "Сохранить"),
            arrayOf("dialogueWindow.deleteButtonMessage", "Удалить"),
            //add window
            arrayOf("addWindow.titleLabel", "Добавление нового маршрута"),
            arrayOf("addWindow.addButtonMessage", "Добавить"),
            arrayOf("addWindow.withoutComparing", "Без сравнения"),
            arrayOf("addWindow.ifMax", "Если больше всех"),
            arrayOf("addWindow.ifMin", "Если меньше всех"),
            //visualization
            arrayOf("visualizationLabel", "Карта маршрутов"),
            //commands
            arrayOf("commands.commandsLabel", "Команды для работы с маршрутами"),
            arrayOf("commands.infoButton", "Информация"),
            arrayOf("commands.addButton", "Добавить маршрут"),
            arrayOf("commands.deleteButton", "Удалить все маршруты"),
            arrayOf("commands.showButton", "Вывести все маршруты"),
            arrayOf("commands.helpButton", "Помощь"),
            arrayOf("commands.executeScriptButton", "Выполнить скрипт из файла"),
            arrayOf("commands.findByNamesButton", "Найти маршрут по имени"),
            arrayOf("commands.clearConsoleButton", "Очистить консоль"),
            arrayOf("commands.consoleTextField", "Введите команду"),
            arrayOf("commands.consoleLabel", "Консоль"),
            arrayOf("commands.fileChooserTitle", "Выбирите файл со скриптом"),
            arrayOf("commands.updateMessage", "Чтобы обновить маршрут используйте секцию \"Таблица\" или \"Визуализация\"\n"),
            arrayOf("commands.deleteConfirmMessage", "Вы действительно хотите удалить все маршруты?\n"),
            arrayOf("commands.findByNameMessage", "Введите строку, с которой должно начинаться имя маршрута:\n"),
            //profile
            arrayOf("profile.profileLabel", "Профиль"),
            arrayOf("profile.languageLabel", "Язык"),
            arrayOf("profile.changePasswordLabel", "Изменить пароль"),
            arrayOf("profile.saveLanguageButton", "Сохранить"),
            arrayOf("profile.savePasswordButton", "Сохранить"),
            arrayOf("profile.exitButton", "Выйти"),
            arrayOf("profile.changePassword", "Новый пароль")

        )
    }
}