package org.sollecitom.chassis.logger.core.implementation.datastructure

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

    interface Mutable : Trie {

        /**
         * Inserts a word into the trie.
         */
        fun insert(word: String)
    }
}

internal fun trieOf(vararg words: String): Trie = TrieNodeTree().apply { words.forEach { insert(it) } }

internal fun mutableTrieOf(vararg words: String): Trie.Mutable = TrieNodeTree().apply { words.forEach { insert(it) } }
internal fun mutableTrieOf(words: Iterable<String>): Trie.Mutable = TrieNodeTree().apply { words.forEach { insert(it) } }

private class TrieNodeTree : Trie.Mutable {

    private val root: TrieNode = TrieNode()

    override fun insert(word: String) {

        var node: TrieNode = root
        for (element in word) {
            if (!node.containsKey(element)) {
                node.put(element, TrieNode())
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

    private fun searchPrefix(word: String): TrieNode? {
        var node: TrieNode? = root
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

private class TrieNode private constructor(val links: MutableMap<Char, TrieNode> = mutableMapOf(), isEnd: Boolean = false) {

    constructor() : this(mutableMapOf(), false)

    var isEnd = isEnd
        private set

    fun containsKey(ch: Char): Boolean = links[ch] != null

    operator fun get(ch: Char): TrieNode? = links[ch]

    fun put(ch: Char, node: TrieNode) {
        links[ch] = node
    }

    fun setEnd() {
        isEnd = true
    }

    fun clone(): TrieNode = TrieNode(links.mapValues { it.value.clone() }.toMutableMap(), isEnd)
}