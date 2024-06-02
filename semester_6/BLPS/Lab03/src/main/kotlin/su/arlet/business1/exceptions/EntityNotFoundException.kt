package su.arlet.business1.exceptions

class EntityNotFoundException(message: String?) : Exception(message) {
    constructor() : this(null)
}