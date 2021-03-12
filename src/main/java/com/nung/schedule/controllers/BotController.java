package com.nung.schedule.controllers;

import com.nung.schedule.Bot;
import com.nung.schedule.services.MessageService;
import com.nung.schedule.utils.DateUtil;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class BotController {
    private final Bot bot;
    private final MessageService messageService;

    public BotController(Bot bot, MessageService messageService) {
        this.bot = bot;
        this.messageService = messageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> getMessage(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
