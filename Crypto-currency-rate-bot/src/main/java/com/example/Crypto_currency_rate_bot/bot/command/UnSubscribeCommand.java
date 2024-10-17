package com.example.Crypto_currency_rate_bot.bot.command;

import com.example.Crypto_currency_rate_bot.model.Subscriber;
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
public class UnSubscribeCommand implements IBotCommand {

    private SubscriberRepository repository;
    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку на цену";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage answer = new SendMessage();
        SendMessage answerTwo = new SendMessage();
        answer.setChatId(message.getChatId());
        answerTwo.setChatId(message.getChatId());
        Subscriber subscriber = repository.findByIdTelegram(message.getFrom().getId());
        subscriber.setPrice(null);
        repository.save(subscriber);
        answer.setText("Подписка отменена");
        if (subscriber.getPrice() == null) {
            answerTwo.setText("Активные подписки отсутствуют");
        }
        try {
            absSender.execute(answer);
            absSender.execute(answerTwo);
        } catch (TelegramApiException e) {
            log.error("An error has occurred /unsubscribe " + e);
            throw new RuntimeException(e);
        }



    }
}
