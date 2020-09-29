package com.javiermendonca.atomicdropsnotifier.core.extensions

fun Long.ended(): Boolean = if (this != 0L) {
    System.currentTimeMillis() / 1000 > this
} else {
    false
}