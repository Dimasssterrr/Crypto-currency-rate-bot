package com.example.Crypto_currency_rate_bot.repository;

import com.example.Crypto_currency_rate_bot.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
      Subscriber findByIdTelegram(Long id);
}
