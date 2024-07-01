package com.bot.telegram.config;

import com.bot.telegram.bot.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {
    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Value("${openai.gpt.model}")
    private String model;

    @Value("${openai.gpt.url}")
    private String url;

    private Bot bot;

    @Bean
    public Bot asdBot() {
        bot = new Bot(name, token, model, url);
        return bot;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
        return telegramBotsApi;
    }
}
