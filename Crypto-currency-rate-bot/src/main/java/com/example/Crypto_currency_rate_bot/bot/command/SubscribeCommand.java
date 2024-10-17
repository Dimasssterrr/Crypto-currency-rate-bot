package com.example.Crypto_currency_rate_bot.bot.command;

import com.example.Crypto_currency_rate_bot.model.Subscriber;
import com.example.Crypto_currency_rate_bot.repository.SubscriberRepository;
import com.example.Crypto_currency_rate_bot.service.CryptoCurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private CryptoCurrencyService service;

    private SubscriberRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        String strPrice = message.getText().replaceAll("[^0-9.,]", "")
                .replaceAll(",", ".").trim();
        try {
            if (strPrice.isEmpty() || !wordAndNumberVerification(message.getText())) {
                answer.setText("В команде не указана цена подписки!");
                absSender.execute(answer);
                return;
            }
            double price = Double.parseDouble(strPrice);
            answer.setText("Текущая цена биткоина " + service.getBitcoinPrice() + " USD");
            absSender.execute(answer);
            answer.setText("Новая подписка создана на стоимость " + price + " USD");
            absSender.execute(answer);
            if (price > service.getBitcoinPrice()) {
                answer.setText("Пора покупать, стоимость биткоина " + service.getBitcoinPrice() + " USD");
                absSender.execute(answer);
            }
            subscribePrice(price, message);

        } catch (IOException | TelegramApiException e) {
            log.error("An error has occurred /subscribe " + e);
            throw new RuntimeException(e);
        }
    }

    public void subscribePrice(double price, Message message) {
        Subscriber subscriber = repository.findByIdTelegram(message.getFrom().getId());
        if (subscriber != null) {
            subscriber.setPrice(price);
            repository.save(subscriber);
            log.info("Пользователь " + message.getFrom().getUserName() + " подписан на цену " + price);
        }
    }
    public boolean wordAndNumberVerification(String input) {
        String regex = "^\\s*\\w+\\s+\\d+\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.replaceAll("/", ""));
       return matcher.matches();
    }
}
