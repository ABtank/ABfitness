package ru.abfitness.oapi.services;

import ru.abfitness.persist.entities.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BasicService<Role, Integer> {
    List<Role> findAll(Map<String, String> params);
}
