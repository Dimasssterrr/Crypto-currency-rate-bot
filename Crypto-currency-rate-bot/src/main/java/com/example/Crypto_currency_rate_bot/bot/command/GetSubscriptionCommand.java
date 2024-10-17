package com.example.Crypto_currency_rate_bot.bot.command;

import com.example.Crypto_currency_rate_bot.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

     private SubscriberRepository repository;
    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        Double subscribePrice = repository.findByIdTelegram(message.getFrom().getId()).getPrice();
        if (subscribePrice != null) {
            answer.setText("Вы подписаны на стоимость биткоина " + subscribePrice + " USD");
        } else {
            answer.setText("Активные подписки отсутствуют");
        }
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("An error has occurred /get_subscription " + e);
            throw new RuntimeException(e);
        }
    }
}
