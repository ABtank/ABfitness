package ru.abfitness.oapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.abfitness.oapi.constants.SwaggerConstant;
import ru.abfitness.oapi.dto.RoleDto;
import ru.abfitness.oapi.dto.transfer.New;
import ru.abfitness.oapi.dto.transfer.Update;
import ru.abfitness.oapi.exceptions.NotFoundException;
import ru.abfitness.oapi.services.RoleService;
import ru.abfitness.persist.entities.Role;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/v1/roles")
@Api(tags = {SwaggerConstant.API_ROLE})
@RestController
public class RoleController {

    private RoleService roleService;

    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "Вывод списка ролей.", notes = "Найти Роли по названию или описанию.")
    @GetMapping
    public List<RoleDto> getAllRoles(
            @ApiParam(name = "name", value = "Название Роли", example = "ROLE_USER", allowEmptyValue = true)
            @RequestParam Optional<String> name,
            @ApiParam(name = "descr", value = "Описание Роли", example = "Пользователь", allowEmptyValue = true)
            @RequestParam Optional<String> descr) {
        Map<String, String> params = Map.of(
                "name", name.orElse(""),
                "descr", descr.orElse(""));
        return roleService.findAll(params)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Найти Роль по id.", notes = "Получение одной Роли по id", response = RoleDto.class)
    @GetMapping("/{id}")
    public RoleDto getRoleById(@PathVariable("id") Integer roleId) {
        return roleService.findById(roleId)
                .map(this::entityToDto)
                .orElseThrow(NotFoundException::new);
    }

    @ApiOperation(value = "Создать Роль.", notes = "Создать новую Роль", response = RoleDto.class)
    @PostMapping()
    public RoleDto createRole(@RequestBody
                                  @Validated(New.class)
                                          RoleDto roleDto, @ApiIgnore Principal principal) {
        roleDto.setId(null);
        return roleService.save(dtoToEntity(roleDto), principal)
                .map(this::entityToDto)
                .orElseThrow(NotFoundException::new);
    }

    @ApiOperation(value = "Изменить Роль.", notes = "Редактировать существующую Роль", response = RoleDto.class)
    @PutMapping
    public RoleDto updateRole(@RequestBody
                                  @Validated(Update.class)
                                          RoleDto roleDto, @ApiIgnore Principal principal) {
        if (roleDto.getId() == null) {
            throw new IllegalArgumentException("Role id must not be null");
        }
        return roleService.save(dtoToEntity(roleDto), principal)
                .map(this::entityToDto)
                .orElseThrow(NotFoundException::new);
    }

    @ApiOperation(value = "Удалить Роль.", notes = "Удалить существующую Роль")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRoleById(@PathVariable("id") Integer roleId, @ApiIgnore Principal principal) {
        roleService.deleteById(roleId, principal);
    }

    @ApiIgnore
    @DeleteMapping()
    public ResponseEntity<String> deleteRoles() {
        return new ResponseEntity<>("You cannot delete all Roles", HttpStatus.BAD_REQUEST);
    }

    private Role dtoToEntity(RoleDto dto) {
        return modelMapper.map(dto, Role.class);
    }

    private RoleDto entityToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }
}
