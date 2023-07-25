package com.example.pdfreader.util

class Stack<T> : Iterable<T> {
    private val items = mutableListOf<T>()
    private var iteratorIndex = 0
    fun push(item: T) {
        items.add(item)
    }
    fun pop(): T? {
        if (isEmpty()) return null
        return items.removeAt(items.size - 1)
    }
    fun peek(): T? {
        if (isEmpty()) return null
        return items[items.size - 1]
    }
    fun isEmpty(): Boolean {
        return items.isEmpty()
    }
    fun size(): Int {
        return items.size
    }
    fun clear() {
        items.clear()
    }
    fun filter(predicate: (T) -> Boolean): Stack<T> {
        val filteredStack = Stack<T>()
        for (item in items) {
            if (predicate(item)) {
                filteredStack.push(item)
            }
        }
        return filteredStack
    }
    override fun iterator(): MutableIterator<T> {
        iteratorIndex = items.size
        return object : MutableIterator<T> {
            override fun hasNext(): Boolean {
                return iteratorIndex > 0
            }

            override fun next(): T {
                if (!hasNext()) throw NoSuchElementException()
                return items[--iteratorIndex]
            }

            override fun remove() {
                if (iteratorIndex >= items.size || iteratorIndex < 0) {
                    throw IllegalStateException()
                }
                items.removeAt(iteratorIndex)
                iteratorIndex--
            }
        }
    }
}