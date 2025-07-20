package util

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun File.hash(): String {
    val messageDigest = MessageDigest.getInstance("SHA-512")
    val result = messageDigest.digest(readBytes())
    val convertedResult = BigInteger(1, result)
    var hashText = convertedResult.toString(16)
    while (hashText.length < 32) {
        hashText = "0$hashText"
    }
    return hashText
}