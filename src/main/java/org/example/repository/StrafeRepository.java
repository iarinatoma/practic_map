package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.model.Strafe;

import java.nio.file.Path;
import java.util.List;

public class StrafeRepository extends JsonRepository<Strafe, Integer> {
    public StrafeRepository(Path filePath) {
        super(filePath, Strafe::getId, (s, id) -> s.setId(id));
    }

    @Override
    protected TypeReference<List<Strafe>> typeRef() {
        return new TypeReference<>() {};
    }
}
