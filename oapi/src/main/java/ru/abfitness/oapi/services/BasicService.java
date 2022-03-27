package ru.abfitness.oapi.services;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface BasicService<E, K> {
    List<E> findAll();
    Optional<E> findById(K id);
    void deleteById(K id, Principal principal);
    Optional<E> save(E o,Principal principal);
    long count();
}
