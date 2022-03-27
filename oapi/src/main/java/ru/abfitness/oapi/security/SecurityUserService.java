package ru.abfitness.oapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abfitness.oapi.dto.users.ChangePasswordDto;
import ru.abfitness.oapi.dto.users.RegistrationRequestDto;
import ru.abfitness.oapi.exceptions.MatchPasswordException;
import ru.abfitness.persist.entities.Role;
import ru.abfitness.persist.entities.User;
import ru.abfitness.persist.repositories.RoleRepository;
import ru.abfitness.persist.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityUserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository repository) {
        this.userRepository = repository;
    }

    @Autowired
    public void setUserRepository(RoleRepository repository) {
        this.roleRepository = repository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Неверные имя пользователя или пароль"));
        Set<Role> roles = user.getRoles();
        return new CustomUserDetails(user, roles);
    }

    public User getUserByCredentials(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User registerUser(RegistrationRequestDto dtoUser) {
        User user = new User(dtoUser.getEmail(), passwordEncoder.encode(dtoUser.getPassword()));
        user.setLastName(dtoUser.getLastName());
        user.setFirstName(dtoUser.getFirstName());
        user.setMiddleName(dtoUser.getMiddleName());
        user.setPhone(dtoUser.getPhone());
        Role mainUserRole = roleRepository.findByName(dtoUser.getUserRole()).orElse(null);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(mainUserRole);
        user.setRoles(roleSet);
        userRepository.save(user);

        return user;
    }

    public boolean changePassword(String email, ChangePasswordDto passwordDto) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new MatchPasswordException("Пароль пользователя не совпадает");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        userRepository.save(user);
        return true;
    }
}
