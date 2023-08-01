package org.sollecitom.chassis.logger.core.implementation

import java.lang.reflect.Modifier
import kotlin.reflect.KClass

internal object LoggingNameResolver {

    internal fun name(loggable: Any): String = name(loggable::class)
    internal fun <T : Any> name(forClass: KClass<T>): String = name(forClass.java)
    private fun <T : Any> name(forClass: Class<T>): String = unwrapCompanionClass(forClass).name

    private fun <T : Any> unwrapCompanionClass(clazz: Class<T>): Class<*> {

        if (clazz.enclosingClass != null) {
            try {
                val field = clazz.enclosingClass.getDeclaredField(clazz.simpleName)
                if (Modifier.isStatic(field.modifiers) && field.type == clazz) {
                    // && field.get(null) === obj
                    // the above might be safer but problematic with initialization order
                    return clazz.enclosingClass
                }
            } catch (e: Exception) {
                //ok, it is not a companion object
            }
        }
        return clazz
    }
}