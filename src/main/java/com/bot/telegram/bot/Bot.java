package com.bot.telegram.bot;

import com.bot.telegram.dto.ChatGPTRequest;
import com.bot.telegram.dto.ChatGPTResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private static final String WELCOME_MSG = """
            Hi %s, nice to meet you!
            This bot is designed to give you hand when you have questions! You can interact with it using the following commands:
            /start: Displays a welcome message.
            /chatgpt: Initiates a prompt conversation with the ChatGPT.
            /clear: Clears the chat history.
            """;
    private static final String UNKNOWN_COMMAND_MSG = """
            Dear %s, currently, the mentioned command isn't supported by the bot. 
            Please give it a go with the existing ones: 
            /start: Displays a welcome message.
            /chatgpt: Initiates a prompt conversation with the ChatGPT.  
            /clear: Clears the chat history.
            """;

    @Autowired
    private Environment env;

    private String name;
    private String token;
    private String gptModel;
    private String gptUrl;

    private List<Integer> messageIds = new LinkedList<>();

    @Autowired
    private RestTemplate restTemplate;

    public Bot() {
    }

    public Bot(String name, String token, String gptModel, String gptUrl) {
        this.name = name;
        this.token = token;
        this.gptModel = gptModel;
        this.gptUrl = gptUrl;

        List<BotCommand> botCommands = List.of(new BotCommand("/start", "Displays a welcome message."),
                new BotCommand("/chatgpt", "Initiates a prompt conversation with the ChatGPT."),
                new BotCommand("/clear", "Clears the chat history."));
        try {
            this.execute(new SetMyCommands(botCommands));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        LOGGER.info("Bot Username: " + this.name);
        return this.name;
    }

    @Override
    public String getBotToken() {
        LOGGER.info("Bot Token: " + this.token);
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            messageIds.add(update.getMessage().getMessageId());
            String message = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();

            if(message.startsWith("/")) {
                switch (message) {
                case "/start" -> startCommand(chatId, update.getMessage().getChat().getFirstName());
                case "/chatgpt" -> chatGptCommand(chatId, message);
                case "/clear" -> clearCommand(chatId);
                default -> unknownCommand(chatId, update.getMessage().getChat().getFirstName());
                }
            } else {
                chat(chatId, message);
            }
        }
    }

    private void startCommand(long chatId, String firstName) {
        send(chatId, String.format(WELCOME_MSG, firstName));
    }

    private void chatGptCommand(long chatId, String messageToBePrompted) {
        chat(chatId, messageToBePrompted);
    }

    private void chat(long chatId, String message) {
        ChatGPTRequest request = new ChatGPTRequest(this.gptModel, message);
        ChatGPTResponse response = restTemplate.postForObject(this.gptUrl, request,
                ChatGPTResponse.class);

        if(response != null) {
            send(chatId, response.getChoices().get(0).getMessage().getContent());
        }
    }

    private void clearCommand(long chatId) {
        for (Integer messageId : messageIds) {
            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .messageId(messageId)
                    .build();
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                LOGGER.info(String.format("Cannot execute command and delete the chat history. Cause: %s" , e.getMessage()));
            }
        }

        messageIds.clear();
    }

    private void unknownCommand(long chatId, String firstName) {
        send(chatId, String.format(UNKNOWN_COMMAND_MSG, firstName));
    }

    private void send(long chatId, String messageToSend) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(messageToSend)
                .build();

        try {
            Message sentMessage = execute(sendMessage);
            messageIds.add(sentMessage.getMessageId());
        } catch (TelegramApiException e) {
            LOGGER.info(String.format("Cannot execute command. Cause: %s" , e.getMessage()));
        }
    }
}
