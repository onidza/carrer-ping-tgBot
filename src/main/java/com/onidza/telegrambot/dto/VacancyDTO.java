package com.onidza.telegrambot.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VacancyDTO {
    private String siteName;
    private String title;
    private String url;
    private String companyName;
    private SalaryDTO salary;
    private String city;
    private String experience;
    private String format;
    private LocalDateTime createdAt;
}
