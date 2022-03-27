package ru.abfitness.oapi.dto.users;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    @ApiModelProperty(notes = "Bearer token")
    private String accessToken;
}
