package com.example.Crypto_currency_rate_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoCurrencyRateBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoCurrencyRateBotApplication.class, args);
	}

}
