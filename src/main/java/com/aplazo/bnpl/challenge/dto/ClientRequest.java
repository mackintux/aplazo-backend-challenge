package com.aplazo.bnpl.challenge.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientRequest {

    private String name;

    private LocalDate birthDate;

}
