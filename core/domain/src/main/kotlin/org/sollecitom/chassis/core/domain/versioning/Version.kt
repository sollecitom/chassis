package org.sollecitom.chassis.core.domain.versioning

interface Version<SELF : Version<SELF>> : Comparable<SELF>