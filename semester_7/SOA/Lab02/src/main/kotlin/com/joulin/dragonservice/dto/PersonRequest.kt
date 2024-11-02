package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Dragon
import com.joulin.dragonservice.core.Person
import com.joulin.dragonservice.core.enums.Color
import com.joulin.dragonservice.core.enums.Country
import com.joulin.dragonservice.core.enums.DragonCharacter
import com.joulin.dragonservice.exceptions.ValidationException
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

data class PersonRequest(
    val name: String?,
    val weight: Int?,
    val eyeColor: String?,
    val nationality: String?
) {
    fun validatePerson(): Person {
        val nameValue = validateName() ?: throw ValidationException("person name should be not empty")
        val weightValue = validateWeight() ?: throw ValidationException("weight should be positive integer")
        val eyeColorEnum = getEyeColorEnum() ?: throw ValidationException("eyeColor should be not null")
        val nationalityEnum = getNationalityEnum() ?: throw ValidationException("nationality should be not null")

        return Person(
            name = nameValue,
            weight = weightValue,
            eyeColor = eyeColorEnum,
            nationality = nationalityEnum
        )
    }

    fun validateName() : String? {
        return if (name.isNullOrBlank()) null else name
    }

    fun validateWeight(): Int? {
        return if (weight != null && weight > 0) weight else null
    }

    fun getEyeColorEnum(): Color? {
        return if (eyeColor != null) {
            if (!Person.getEnumValues("eyeColor").contains(eyeColor.uppercase()))
                throw ValidationException("eyeColor should be correct enum")
            Color.valueOf(eyeColor.uppercase())
        } else null
    }

    fun getNationalityEnum(): Country? {
        return if (nationality != null) {
            if (!Person.getEnumValues("nationality").contains(nationality.uppercase()))
                throw ValidationException("nationality should be correct enum")
            Country.valueOf(nationality.uppercase())
        } else null
    }
}
