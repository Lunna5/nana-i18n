package dev.lunna.nana.translator.database.model;

import dev.lunna.nana.translator.script.ScriptInstruction;
import jakarta.persistence.*;

@Entity
@Table(name = "translation_hashes")
public class TranslationHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String file;

    @Enumerated(EnumType.ORDINAL)
    private ScriptInstruction instruction;

    @Column(unique = true, nullable = false)
    private int hash;

    @Column
    private String original;

    public TranslationHash() {
    }

    public TranslationHash(String file, ScriptInstruction instruction, int hash, String original) {
        this.file = file;
        this.instruction = instruction;
        this.hash = hash;
        this.original = original;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScriptInstruction getInstruction() {
        return instruction;
    }

    public void setInstruction(ScriptInstruction instruction) {
        this.instruction = instruction;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
