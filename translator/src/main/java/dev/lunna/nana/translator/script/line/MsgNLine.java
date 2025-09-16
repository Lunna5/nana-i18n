package dev.lunna.nana.translator.script.line;

import dev.lunna.nana.translator.script.ScriptInstruction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgNLine extends ScriptLine {
    private static final Logger log = LoggerFactory.getLogger(MsgNLine.class);

    private final StringBuilder[] parts = {
            new StringBuilder(), // MSGN
            new StringBuilder(), // id
            new StringBuilder(), // name
            new StringBuilder(), // text
            new StringBuilder()  // remaining (optional)
    };

    private String id;
    private String name;
    private String text;

    public MsgNLine(@NotNull String original, @NotNull final String hashPrefix, int lineNumber) {
        super(original, hashPrefix, lineNumber);

        int currPart = 0;
        int i = 0;
        int len = original.length();

        while (i < len && currPart < 4) {
            char c = original.charAt(i);

            if (currPart == 0 && original.startsWith("MSGN", i)) {
                parts[currPart].append("MSGN");
                i += 5; // Skip "MSGN" and the following space
                currPart++;
                continue;
            }

            // Now we have to read the id
            // read numbers until we hit a space
            if (currPart == 1) {
                if (Character.isDigit(c)) {
                    parts[currPart].append(c);
                    i++;
                    continue;
                } else if (Character.isWhitespace(c)) {
                    currPart++;
                } else {
                    log.warn("Unexpected character in id part: '{}' in line: {}", c, original);
                }
            }

            // Now we just read all the quoted strings
            if (currPart == 2 || currPart == 3) {
                if (c == '"') {
                    int start = i;
                    i++; // skip opening quote
                    while (i < len) {
                        char currChar = original.charAt(i);
                        if (currChar == '\\' && i + 1 < len) {
                            i += 2; // skip escaped character
                        } else if (currChar == '"') {
                            break; // found closing quote
                        } else {
                            i++;
                        }
                    }
                    if (i < len) {
                        parts[currPart].append(original, start, i + 1); // include closing quote
                        i++; // move past closing quote
                        currPart++;
                        continue;
                    } else {
                        log.warn("Unmatched quote in line: {}", original);
                        parts[currPart].append(original.substring(start));
                        currPart++;
                        break; // exit loop since we reached the end
                    }
                } else if (Character.isWhitespace(c)) {
                    i++; // skip whitespace
                    continue;
                } else {
                    log.warn("Unexpected character in quoted part: '{}' in line: {}", c, original);
                }
            }

            i++;
        }

        this.id = parts[1].toString();
        this.name = parts[2].toString().replaceAll("^\"|\"$", ""); // remove surrounding quotes
        this.text = parts[3].toString().replaceAll("^\"|\"$", ""); // remove surrounding quotes
        if (this.text.isEmpty()) {
            log.warn("Failed to parse MSGN line: {}", original);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean setText(@NotNull String text) {
        this.text = text;
        // actualizar la parte correspondiente
        int startQuote = parts[3].indexOf("\"") + 1;
        int endQuote = parts[3].lastIndexOf("\"");
        if (startQuote >= 0 && endQuote > startQuote) {
            parts[3].replace(startQuote, endQuote, text);
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
        int startQuote = parts[1].indexOf("\"") + 1;
        int endQuote = parts[1].lastIndexOf("\"");
        if (startQuote >= 0 && endQuote > startQuote) {
            parts[1].replace(startQuote, endQuote, id);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
        int startQuote = parts[2].indexOf("\"") + 1;
        int endQuote = parts[2].lastIndexOf("\"");
        if (startQuote >= 0 && endQuote > startQuote) {
            parts[2].replace(startQuote, endQuote, name);
        }
    }

    @Override
    public boolean isTranslatable() {
        return true;
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (StringBuilder part : parts) {
            sb.append(part);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public ScriptInstruction getInstruction() {
        return ScriptInstruction.MSGN;
    }
}
