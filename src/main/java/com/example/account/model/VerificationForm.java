package com.example.account.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class VerificationForm {
    @NotEmpty
    @Email
    private String email;


}
