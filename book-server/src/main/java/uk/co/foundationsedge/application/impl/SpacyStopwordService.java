package uk.co.foundationsedge.application.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.co.foundationsedge.application.StopwordService;

import java.nio.file.Files;
import java.util.Set;

@Component
public class SpacyStopwordService implements StopwordService {

    private final Set<String> stopWords;

    public SpacyStopwordService(@Value("classpath:spacystopwords.txt") Resource stopWordsFile) {
        try {
            this.stopWords = Set.copyOf(Files.readAllLines(stopWordsFile.getFile().toPath()));
        } catch (Exception e) {
            throw new RuntimeException("failed to load stopword file", e);
        }
    }

    public Set<String> removeFrom(Set<String> keywords) {
        keywords.removeAll(stopWords); // yuck
        return keywords;
    }
}
