package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Repo generic simplu:
 * - load la start
 * - CRUD pe cache (data)
 * - save după add/update/delete
 *
 * Fără "auto-int-id" aici (o faci în service).
 */
public abstract class JsonRepository<T, ID> {

    protected final ObjectMapper mapper = new ObjectMapper();

    private final Path filePath;
    private final Function<T, ID> idGetter;
    private final BiConsumer<T, ID> idSetter; // poate fi (e, id) -> {}

    protected final List<T> data = new ArrayList<>();

    protected JsonRepository(Path filePath,
                             Function<T, ID> idGetter,
                             BiConsumer<T, ID> idSetter) {
        this.filePath = filePath;
        this.idGetter = idGetter;
        this.idSetter = idSetter;
        load();
    }

    protected abstract TypeReference<List<T>> typeRef();

    protected final void load() {
        try {
            if (!Files.exists(filePath)) {
                if (filePath.getParent() != null) Files.createDirectories(filePath.getParent());
                save(); // scriem []
                return;
            }

            byte[] bytes = Files.readAllBytes(filePath);
            if (bytes.length == 0) {
                save();
                return;
            }

            List<T> loaded = mapper.readValue(bytes, typeRef());
            data.clear();
            data.addAll(loaded);

        } catch (Exception e) {
            throw new RuntimeException("Eroare la citirea JSON: " + filePath, e);
        }
    }

    public void writeObjectAsJson(Path out, Object obj) {
        try {
            Files.write(out,
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(obj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final void save() {
        try {
            byte[] out = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(data);
            Files.write(filePath, out);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la scrierea JSON: " + filePath, e);
        }
    }

    // --- CRUD ---

    public List<T> findAll() {
        return new ArrayList<>(data);
    }

    public Optional<T> findById(ID id) {
        return data.stream()
                .filter(e -> Objects.equals(idGetter.apply(e), id))
                .findFirst();
    }

    public void add(T entity) {
        ID id = idGetter.apply(entity);
        if (id == null) throw new IllegalArgumentException("Entity id is null.");
        if (findById(id).isPresent()) throw new IllegalArgumentException("Duplicate id: " + id);

        data.add(entity);
        save();
    }

    public void update(T entity) {
        ID id = idGetter.apply(entity);
        if (id == null) throw new IllegalArgumentException("Entity id is null.");

        for (int i = 0; i < data.size(); i++) {
            if (Objects.equals(idGetter.apply(data.get(i)), id)) {
                data.set(i, entity);
                save();
                return;
            }
        }
        throw new NoSuchElementException("No entity with id " + id);
    }

    public void deleteById(ID id) {
        boolean removed = data.removeIf(e -> Objects.equals(idGetter.apply(e), id));
        if (!removed) throw new NoSuchElementException("No entity with id " + id);
        save();
    }

    public void setId(T entity, ID id) {
        idSetter.accept(entity, id);
    }

}
