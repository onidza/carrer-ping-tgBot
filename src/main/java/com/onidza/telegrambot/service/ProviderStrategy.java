package com.onidza.telegrambot.service;

import com.onidza.telegrambot.dto.VacancyDTO;

import java.util.List;

public interface ProviderStrategy {
    List<VacancyDTO> getContent(String searchString);
}
