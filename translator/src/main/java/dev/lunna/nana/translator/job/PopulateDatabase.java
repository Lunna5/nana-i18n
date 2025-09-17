package dev.lunna.nana.translator.job;

import dev.lunna.nana.translator.bootstrap.TranslatorSettings;
import dev.lunna.nana.translator.database.model.PendingTranslation;
import dev.lunna.nana.translator.database.model.TranslationHash;
import dev.lunna.nana.translator.database.service.PendingTranslationService;
import dev.lunna.nana.translator.database.service.TranslationHashService;
import dev.lunna.nana.translator.script.ScriptParser;
import dev.lunna.nana.translator.tl.Language;
import dev.lunna.nana.translator.util.FileUtil;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PopulateDatabase implements Runnable {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PopulateDatabase.class);

    private final TranslatorSettings translatorSettings;
    private final TranslationHashService hashService;
    private final PendingTranslationService pendingTranslationService;

    @Inject
    public PopulateDatabase(
            @NotNull final TranslatorSettings translatorSettings,
            @NotNull final TranslationHashService hashService,
            @NotNull final PendingTranslationService pendingTranslationService
    ) {
        this.translatorSettings = translatorSettings;
        this.hashService = hashService;
        this.pendingTranslationService = pendingTranslationService;
    }

    @Override
    public void run() {
        for (var file : translatorSettings.inputFiles()) {
            log.info("Translating file: {}", file);
            var parser = new ScriptParser()
                    .parse(FileUtil.readFileToString(file, FileUtil.SHIFT_JIS), file.getName());

            List<TranslationHash> hashes = parser.getTranslationHashes(file.getName());

            hashService.batchSave(hashes);

            List<PendingTranslation> jobs = new ArrayList<>();

            for (var hash : hashes) {
                var job = new PendingTranslation(hash, JobStatus.PENDING, TranslationJobType.TRANSLATE, Language.EN);
                jobs.add(job);
            }

            pendingTranslationService.batchSave(jobs);
        }
    }
}
