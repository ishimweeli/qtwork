package com.assignmentTeam4.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class ChangePasswordDTO {

  @NotBlank
    private String oldPassword;
   @NotBlank
    private String newPassword;


}


