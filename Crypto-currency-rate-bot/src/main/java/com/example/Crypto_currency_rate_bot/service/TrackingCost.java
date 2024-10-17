package com.example.Crypto_currency_rate_bot.service;

import com.example.Crypto_currency_rate_bot.model.Subscriber;
import com.example.Crypto_currency_rate_bot.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TrackingCost {

    private SubscriberRepository repository;

    private CryptoCurrencyService service;
    @Scheduled(fixedDelayString = "${app.rate update frequency}")
     public List<Subscriber> getSubscribersForMessage() throws IOException {
         List<Subscriber> subscribers = repository.findAll();
         List<Subscriber> subscribersForMessage = new ArrayList<>();
         double price = service.getBitcoinPrice();
         subscribers.forEach(subscriber -> {
             if(subscriber.getPrice() != null && subscriber.getPrice() > price) {
                subscribersForMessage.add(subscriber);
             }
         });
        return subscribersForMessage;
     }
}
