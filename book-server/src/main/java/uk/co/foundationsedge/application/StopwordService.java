package uk.co.foundationsedge.application;

import java.util.Set;

public interface StopwordService {
    Set<String> removeFrom(Set<String> keywords);
}
