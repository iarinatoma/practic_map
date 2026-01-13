package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.model.RennenEreignis;

import java.nio.file.Path;
import java.util.List;

public class EventRepository extends JsonRepository<RennenEreignis, Integer> {
    public EventRepository(Path filePath) {
        super(filePath, RennenEreignis::getId, (e, id) -> e.setId(id));
    }

    @Override
    protected TypeReference<List<RennenEreignis>> typeRef() {
        return new TypeReference<>() {};
    }
}
