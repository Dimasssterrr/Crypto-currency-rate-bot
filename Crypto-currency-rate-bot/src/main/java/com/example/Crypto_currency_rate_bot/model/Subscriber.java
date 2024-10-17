package com.example.Crypto_currency_rate_bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "Subscribers")
public class Subscriber {
    @Id
    @Column(name = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "id_telegram")
    private Long idTelegram;
    @Column(name = "price")
    private Double price;
}
