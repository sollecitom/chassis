package org.sollecitom.chassis.messaging.domain

import org.sollecitom.chassis.core.domain.naming.Name

interface Topic {

    val name: Name
    val namespace: Namespace?
    val protocol: Name

    data class Namespace(val tenant: Name, val name: Name)

    data class Partition(val topic: Topic, val partitionIndex: Int)

    companion object
}