package com.onidza.telegrambot.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final PingService pingService;
    private boolean started = false;

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            String received = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (received) {
                case "/start" -> {
                    if (!started) {
                        started = true;
                        log.info("Бот запущен пользователем {}", chatId);
                        log.info("Сохранили chatId для пинга: {}", chatId);
                        telegramClient.execute(SendMessage.builder()
                                .text("Бот активирован. Задайте направление поиска")
                                .chatId(chatId)
                                .build());
                    }
                }

                case "/stop" -> {
                    pingService.getFollower().remove(chatId);
                    started = false;
                    log.info("Пинги остановлены для: {}", chatId);
                    telegramClient.execute(SendMessage.builder()
                            .text("Пинги остановлены")
                            .chatId(chatId)
                            .build());
                }

                default -> {
                    if (started) {
                        pingService.setSearchLine(received);
                        log.info("Задан поиск: {}", received);
                        telegramClient.execute(SendMessage.builder()
                                .text("Понял тебя, буду пинговать вакансии по поиску: " + received)
                                .chatId(chatId)
                                .build());
                        pingService.getFollower().add(chatId);
                    }
                }
            }
        }
    }
}
