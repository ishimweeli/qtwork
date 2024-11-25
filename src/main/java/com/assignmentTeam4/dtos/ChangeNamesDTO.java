package com.assignmentTeam4.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangeNamesDTO {

    @NotBlank
    private String oldFirstName;
    @NotBlank
    private String OldSecondName;
    @NotBlank
    private String newFirstName;
    @NotBlank
    private String newSecondName;
}
