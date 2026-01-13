package org.example.controller;

import org.example.model.Fahrer;
import org.example.service.RaceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RaceController {

    private final RaceService service;

    public RaceController(RaceService service) {
        this.service = service;
    }

    // Aufgabe 1
    public void printLoadedData() {
        List<Fahrer> drivers = service.getAllDrivers();
        int events = service.getAllEvents().size();
        int penalties = service.getAllPenalties().size();

        System.out.println("Drivers loaded: " + drivers.size());
        System.out.println("Events loaded: " + events);
        System.out.println("Penalties loaded: " + penalties);

        drivers.forEach(d -> System.out.println(d.toOutputLine()));
    }



}