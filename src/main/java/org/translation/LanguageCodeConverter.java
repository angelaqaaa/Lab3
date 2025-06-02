package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {

    private final Map<String, String> codeToName;
    private final Map<String, String> nameToCode;

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources' folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {
        codeToName = new HashMap<>();
        nameToCode = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String languageName = parts[0].trim();
                    String languageCode = parts[1].trim();

                    codeToName.put(languageCode, languageName);
                    nameToCode.put(languageName, languageCode);
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the language for the given language code.
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        return codeToName.getOrDefault(code, code);
    }

    /**
     * Returns the code of the language for the given language name.
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        return nameToCode.getOrDefault(language, language);
    }

    /**
     * Returns how many languages are included in this code converter.
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return codeToName.size();
    }
}
