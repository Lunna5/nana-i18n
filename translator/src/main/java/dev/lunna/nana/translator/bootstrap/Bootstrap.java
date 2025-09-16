package dev.lunna.nana.translator.bootstrap;

import com.google.inject.Guice;
import dev.lunna.nana.translator.Translator;
import dev.lunna.nana.translator.database.HibernateConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
        name = "NANA Ps2 Translator",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "A tool to translate NANA PS2 game text."
)
public final class Bootstrap implements Callable<Integer> {
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    @Parameters(index = "0", description = "The input file or folder to translate.")
    private String input;

    @Option(names = {"-o", "--output"}, description = "The output folder.")
    private String output = "output";

    @Option(names = {"-p", "--prompt"}, description = "The translation prompt to use.")
    private String prompt = "classpath:prompts/jp2en.txt";

    @Option(names = "--populate", description = "Populate the database with existing translations.", defaultValue = "false")
    private boolean populate = false;

    @Override
    public Integer call() throws Exception {
        log.info("Injecting members...");
        TranslatorSettings settings = TranslatorSettings.parse(input, output, prompt, populate);
        final var injector = Guice.createInjector(new TranslatorModule(settings));
        injector.getInstance(HibernateConnection.class).initialize();

        injector.getInstance(Translator.class).run();
        return 0;
    }

    public static void main(String... args) {
        CommandLine commandLine = new CommandLine(new Bootstrap());
        commandLine.setCaseInsensitiveEnumValuesAllowed(true);
        commandLine.setExecutionStrategy(new CommandLine.RunLast());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
