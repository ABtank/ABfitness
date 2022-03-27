package ru.abfitness.oapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    @NotNull
    @ApiModelProperty(notes = "Заголовок письма", example = "Хочу чтоб вообще....",required = true)
    private String header;
    @NotNull
    @ApiModelProperty(notes = "Тело письма", example = "Всякие подробности того что хочет",required = true)
    private String body;
    @ApiModelProperty(notes = "Email адреса. Кому еще отправить кроме тех. поддержки.", example = "[\"abtank@bk.ru\"]")
    private Set<String> to;
    @ApiModelProperty(notes = "Email адреса. Кого поставить в копию.", example = "[\"abtank@bk.ru\"]")
    private Set<String> cc;
//    private Set<String> attaches;
}
