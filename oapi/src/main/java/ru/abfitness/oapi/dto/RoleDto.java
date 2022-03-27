package ru.abfitness.oapi.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.abfitness.oapi.dto.transfer.New;
import ru.abfitness.oapi.dto.transfer.Update;
import ru.abfitness.persist.entities.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
public class RoleDto {

    @Null(groups = {New.class})
    @NotNull(groups = {Update.class})
    private Integer id;

    @NotNull(groups = {New.class, Update.class})
    @ApiModelProperty(notes = "Уникальное название роли", example = "ROLE_DISPATCHER",required = true)
    private String name;

    @NotNull(groups = {New.class, Update.class})
    @ApiModelProperty(notes = "Описание роли", example = "Диспетчер",required = true)
    private String description;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description =role.getDescription();
    }
}