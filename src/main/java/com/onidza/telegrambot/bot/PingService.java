package com.onidza.telegrambot.bot;

import com.onidza.telegrambot.dto.VacancyDTO;
import com.onidza.telegrambot.service.VacancyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class PingService {
    private final TelegramClient telegramClient;
    private final VacancyService vacancyService;

    @Setter
    private String searchLine;

    @Getter
    private final Set<Long> follower = new HashSet<>();

    @SneakyThrows
    @Scheduled(fixedRateString = "${ping.interval}")
    public void sendPing() {
        if (!follower.isEmpty()) {
            for (Long id : follower) {
                telegramClient.execute(SendMessage.builder()
                        .text("ты не вставил контекст")
                        .chatId(id)
                        .build());
                log.info("Пинг пользователя {}", id);
            }
        }
    }

//    public String getVacancies(String searchLine) {
//        return stringBuilder(vacancyService.getUpdates(searchLine));
//    }

//    private String stringBuilder(List<VacancyDTO> updates) {
//
//    }
}
