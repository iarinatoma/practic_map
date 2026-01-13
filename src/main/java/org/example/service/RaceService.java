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

    // Aufgabe 3
    public List<Fahrer> sortDrivers(List<Fahrer> drivers) {
        return drivers.stream()
                .sorted(Comparator
                        .comparingInt(Fahrer::getSkillLevel).reversed()
                        .thenComparing(Fahrer::getName))
                .collect(Collectors.toList());
    }

    // Aufgabe 5
    public int computePoints(RennenEreignis e) {
        int bp = e.getBasePoints();
        int lap = e.getLap();

        return switch (e.getTyp()) {
            case OVERTAKE -> bp + 1;
            case FASTEST_LAP -> bp + 2 * (lap % 3);
            case TRACK_LIMITS -> bp - 5;
            case COLLISION -> bp - 10 - lap;
            case PIT_STOP -> (lap <= 10) ? (bp + 2) : bp;
        };
    }

    public List<String> first5EventLines() {
        return eventRepo.findAll().stream()
                .limit(5)
                .map(e -> "Event " + e.getId() + " -> raw=" + e.getBasePoints() +
                        " -> computed=" + computePoints(e))
                .collect(Collectors.toList());
    }

    // Aufgabe 6
    public static record DriverScore(Fahrer driver, int totalScore) {}

    public List<DriverScore> top5Ranking() {
        List<RennenEreignis> events = eventRepo.findAll();
        List<Strafe> penalties = strafeRepo.findAll();
        List<Fahrer> drivers = fahrerRepo.findAll();

        Map<Integer, Integer> eventSum = new HashMap<>();
        for (RennenEreignis e : events) {
            eventSum.merge(e.getFahrerId(), computePoints(e), Integer::sum);
        }

        Map<Integer, Integer> penaltySum = new HashMap<>();
        for (Strafe s : penalties) {
            penaltySum.merge(s.getFahrerId(), s.getSeconds(), Integer::sum);
        }

        List<DriverScore> scored = new ArrayList<>();
        for (Fahrer d : drivers) {
            int total = eventSum.getOrDefault(d.getId(), 0) - penaltySum.getOrDefault(d.getId(), 0);
            scored.add(new DriverScore(d, total));
        }

        return scored.stream()
                .sorted((a, b) -> {
                    int cmp = Integer.compare(b.totalScore(), a.totalScore()); // desc
                    if (cmp != 0) return cmp;
                    return a.driver().getName().compareTo(b.driver().getName()); // asc
                })
                .limit(5)
                .toList();
    }

    public String winningTeamFromRanking(List<DriverScore> top5) {
        if (top5.isEmpty()) return "N/A";
        return top5.get(0).driver().getTeam();
    }
}