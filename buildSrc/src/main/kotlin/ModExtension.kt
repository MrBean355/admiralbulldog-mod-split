import compile.SINGLE_REPLACE
import org.gradle.api.Action

open class ModExtension {
    val allReplacements: MutableMap<String, String> = mutableMapOf()
    val allFileRenames: MutableMap<String, String> = mutableMapOf()
    val allEmoticons: MutableMap<String, Int> = mutableMapOf()

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

    fun emoticons(action: Action<Emoticons>) {
        Emoticons().also {
            action.execute(it)
            allEmoticons += it.millisPerFrame
        }
    }
}

class Replacements {
    val replacements = mutableMapOf<String, String>()

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

class Emoticons {
    val millisPerFrame = mutableMapOf<String, Int>()

    fun add(name: String, millisPerFrame: Int) {
        this.millisPerFrame += name to millisPerFrame
    }
}