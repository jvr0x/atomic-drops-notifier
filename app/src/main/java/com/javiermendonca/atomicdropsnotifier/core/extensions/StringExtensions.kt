package com.javiermendonca.atomicdropsnotifier.core.extensions

private val IPFS_REGEX = Regex("^Qm[1-9A-HJ-NP-Za-km-z]{44}.*")

fun String.isIPFS() = IPFS_REGEX.matches(this)