package ru.abfitness.oapi.dto.users;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import ru.abfitness.oapi.dto.transfer.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequestDto { //TODO валидатор
    @Email(groups = {Update.class})
    @NotNull(groups = {Update.class})
    @ApiModelProperty(notes = "Адрес электронной почты", example = "email@mail.com",required = true)
    private String email;
    @NotNull(groups = {Update.class})
    @ApiModelProperty(notes = "Имя", example = "Имя",required = true)
    private String firstName;
    @ApiModelProperty(notes = "Отчество", example = "Отчество")
    private String middleName;
    @NotNull(groups = {Update.class})
    @ApiModelProperty(notes = "Фамилия", example = "Фамилия",required = true)
    private String lastName;
    @NotNull(groups = {Update.class})
    @ApiModelProperty(notes = "Номер телефона", example = "+79500309968",required = true)
    private String phone;
    @NonNull
    @ApiModelProperty(notes = "Роли", example = "ROLE_USER",required = true)
    private String userRole;
    @NonNull
    @ApiModelProperty(required = true, example = "qwerty")
    private String password;
    @NonNull
    @ApiModelProperty(required = true, example = "qwerty")
    private String passwordConfirmation;
}