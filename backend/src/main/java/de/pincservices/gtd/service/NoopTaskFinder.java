package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;

import java.util.Collections;

public class NoopTaskFinder extends TaskFinder {

    public NoopTaskFinder() {
        super(null);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        return Collections.emptyList();
    }
}
