package entities.validators.exceptions

import org.jetbrains.annotations.NotNull

class IncorrectFieldDataException(@NotNull message: String) : Exception(message)