package com.onidza.telegrambot.service;

import com.onidza.telegrambot.dto.VacancyDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VacancyService {
    private final Provider[] providers;

    public VacancyService(Provider...providers) {
        this.providers = providers;
    }

    public List<VacancyDTO> getUpdates(String searchLine) {
        List<VacancyDTO> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.providerRequest(searchLine));
        }
        return vacancies;
    }
}
