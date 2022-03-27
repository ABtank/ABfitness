package ru.abfitness.oapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.abfitness.oapi.config.JwtProvider;
import ru.abfitness.oapi.constants.SwaggerConstant;
import ru.abfitness.oapi.controllers.mappers.UserMapper;
import ru.abfitness.oapi.dto.users.*;
import ru.abfitness.oapi.dto.transfer.Update;
import ru.abfitness.oapi.exceptions.NotFoundException;
import ru.abfitness.oapi.security.SecurityUserService;
import ru.abfitness.oapi.services.UserService;
import ru.abfitness.persist.entities.User;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = {SwaggerConstant.API_AUTH})
public class AuthController {
    private SecurityUserService securityUserService;
    private UserService userService;
    private JwtProvider jwtProvider;
    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService service) {
        this.securityUserService = service;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @ApiOperation(value = "Авторизоваться.", notes = "Отправить email и пароль для авторизации (для получения Bearer token).", response = LoginRequestDto.class)
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) {
        User user = securityUserService.getUserByCredentials(requestDto.getEmail(), requestDto.getPassword());
        if (user == null) {
            throw new UsernameNotFoundException("Неверные логин или пароль");
        }
        return new LoginResponseDto(jwtProvider.createToken(user.getEmail()));
    }

    @ApiOperation(value = "Кто я.", notes = "Информация авторизовавшегося пользователя", response = UserDto.class)
    @GetMapping("/whoami")
    public ResponseEntity<UserDto> login(@ApiIgnore Principal principal) {
        return (principal != null) ?
                new ResponseEntity<>
                        (userService
                                .findByEmail(principal.getName())
                                .map(userMapper::userToDto)
                                .orElseThrow(NotFoundException::new), HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Зарегистрировать нового Пользователя.", notes = "Зарегистрировать нового Пользователя (учитель, студент).", response = UserDto.class)
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Новый Пользователь была зарегистрирован."),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере."),
            @ApiResponse(responseCode = "400", description = "Запрос неверный."),
            @ApiResponse(responseCode = "404", description = "Адрес URL не найден."),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы. Авторизуйтесь и повторите еще раз."),
            @ApiResponse(responseCode = "401", description = "У вас не достаточно прав доступа."),
    })

    @PostMapping
    public UserDto create(@Validated(Update.class) @RequestBody UserCreationDto userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getMatchingPassword())) {
            throw new IllegalArgumentException("password not matching");
        }
        return userService.registration(userDTO)
                .map(user -> userMapper.entityToDto(user))
                .orElseThrow(NotFoundException::new);
    }

    @ApiOperation(value = "Зарегистрировать нового Пользователя с ФИО и 1 ролью.", notes = "Зарегистрировать нового Пользователя (учитель, студент).")
    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody RegistrationRequestDto requestDto) {
        User user = securityUserService.registerUser(requestDto);
        return new ResponseEntity<>(userMapper.userToDto(user),HttpStatus.OK);
    }

    @ApiOperation(value = "Сменить пароль.", notes = "Сменить со старого пароля на новый.", response = Boolean.class)
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Пароль изменён."),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере."),
            @ApiResponse(responseCode = "400", description = "Запрос неверный."),
            @ApiResponse(responseCode = "404", description = "Адрес URL не найден."),
            @ApiResponse(responseCode = "403", description = "У вас не достаточно прав доступа."),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы. Авторизуйтесь и повторите еще раз."),
    })
    @PutMapping("/change_password")
    public Boolean create(@Validated(Update.class) @RequestBody ChangePasswordDto passwordDto, @ApiIgnore Principal principal) {
        return securityUserService.changePassword(principal.getName(), passwordDto);
    }
}
