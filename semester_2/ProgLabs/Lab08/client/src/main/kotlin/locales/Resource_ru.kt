package locales

import java.util.*

class Resource_ru : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> {
        return arrayOf<Array<Any>>(
            arrayOf("authorizationLabelMessage", "Войдите, для доступа к приложению"),
            arrayOf("startRegisteringButtonMessage", "Зарегистрироваться"),
            arrayOf("loginButtonMessage", "Войти")

        )
    }
}