package com.joulin.killerservice.exceptions

import com.joulin.killerservice.dto.ServiceFaultDto


class ServiceFaultException : RuntimeException {
    var serviceFault: ServiceFaultDto

    constructor(serviceFault: ServiceFaultDto) : super(serviceFault.description) {
        this.serviceFault = serviceFault
    }

    constructor(message: String?, serviceFault: ServiceFaultDto) : super(message) {
        this.serviceFault = serviceFault
    }

    constructor(message: String?, e: Throwable?, serviceFault: ServiceFaultDto) : super(message, e) {
        this.serviceFault = serviceFault
    }
}