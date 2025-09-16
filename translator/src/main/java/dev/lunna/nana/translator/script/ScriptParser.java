package dev.lunna.nana.translator.script;

import dev.lunna.nana.translator.database.model.TranslationHash;
import dev.lunna.nana.translator.script.line.BasicLine;
import dev.lunna.nana.translator.script.line.MsgLine;
import dev.lunna.nana.translator.script.line.MsgNLine;
import dev.lunna.nana.translator.script.line.ScriptLine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScriptParser {
    private final List<ScriptLine> lines = new ArrayList<>();

    public ScriptParser parse(@NotNull final String original, @NotNull final String hash_prefix) {
        Iterator<String> iterator = original.lines().iterator();
        int lineNumber = 0;

        while (iterator.hasNext()) {
            String line = iterator.next();
            lineNumber++;
            if (line.contains("MSGN")) {
                lines.add(new MsgNLine(line, hash_prefix, lineNumber));
            } else if (line.contains("MSG")) {
                lines.add(new MsgLine(line, hash_prefix, lineNumber));
            } else {
                lines.add(new BasicLine(line, hash_prefix, lineNumber));
            }
        }

        return this;
    }

    public List<ScriptLine> getLines() {
        return lines;
    }

    public List<TranslationHash> getTranslationHashes(String fileName) {
        List<TranslationHash> hashes = new ArrayList<>();
        for (var line : lines) {
            if (line.isTranslatable()) {
                hashes.add(new TranslationHash(fileName, line.getInstruction(), line.hashCode(), line.getText()));
            }
        }
        return hashes;
    }
}
