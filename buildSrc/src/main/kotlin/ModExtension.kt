import compile.EXACT_REPLACE
import compile.SINGLE_REPLACE
import org.gradle.api.Action

open class ModExtension {
    val allReplacements: MutableMap<String, String> = mutableMapOf()
    val allFileRenames: MutableMap<String, String> = mutableMapOf()

    var modName: String = ""
    var modDescription: String = ""

    fun stringReplacements(action: Action<Replacements>) {
        Replacements().also {
            action.execute(it)
            allReplacements += it.replacements
        }
    }

    fun fileRenames(action: Action<Renames>) {
        Renames().also {
            action.execute(it)
            allFileRenames += it.renames
        }
    }
}

class Replacements {
    val replacements = mutableMapOf<String, String>()

    fun exact(match: String, replacement: String) {
        replacements["$EXACT_REPLACE$match"] = replacement
    }

    fun id(id: String, replacement: String) {
        replacements["$SINGLE_REPLACE$id"] = replacement
    }

    fun any(match: String, replacement: String) {
        replacements[match] = replacement
    }
}

class Renames {
    val renames = mutableMapOf<String, String>()

    fun add(from: String, to: String) {
        renames += from to to
    }
}