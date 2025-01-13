package com.joulin.dragonservice.exceptions

class EntityNotFoundException(message: String?) : Exception(message) {
    constructor() : this(null)
}