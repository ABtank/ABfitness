package ru.abfitness.oapi.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abfitness.oapi.controllers.mappers.UserMapper;
import ru.abfitness.oapi.dto.users.UserCreationDto;
import ru.abfitness.persist.entities.User;
import ru.abfitness.persist.repositories.RoleRepository;
import ru.abfitness.persist.repositories.UserRepository;
import ru.abfitness.persist.repositories.specifications.UserSpecification;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Primary
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteById(Integer id, Principal principal) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Optional<User> registration(UserCreationDto userCreationDTO) {
        User user = userMapper.userCreationDTOtoUser(userCreationDTO);
        userCreationDTO.getRoles()
                .stream()
                .map(role -> roleRepository.findByName(role).get())
                .forEach(user.getRoles()::add);
        return findById(userRepository.save(user).getId());
    }

    @Override
    public Optional<User> save(User user, Principal principal) {

        return findById(userRepository.save(user).getId());
    }

    @Override
    public Page<User> findAll(Map<String, String> params, PageRequest pageRequest) {
        Specification<User> spec = UserSpecification.trueLiteral();
        if (params.containsKey("email_filter") && !params.get("email_filter").isBlank()) {
            spec = spec.and(UserSpecification.emailContains(params.get("email_filter")));
        }
        return userRepository.findAll(spec, pageRequest);
    }

    @Override
    public List<User> findAll(Specification<User> spec) {
        return userRepository.findAll(spec);
    }

    @Override
    public boolean checkIsUnique(String email, Integer id) {
        Specification<User> spec = UserSpecification.trueLiteral();
        spec = spec.and(UserSpecification.findByEmail(email));
        if (id != null) {
            spec = spec.and(UserSpecification.idNotEqual(id));
        }
        List<User> checkEquals = findAll(spec);
        return checkEquals.isEmpty();
    }

    @Override
    public long count() {
        return userRepository.count();
    }
}
