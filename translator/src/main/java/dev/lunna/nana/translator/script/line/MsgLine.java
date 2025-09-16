package dev.lunna.nana.translator.script.line;

import dev.lunna.nana.translator.script.ScriptInstruction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class MsgLine extends ScriptLine {
    private static final Logger log = LoggerFactory.getLogger(MsgLine.class);

    private String text;
    private StringBuilder[] parts = {
            new StringBuilder(), // MSG
            new StringBuilder(), // (id)
            new StringBuilder()  // "text"
    };

    public MsgLine(@NotNull String original, @NotNull final String hashPrefix, int lineNumber) {
        super(original, hashPrefix, lineNumber);

        int i = 0;
        int currPart = 0;
        while (i < original.length()) {
            if (original.charAt(i) == 'M' && original.startsWith("MSG", i)) {
                parts[currPart].append(original, i, i + 3);
                currPart++;
                i += 3;
                continue;
            }

            if (original.charAt(i) == '(') {
                int start = i;
                while (i < original.length() && original.charAt(i) != ')') {
                    i++;
                }
                if (i < original.length()) {
                    i++; // include closing parenthesis
                    parts[currPart].append(original, start, i);
                    currPart++;
                } else {
                    log.warn("Unmatched parenthesis in line: {}", original);
                    parts[currPart].append(original.substring(start));
                    currPart++;
                }
            }

            if (original.charAt(i) == '"') {
                int start = i;
                i++; // skip opening quote
                while (i < original.length() && original.charAt(i) != '"') {
                    if (original.charAt(i) == '\\' && i + 1 < original.length()) {
                        i += 2; // skip escaped character
                    } else {
                        i++;
                    }
                }
                if (i < original.length()) {
                    parts[2].append(original, start, i + 1); // include closing quote
                    text = original.substring(start + 1, i); // exclude quotes for text
                    i++; // move past closing quote
                } else {
                    log.warn("Unmatched quote in line: {}", original);
                    parts[2].append(original.substring(start)); // include rest of line
                    text = original.substring(start + 1); // exclude opening quote for text
                }
                continue;
            }

            parts[currPart].append(original.charAt(i));

            i++;
        }

        if (parts[2] == null) {
            log.warn("Failed to parse MSG line: {}", original);
            text = "";
        }

        text = parts[2].toString();
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public boolean setText(@NotNull String text) {
        this.text = text;
        return true;
    }

    @Override
    public boolean isTranslatable() {
        return true;
    }

    @Override
    public String serialize() {
        return original.replaceFirst(Pattern.quote(this.text), this.text);
    }

    @Override
    public ScriptInstruction getInstruction() {
        return ScriptInstruction.MSG;
    }


}
