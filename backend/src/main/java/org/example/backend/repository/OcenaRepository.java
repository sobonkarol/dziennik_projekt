package org.example.backend.repository;

import org.example.backend.model.Ocena;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OcenaRepository extends MongoRepository<Ocena, String> {
    List<Ocena> findByUczenId(String uczenId);
    List<Ocena> findByNauczycielId(String nauczycielId);
}