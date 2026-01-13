package org.example.controller;

import org.example.repository.JsonRepository;

import java.nio.file.Path;

/**
 * Controller generic pentru acțiuni comune de aplicație.
 * Aici punem export JSON generic (folosește mapper-ul din repository).
 *
 * Motiv: păstrăm I/O în controller, service rămâne pur.
 */
public class AppController {

    private final JsonRepository<?, ?> anyRepo;

    public AppController(JsonRepository<?, ?> anyRepo) {
        this.anyRepo = anyRepo;
    }

    public void exportAsJson(Path outPath, Object anyObject) {
        anyRepo.writeObjectAsJson(outPath, anyObject);
    }
}
