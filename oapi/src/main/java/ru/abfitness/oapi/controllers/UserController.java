package ru.abfitness.oapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.abfitness.oapi.constants.SwaggerConstant;
import ru.abfitness.oapi.controllers.mappers.UserMapper;
import ru.abfitness.oapi.dto.users.UserDto;
import ru.abfitness.oapi.dto.transfer.Update;
import ru.abfitness.oapi.exceptions.NotFoundException;
import ru.abfitness.oapi.services.UserService;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequestMapping("/api/v1/users")
@Api(tags = {SwaggerConstant.API_USER})
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @ApiOperation(value = "Получить список Пользователей", notes = "Получить список всех зарегистрировавшихся Пользователей.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Данные получены."),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере."),
            @ApiResponse(responseCode = "400", description = "Запрос неверный."),
            @ApiResponse(responseCode = "404", description = "Адрес URL не найден."),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы. Авторизуйтесь и повторите еще раз."),
            @ApiResponse(responseCode = "401", description = "У вас не достаточно прав доступа."),
    })
    public List<UserDto> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(userMapper::entityToDto)
                .collect(toList());
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Найти Пользователя по id.", notes = "Пользователь.", response = UserDto.class)
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Пользователь найден."),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере."),
            @ApiResponse(responseCode = "400", description = "Запрос неверный."),
            @ApiResponse(responseCode = "404", description = "Адрес URL не найден."),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы. Авторизуйтесь и повторите еще раз."),
            @ApiResponse(responseCode = "401", description = "У вас не достаточно прав доступа."),
    })
    public UserDto findById(@PathVariable("id") Integer id) {
        return userService.findById(id)
                .map(userMapper::entityToDto)
                .orElseThrow(NotFoundException::new);
    }

    @PutMapping
    @ApiOperation(value = "Обновить данные Пользователя.", notes = "Обновить основные данные Пользователя.", response = UserDto.class)
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Данные обновлены."),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере."),
            @ApiResponse(responseCode = "400", description = "Запрос неверный."),
            @ApiResponse(responseCode = "404", description = "Адрес URL не найден."),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы. Авторизуйтесь и повторите еще раз."),
            @ApiResponse(responseCode = "401", description = "У вас не достаточно прав доступа."),
    })
    public UserDto updateUser(
            @Validated(Update.class) @RequestBody UserDto userDTO, @ApiIgnore Principal principal) {
        if (userDTO.getId() == null && userService.checkIsUnique(userDTO.getEmail(), userDTO.getId())) {
            throw new IllegalArgumentException("Id not found in the update request or email not unique");
        }
        return userService
                .save(userMapper
                        .dtoToEntity(userDTO, userService.findById(userDTO.getId())
                        .orElseThrow(NotFoundException::new)), principal)
                .map(userMapper::entityToDto)
                .orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удалить Пользователя.", notes = "Удалить Пользователя (по правде поместить в архив).")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Пользователь удален."),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере."),
            @ApiResponse(responseCode = "400", description = "Запрос неверный."),
            @ApiResponse(responseCode = "404", description = "Адрес URL не найден."),
            @ApiResponse(responseCode = "403", description = "Вы не авторизованы. Авторизуйтесь и повторите еще раз."),
            @ApiResponse(responseCode = "401", description = "У вас не достаточно прав доступа."),
    })
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id, @ApiIgnore Principal principal) {
        userService.deleteById(id, principal);
        log.info("-=OK=-");
    }
}