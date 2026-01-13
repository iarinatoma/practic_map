package org.example.service;

import org.example.model.*;
import org.example.repository.EventRepository;
import org.example.repository.FahrerRepository;
import org.example.repository.StrafeRepository;

import java.util.*;
import java.util.stream.Collectors;

public class RaceService {

    private final FahrerRepository fahrerRepo;
    private final EventRepository eventRepo;
    private final StrafeRepository strafeRepo;

    public RaceService(FahrerRepository fahrerRepo, EventRepository eventRepo, StrafeRepository strafeRepo) {
        this.fahrerRepo = fahrerRepo;
        this.eventRepo = eventRepo;
        this.strafeRepo = strafeRepo;
    }

    // Aufgabe 1
    public List<Fahrer> getAllDrivers() { return fahrerRepo.findAll(); }
    public List<RennenEreignis> getAllEvents() { return eventRepo.findAll(); }
    public List<Strafe> getAllPenalties() { return strafeRepo.findAll(); }

    // Aufgabe 2
    public List<Fahrer> filterActiveByTeam(String team) {
        return fahrerRepo.findAll().stream()
                .filter(d -> d.getStatus() == FahrerStatus.ACTIVE)
                .filter(d -> d.getTeam() != null && d.getTeam().equals(team))
                .collect(Collectors.toList());
    }

}