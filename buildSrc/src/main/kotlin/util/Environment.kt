package util

import java.io.File

private const val PROPERTY_DOTA_ROOT = "dota.root"
private const val PROPERTY_FULL_COMPILE = "compiler.fullCompile"

object Environment {
    var dotaRoot: File = File("")
        private set
    var fullCompile: Boolean = false
        private set

    fun init(properties: Map<String, *>) {
        val dotaRootProperty = properties[PROPERTY_DOTA_ROOT] as? String ?: ""

        dotaRoot = File(dotaRootProperty)
        fullCompile = (properties[PROPERTY_FULL_COMPILE] as? String)?.toBoolean() ?: true
    }
}