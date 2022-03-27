package ru.abfitness.oapi.dto.users;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.abfitness.oapi.dto.transfer.New;
import ru.abfitness.oapi.dto.transfer.Update;
import ru.abfitness.oapi.validators.EqualFieldsConstraint;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualFieldsConstraint(baseField = "passwordConfirmation", matchField = "password")
public class ChangePasswordDto {
    @ApiModelProperty(notes = "Старый пароль", example = "example",required = true)
    @NotNull(groups = {New.class, Update.class})
    private String oldPassword;

    @NotNull(groups = {New.class, Update.class})
    @ApiModelProperty(notes = "Новый пароль", example = "new_example",required = true)
    private String password;

    @NotNull(groups = {New.class, Update.class})
    @ApiModelProperty(notes = "Подтверждение пароля", example = "new_example",required = true)
    private String passwordConfirmation;
}