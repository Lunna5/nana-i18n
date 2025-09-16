package dev.lunna.nana.translator.script.line;

import dev.lunna.nana.translator.script.ScriptInstruction;
import org.jetbrains.annotations.NotNull;

public final class BasicLine extends ScriptLine {
    public BasicLine(@NotNull String original, @NotNull final String hashPrefix, int lineNumber) {
        super(original, hashPrefix, lineNumber);
    }

    @Override
    public String getText() {
        return super.original;
    }

    @Override
    public boolean setText(@NotNull String text) {
        return false;
    }

    @Override
    public boolean isTranslatable() {
        return false;
    }

    @Override
    public String serialize() {
        return super.original;
    }

    @Override
    public ScriptInstruction getInstruction() {
        return ScriptInstruction.OTHER;
    }
}
