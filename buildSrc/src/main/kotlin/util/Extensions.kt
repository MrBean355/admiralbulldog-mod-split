/*
 * Copyright 2022 Michael Johnston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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