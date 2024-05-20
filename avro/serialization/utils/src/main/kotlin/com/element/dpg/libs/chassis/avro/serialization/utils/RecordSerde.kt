package com.element.dpg.libs.chassis.avro.serialization.utils

interface RecordSerde<VALUE> : RecordSerializer<VALUE>, RecordDeserializer<VALUE>