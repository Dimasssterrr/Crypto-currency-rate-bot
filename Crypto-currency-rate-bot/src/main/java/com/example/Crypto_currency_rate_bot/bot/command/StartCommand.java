package com.example.Crypto_currency_rate_bot.bot.command;

import com.example.Crypto_currency_rate_bot.model.Subscriber;
import com.example.Crypto_currency_rate_bot.repository.SubscriberRepository;
import com.example.Crypto_currency_rate_bot.service.TrackingCost;
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
public class StartCommand implements IBotCommand {

    private final SubscriberRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText("Привет! Данный бот помогает отслеживать стоимость биткоина. "
                + listAvailableCommands());

        try {
            createSubscriber(message);
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
            throw new RuntimeException(e);
        }
    }

    public void createSubscriber(Message message) throws TelegramApiException {
        Subscriber subscriber = repository.findByIdTelegram(message.getFrom().getId());
        if (subscriber != null) {
            return;
        }
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setIdTelegram(message.getFrom().getId());
        repository.save(newSubscriber);
    }
    public String listAvailableCommands() {
       return  "Поддерживаемые команды:\n" +
                " /start - запустить бота\n" +
                " /get_price - получить стоимость биткоина\n" +
                " /subscribe [число] - подписаться на стоимость биткоина в USD\n" +
                " /get_subscription - получить текущую подписку\n" +
                " /unsubscribe - отменить подписку на стоимость\n" +
                " /help - получить список доступных команд";
    }
}
