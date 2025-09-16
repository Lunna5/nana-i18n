package dev.lunna.nana.translator.script;

public enum ScriptInstruction {
    MSG, // 0
    MSGN, // 1
    OTHER; // 2

    public static ScriptInstruction fromString(String instruction) {
        return switch (instruction) {
            case "MSG" -> MSG;
            case "MSGN" -> MSGN;
            default -> OTHER;
        };
    }
}
