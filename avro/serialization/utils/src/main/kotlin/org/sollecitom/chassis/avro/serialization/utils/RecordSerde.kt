package org.sollecitom.chassis.avro.serialization.utils

interface RecordSerde<VALUE> : RecordSerializer<VALUE>, RecordDeserializer<VALUE>