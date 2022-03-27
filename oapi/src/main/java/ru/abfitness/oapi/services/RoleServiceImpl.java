package ru.abfitness.oapi.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.abfitness.persist.repositories.RoleRepository;
import ru.abfitness.persist.entities.Role;
import ru.abfitness.persist.repositories.RoleRepository;
import ru.abfitness.persist.repositories.specifications.RoleSpecification;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }


    @Override
    public Optional<Role> save(Role o, Principal principal) {
        return Optional.of(roleRepository.save(o));
    }

    @Override
    public void deleteById(Integer id, Principal principal) {
        roleRepository.deleteById(id);
    }

    @Override
    public long count() {
        return roleRepository.count();
    }

    @Override
    public List<Role> findAll(Map<String, String> params) {
        Specification<Role> spec = RoleSpecification.trueLiteral();
        if (!params.get("name").isBlank()) {
            spec = spec.and(RoleSpecification.nameContains(params.get("name")));
        }
        if (!params.get("descr").isBlank()) {
            spec = spec.and(RoleSpecification.descriptionContains(params.get("descr")));
        }
        return roleRepository.findAll(spec);
    }
}
