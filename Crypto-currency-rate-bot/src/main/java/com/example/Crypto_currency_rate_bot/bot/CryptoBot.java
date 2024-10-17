package com.example.Crypto_currency_rate_bot.bot;

import com.example.Crypto_currency_rate_bot.model.Subscriber;
import com.example.Crypto_currency_rate_bot.service.CryptoCurrencyService;
import com.example.Crypto_currency_rate_bot.service.TrackingCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingCommandBot {

    private final String botUsername;
    private final TrackingCost trackingCost;
    private final CryptoCurrencyService service;

    public CryptoBot(@Value("${telegram.bot.token}") String botToken,
                     @Value("${telegram.bot.username}") String botUsername,
                     List<IBotCommand> commandList, TrackingCost trackingCost, CryptoCurrencyService service) {
        super(botToken);
        this.botUsername = botUsername;
        this.trackingCost = trackingCost;
        this.service = service;
        commandList.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        SendMessage answer = new SendMessage();
        long chatId = update.getMessage().getChatId();
        answer.setChatId(chatId);
        answer.setText("Незнакомая команда, поробуйте команду: /help");
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("An error has occurred processNonCommandUpdate method " + e);
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedDelayString = "${app.minimum notification time}")
    @Async
    public void sendAutomaticMessage() throws IOException {
        List<Subscriber> subscribers = trackingCost.getSubscribersForMessage();
        SendMessage answer = new SendMessage();
        if (!subscribers.isEmpty()) {
            subscribers.forEach(subscriber -> {
                answer.setChatId(subscriber.getIdTelegram());
                try {
                    answer.setText("Пора покупать, стоимость биткоина " + service.getBitcoinPrice() + " USD");
                    log.info("Пора покупать, стоимость биткоина " + service.getBitcoinPrice()  + " USD");
                    execute(answer);
                } catch (TelegramApiException | IOException e) {
                    log.error("An error has occurred sendAutomaticMessage method: " + e);
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
