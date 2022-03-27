package ru.abfitness.oapi.controllers.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.abfitness.oapi.dto.users.UserCreationDto;
import ru.abfitness.oapi.dto.users.UserDto;
import ru.abfitness.persist.entities.Role;
import ru.abfitness.persist.entities.User;
import ru.abfitness.persist.repositories.RoleRepository;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMapper {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User dtoToEntity(UserDto userDTO, User user) {
        user = userDtoToUser(userDTO, user);
        if(!userDTO.getRoles().isEmpty()){
            userDTO.getRoles()
                    .stream()
                    .map(role -> roleRepository.findByName(role).get())
                    .forEach(user.getRoles()::add);
        }
        return user;
    }

    public UserDto entityToDto(User dto){
        return userToDto(dto);
    }

    public UserDto userToDto(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(toList()),
                user.getRoles()
                        .stream()
                        .map(Role::getDescription)
                        .collect(toList()),
                user.getDtCreate(),
                user.getDtModify());
    }

    public User userDtoToUser(UserDto userDto, User user) {
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        return user;
    }

    public User userCreationDTOtoUser(UserCreationDto userCreationDTO) {
        return new User(
                userCreationDTO.getEmail(),
                passwordEncoder.encode(userCreationDTO.getPassword())
        );
    }
}
