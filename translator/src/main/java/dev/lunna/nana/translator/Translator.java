package dev.lunna.nana.translator;

import dev.lunna.nana.translator.bootstrap.TranslatorSettings;
import dev.lunna.nana.translator.job.PopulateDatabase;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Translator implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Translator.class);

    private final PopulateDatabase populateDatabase;
    private final TranslatorSettings translatorSettings;

    @Inject
    public Translator(@NotNull final PopulateDatabase populateDatabase, @NotNull final TranslatorSettings translatorSettings) {
        this.populateDatabase = populateDatabase;
        this.translatorSettings = translatorSettings;
    }

    @Override
    public void run() {
        if (translatorSettings.pupulate()) {
            log.info("Populating database with original game text...");
            populateDatabase.run();
        }
    }
}