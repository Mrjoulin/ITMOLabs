package com.joulin.dragonservice.core

import com.fasterxml.jackson.annotation.JsonFormat
import com.joulin.dragonservice.core.enums.Color
import com.joulin.dragonservice.core.enums.DragonCharacter
import com.joulin.dragonservice.core.enums.DragonType
import com.joulin.dragonservice.dto.FieldType
import java.time.LocalDateTime
import jakarta.persistence.*
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Table(name = "dragons")
@Getter
@NoArgsConstructor
@AllArgsConstructor
data class Dragon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @NotBlank
    var name: String,
    @Embedded
    @AttributeOverrides(*[
        AttributeOverride( name = "x", column = Column(name = "coordinates_x")),
        AttributeOverride( name = "y", column = Column(name = "coordinates_y"))
    ])
    var coordinates: Coordinates,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val creationDate: LocalDateTime,
    @Positive
    var age: Int,
    @Enumerated(EnumType.STRING)
    var color: Color,
    @Enumerated(EnumType.STRING)
    var type: DragonType,
    @Enumerated(EnumType.STRING)
    var character: DragonCharacter?,
    @Embedded
    @AttributeOverrides(*[
        AttributeOverride( name = "name", column = Column(name = "killer_name")),
        AttributeOverride( name = "weight", column = Column(name = "killer_weight")),
        AttributeOverride( name = "eyeColor", column = Column(name = "killer_eye_color")),
        AttributeOverride( name = "nationality", column = Column(name = "killer_nationality"))
    ])
    var killer: Person?,

    @ManyToOne
    @JoinColumn(name = "cave_id")
    var cave: Cave?,
) {
    companion object {
        private val basicFields = mapOf(
            "id" to FieldType.INTEGER,
            "name" to FieldType.STRING,
            "creationDate" to FieldType.DATE,
            "age" to FieldType.INTEGER,
            "color" to FieldType.ENUM,
            "type" to FieldType.ENUM,
            "character" to FieldType.ENUM,
            "caveId" to FieldType.INTEGER
        )

        fun getFields(): Map<String, FieldType> {
            val fields = basicFields.toMutableMap()
            Coordinates.getFields().forEach { fields["coordinates.${it.key}"] = it.value }
            Person.getFields().forEach { fields["killer.${it.key}"] = it.value }
            return fields
        }

        fun getEnumValues(fieldName: String): List<String> {
            if (fieldName.startsWith("killer."))
                return Person.getEnumValues(fieldName.removePrefix("killer."))
            if (fieldName.startsWith("coordinates."))
                return Coordinates.getEnumValues(fieldName.removePrefix("coordinates."))

            return when(fieldName) {
                "color" -> Color.values().map { it.name }
                "type" -> DragonType.values().map { it.name }
                "character" -> DragonCharacter.values().map { it.name }
                else -> listOf()
            }
        }

        fun getIntegerFieldPath(root: Path<Dragon>, fieldName: String): Path<Long>? {
            if (fieldName.startsWith("coordinates."))
                return Coordinates.getIntegerFieldPath(root.get("coordinates"), fieldName.removePrefix("coordinates."))
            if (fieldName.startsWith("killer."))
                return Person.getIntegerFieldPath(root.get("killer"), fieldName.removePrefix("killer."))
            if (fieldName == "caveId")
                return root.get<Cave>("cave").get("id")

            return when(fieldName) {
                "id" -> root.get("id")
                "age" -> root.get("age")
                else -> null
            }
        }

        fun getNumberFieldPath(root: Path<Dragon>, fieldName: String): Path<Double>? {
            if (fieldName.startsWith("coordinates."))
                return Coordinates.getNumberFieldPath(root.get("coordinates"), fieldName.removePrefix("coordinates."))
            if (fieldName.startsWith("killer."))
                return Person.getNumberFieldPath(root.get("killer"), fieldName.removePrefix("killer."))
            return null
        }

        fun getDateFieldPath(root: Path<Dragon>, fieldName: String): Path<LocalDateTime>? {
            if (fieldName.startsWith("coordinates."))
                return Coordinates.getDateFieldPath(root.get("coordinates"), fieldName.removePrefix("coordinates."))
            if (fieldName.startsWith("killer."))
                return Person.getDateFieldPath(root.get("killer"), fieldName.removePrefix("killer."))

            return when(fieldName) {
                "creationDate" -> root.get("creationDate")
                else -> null
            }
        }

        fun getStringFieldPath(root: Path<Dragon>, fieldName: String): Path<String>? {
            if (fieldName.startsWith("coordinates."))
                return Coordinates.getStringFieldPath(root.get("coordinates"), fieldName.removePrefix("coordinates."))
            if (fieldName.startsWith("killer."))
                return Person.getStringFieldPath(root.get("killer"), fieldName.removePrefix("killer."))

            return when(fieldName) {
                "name" ->  root.get("name")
                "color" ->  root.get("color")
                "type" -> root.get("type")
                "character" -> root.get("character")
                else -> null
            }
        }
    }

    fun getFieldNumberValueByName(fieldName: String): Double? {
        if (fieldName.startsWith("coordinates."))
            return coordinates.getFieldNumberValueByName(fieldName.removePrefix("coordinates."))
        if (fieldName.startsWith("killer."))
            return killer?.getFieldNumberValueByName(fieldName.removePrefix("killer."))

        return when(fieldName) {
            "id" -> id.toDouble()
            "age" -> age.toDouble()
            "caveId" -> cave?.id?.toDouble()
            else -> null
        }
    }

    fun getFieldStringValueByName(fieldName: String): String? {
        if (fieldName.startsWith("coordinates."))
            return coordinates.getFieldStringValueByName(fieldName.removePrefix("coordinates."))
        if (fieldName.startsWith("killer."))
            return killer?.getFieldStringValueByName(fieldName.removePrefix("killer."))

        return when(fieldName) {
            "name" -> name
            "color" -> color.name
            "type" -> type.name
            "character" -> character?.name
            else -> null
        }
    }

    fun getFieldDateValueByName(fieldName: String): LocalDateTime? {
        if (fieldName.startsWith("coordinates."))
            return coordinates.getFieldDateValueByName(fieldName.removePrefix("coordinates."))
        if (fieldName.startsWith("killer."))
            return killer?.getFieldDateValueByName(fieldName.removePrefix("killer."))

        return when(fieldName) {
            "creationDate" -> creationDate
            else -> null
        }
    }
}