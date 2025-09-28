package com.onidza.telegrambot.service;

import com.onidza.telegrambot.dto.VacancyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Provider {
    private final ProviderStrategy strategy;

    public List<VacancyDTO> providerRequest(String searchLine) {
        List<VacancyDTO> vacancies = strategy.getContent(searchLine);
        return Objects.requireNonNullElse(vacancies, Collections.emptyList());
    }
}
