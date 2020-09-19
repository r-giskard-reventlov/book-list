package uk.co.foundationsedge.infrastructure;

import java.util.Set;

public class WorksSet {
    private final Set<String> workIds;

    public WorksSet(Set<String> workIds) {
        this.workIds = workIds;
    }

    public Set<String> getWorkIds() {
        return workIds;
    }
}
