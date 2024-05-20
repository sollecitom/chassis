package com.element.dpg.libs.chassis.logger.core.implementation.datastructure

internal interface Trie {

    /**
     * Returns the word in the trie with the longest prefix in common to the given word.
     */
    fun searchLongestPrefixWord(word: String): String

    /**
     * Returns the longest prefix in the trie common to the word.
     */
    fun searchLongestPrefix(word: String): String

    /**
     * Returns if the word is in the trie.
     */
    fun search(word: String): Boolean

    /**
     * Returns if there is any word in the trie that starts with the given prefix.
     */
    fun searchWithPrefix(prefix: String): Boolean

    interface Mutable : com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie {

        /**
         * Inserts a word into the trie.
         */
        fun insert(word: String)
    }
}

internal fun trieOf(vararg words: String): com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie = _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNodeTree().apply { words.forEach { insert(it) } }

internal fun mutableTrieOf(vararg words: String): com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie.Mutable = _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNodeTree().apply { words.forEach { insert(it) } }
internal fun mutableTrieOf(words: Iterable<String>): _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie.Mutable = _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNodeTree().apply { words.forEach { insert(it) } }

private class TrieNodeTree : _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.Trie.Mutable {

    private val root: _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode = _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode()

    override fun insert(word: String) {

        var node: _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode = root
        for (element in word) {
            if (!node.containsKey(element)) {
                node.put(element, _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode())
            }
            node = node[element]!!
        }
        node.setEnd()
    }

    override fun searchLongestPrefixWord(word: String): String {

        var node = root
        val prefixes = mutableListOf<String>()
        val currentPrefix = StringBuilder()
        for (element in word) {
            if (node.containsKey(element)) {
                if (node.isEnd) {
                    prefixes += currentPrefix.toString()
                }
                currentPrefix.append(element)
                node = node[element]!!
            } else {
                if (node.isEnd) {
                    prefixes += currentPrefix.toString()
                }
                return prefixes.maxByOrNull(String::length) ?: ""
            }
        }
        return ""
    }

    override fun searchLongestPrefix(word: String): String {

        var node = root
        val currentPrefix = StringBuilder()
        for (element in word) {
            if (node.containsKey(element) && node.links.size == 1) {
                currentPrefix.append(element)
                node = node[element]!!
            } else {
                return currentPrefix.toString()
            }
        }
        return ""
    }

    override fun search(word: String): Boolean {
        val node = searchPrefix(word)
        return node != null && node.isEnd
    }

    override fun searchWithPrefix(prefix: String): Boolean {
        val node = searchPrefix(prefix)
        return node != null
    }

    private fun searchPrefix(word: String): _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode? {
        var node: _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode? = root
        for (element in word) {
            node = if (node!!.containsKey(element)) {
                node[element]
            } else {
                return null
            }
        }
        return node
    }
}

private class TrieNode private constructor(val links: MutableMap<Char, _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode> = mutableMapOf(), isEnd: Boolean = false) {

    constructor() : this(mutableMapOf(), false)

    var isEnd = isEnd
        private set

    fun containsKey(ch: Char): Boolean = links[ch] != null

    operator fun get(ch: Char): _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode? = links[ch]

    fun put(ch: Char, node: _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode) {
        links[ch] = node
    }

    fun setEnd() {
        isEnd = true
    }

    fun clone(): _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode = _root_ide_package_.com.element.dpg.libs.chassis.logger.core.implementation.datastructure.TrieNode(links.mapValues { it.value.clone() }.toMutableMap(), isEnd)
}