package ru.abfitness.oapi.dto.users;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @ApiModelProperty(notes = "Unique Email address", required = true, example = "email@mail.com")
    private String email;
    @ApiModelProperty(required = true, example = "qwerty")
    private String password;
}
