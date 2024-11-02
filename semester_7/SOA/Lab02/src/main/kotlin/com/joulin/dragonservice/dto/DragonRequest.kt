package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.*
import com.joulin.dragonservice.core.enums.Color
import com.joulin.dragonservice.core.enums.DragonCharacter
import com.joulin.dragonservice.core.enums.DragonType
import com.joulin.dragonservice.exceptions.ValidationException
import java.time.LocalDateTime

data class DragonRequest(
    val name: String? = null,
    val coordinates: CoordinatesRequest? = null,
    val age: Int? = null,
    val color: String? = null,
    val type: String? = null,
    val character: String? = null,
    val killer: PersonRequest? = null,
    val caveId: Long? = null
) {
    private var cave: Cave? = null
    fun validateDragon(): Dragon {
        val nameValue = validateName() ?: throw ValidationException("name should be not empty")
        val ageValue = validateAge() ?: throw ValidationException("age should be positive integer")

        // Check enums
        val colorEnum = getColorEnum() ?: throw ValidationException("color should be not null")
        val typeEnum = getTypeEnum() ?: throw ValidationException("type should be not null")
        val characterEnum = getCharacterEnum()

        // Validate coordinates
        val coordinatesObj = coordinates?.validateCoordinates() ?: throw ValidationException("coordinates should be not null")

        // Validate killer
        val killerObj = killer?.validatePerson()

        return Dragon(
            name=nameValue,
            coordinates = coordinatesObj,
            creationDate = LocalDateTime.now(),
            age = ageValue,
            color = colorEnum,
            type = typeEnum,
            character = characterEnum,
            killer = killerObj,
            cave = cave
        )
    }

    fun validateName(): String? {
        return if (name.isNullOrBlank()) null else name
    }

    fun validateAge(): Int? {
        return if (age != null && age > 0) age else null
    }

    fun getColorEnum() : Color? {
        return if (color != null) {
            if (!Dragon.getEnumValues("color").contains(color.uppercase()))
                throw ValidationException("color should be correct enum")
            Color.valueOf(color.uppercase())
        } else null
    }

    fun getTypeEnum(): DragonType? {
        return if (type != null) {
            if (!Dragon.getEnumValues("type").contains(type.uppercase()))
                throw ValidationException("type should be correct enum")
            DragonType.valueOf(type.uppercase())
        } else null
    }

    fun getCharacterEnum(): DragonCharacter? {
        return if (character != null) {
            if (!Dragon.getEnumValues("character").contains(character.uppercase()))
                throw ValidationException("character should be correct enum")
            DragonCharacter.valueOf(character.uppercase())
        } else null
    }

    fun setCave(cave: Cave?) {
        this.cave = cave
    }
}