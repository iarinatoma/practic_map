package org.example;

import org.example.controller.RaceController;
import org.example.repository.EventRepository;
import org.example.repository.FahrerRepository;
import org.example.repository.StrafeRepository;
import org.example.service.RaceService;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // ajustează căile la fișiere
        Path driversJson = Path.of("src/main/resources/drivers.json"); // cerința spune să păstrezi ordinea din acest fișier
        Path eventsJson = Path.of("src/main/resources/events.json");
        Path penaltiesJson = Path.of("src/main/resources/penalties.json");

        FahrerRepository fahrerRepo = new FahrerRepository(driversJson);
        EventRepository eventRepo = new EventRepository(eventsJson);
        StrafeRepository strafeRepo = new StrafeRepository(penaltiesJson);

        RaceService service = new RaceService(fahrerRepo, eventRepo, strafeRepo);
        RaceController controller = new RaceController(service);

        Scanner sc = new Scanner(System.in);

        System.out.println("Aufgabe 1");
        controller.printLoadedData();

        System.out.println("Aufgabe 2");
        controller.printActiveByTeam(sc);

        System.out.println("Aufgabe 3 + 4");
        controller.printAndSaveSortedDrivers(Path.of("out/drivers_sorted.txt"));

        System.out.println("Aufgabe 5");
        controller.printFirst5ComputedEvents();

        System.out.println("Aufgabe 6");
        controller.printTop5AndWinningTeam();
    }
}