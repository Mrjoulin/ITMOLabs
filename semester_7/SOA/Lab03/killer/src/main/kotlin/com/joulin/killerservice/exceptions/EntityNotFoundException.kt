package com.joulin.killerservice.exceptions

class EntityNotFoundException(message: String?) : Exception(message) {
    constructor() : this(null)
}