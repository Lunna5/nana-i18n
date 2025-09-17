package dev.lunna.nana.translator.database.model;

import dev.lunna.nana.translator.job.JobStatus;
import dev.lunna.nana.translator.job.TranslationJobType;
import dev.lunna.nana.translator.tl.Language;
import jakarta.persistence.*;

@Entity
@Table
public class PendingTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translation_hash_id", nullable = false)
    private TranslationHash translationHash;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private JobStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TranslationJobType type;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Language lang;

    public PendingTranslation() {
    }

    public PendingTranslation(TranslationHash translationHash, JobStatus status, TranslationJobType type, Language lang) {
        this.translationHash = translationHash;
        this.status = status;
        this.type = type;
        this.lang = lang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TranslationHash getTranslationHash() {
        return translationHash;
    }

    public void setTranslationHash(TranslationHash translationHash) {
        this.translationHash = translationHash;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public TranslationJobType getType() {
        return type;
    }

    public void setType(TranslationJobType type) {
        this.type = type;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
