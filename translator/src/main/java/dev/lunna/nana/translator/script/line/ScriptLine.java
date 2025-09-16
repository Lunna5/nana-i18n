package dev.lunna.nana.translator.script.line;

import dev.lunna.nana.translator.script.ScriptInstruction;
import org.jetbrains.annotations.NotNull;

public abstract class ScriptLine {
    protected String original;
    private final String hash_prefix;
    private final int line;

    public ScriptLine(@NotNull final String original, @NotNull final String hash_prefix, final int line) {
        this.original = original;
        this.hash_prefix = hash_prefix;
        this.line = line;
    }

    public ScriptLine(@NotNull final String original) {
        this.original = original;
        this.hash_prefix = "foo_";
        this.line = 0;
    }

    public abstract String getText();

    public abstract boolean setText(@NotNull final String text);

    public abstract boolean isTranslatable();

    public abstract String serialize();

    public abstract ScriptInstruction getInstruction();

    @Override
    public int hashCode() {
        return hash().hashCode();
    }

    public int getLineNumber() {
        return line;
    }

    public String hash() {
        return hash_prefix + ":" + line;
    }
}
