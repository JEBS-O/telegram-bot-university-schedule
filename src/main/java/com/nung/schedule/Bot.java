package com.nung.schedule;

import com.nung.schedule.services.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Bot extends TelegramWebhookBot {
    @Value("${telegrambot.username}")
    private String BOT_USERNAME;
    @Value("${telegrambot.token}")
    private String BOT_TOKEN;
    @Value("${telegrambot.path}")
    private String BOT_PATH;
    private final MessageService messageService;

    public Bot(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        return messageService.getMessage(update);
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotPath() {
        return BOT_PATH;
    }
}
