package dev.lunna.nana.translator.bootstrap;

import com.google.inject.AbstractModule;
import dev.lunna.nana.translator.database.HibernateConnection;
import org.jetbrains.annotations.NotNull;

public class TranslatorModule extends AbstractModule {
    private final TranslatorSettings translatorSettings;

    public TranslatorModule(
            @NotNull final TranslatorSettings translatorSettings
    ) {
        this.translatorSettings = translatorSettings;
    }

    @Override
    protected void configure() {
        bind(HibernateConnection.class).asEagerSingleton();
        bind(TranslatorSettings.class).toInstance(translatorSettings);
    }
}
