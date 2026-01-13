package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.model.Fahrer;

import java.nio.file.Path;
import java.util.List;

public class FahrerRepository extends JsonRepository<Fahrer, Integer> {
    public FahrerRepository(Path filePath) {
        super(filePath, Fahrer::getId, (f, id) -> f.setId(id));
    }

    @Override
    protected TypeReference<List<Fahrer>> typeRef() {
        return new TypeReference<>() {};
    }
}
