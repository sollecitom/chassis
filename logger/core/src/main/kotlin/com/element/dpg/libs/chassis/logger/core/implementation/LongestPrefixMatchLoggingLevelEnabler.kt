package com.element.dpg.libs.chassis.logger.core.implementation

import com.element.dpg.libs.chassis.logger.core.LoggingLevel
import com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie
import com.element.dpg.libs.chassis.logger.core.implementation.datastructure.mutableTrieOf

internal class LongestPrefixMatchLoggingLevelEnabler(private val prefixMap: Map<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel>, private val defaultMinimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel) : (com.element.dpg.libs.chassis.logger.core.LoggingLevel, String) -> Boolean {

    private val trie: com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie = com.element.dpg.libs.chassis.logger.core.implementation.datastructure.mutableTrieOf(prefixMap.keys)

    override fun invoke(level: com.element.dpg.libs.chassis.logger.core.LoggingLevel, loggerName: String): Boolean {

        val longestPrefixMatch = trie.searchLongestPrefixWord(loggerName)
        val minimumLevel = prefixMap[longestPrefixMatch] ?: defaultMinimumLoggingLevel
        return level >= minimumLevel
    }
}