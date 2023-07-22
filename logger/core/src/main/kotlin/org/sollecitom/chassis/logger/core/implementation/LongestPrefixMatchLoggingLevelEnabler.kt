package org.sollecitom.chassis.logger.core.implementation

import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.implementation.datastructure.Trie
import org.sollecitom.chassis.logger.core.implementation.datastructure.mutableTrieOf

internal class LongestPrefixMatchLoggingLevelEnabler(private val prefixMap: Map<String, LoggingLevel>, private val defaultMinimumLoggingLevel: LoggingLevel) : (LoggingLevel, String) -> Boolean {

    private val trie: Trie = mutableTrieOf(prefixMap.keys)

    override fun invoke(level: LoggingLevel, loggerName: String): Boolean {

        val longestPrefixMatch = trie.searchLongestPrefixWord(loggerName)
        val minimumLevel = prefixMap[longestPrefixMatch] ?: defaultMinimumLoggingLevel
        return level >= minimumLevel
    }
}