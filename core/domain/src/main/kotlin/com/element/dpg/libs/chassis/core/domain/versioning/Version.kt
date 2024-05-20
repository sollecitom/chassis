package com.element.dpg.libs.chassis.core.domain.versioning

interface Version<SELF : Version<SELF>> : Comparable<SELF>