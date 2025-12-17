package com.jpmc.midascore;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class KafkaConsumer {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    public KafkaConsumer(UserRepository userRepository,
                         TransactionRepository transactionRepository,
                         RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "midas-core-group"
    )
    @Transactional
    public void listen(Transaction transaction) {

        UserRecord sender =userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);


        if (sender == null || recipient == null) {
            return;
        }

        float amount = transaction.getAmount();

        if (sender.getBalance() < amount) {
            return;
        }

        // Call Incentive API
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;

        // Update balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        // Save transaction record
        TransactionRecord record =
                new TransactionRecord(sender, recipient, amount, incentiveAmount);
        transactionRepository.save(record);

        // Debug for Task 4
        if ("wilbur".equals(sender.getName()) || "wilbur".equals(recipient.getName())) {
            System.out.println(
                "WILBUR CURRENT BALANCE = " +
                ("wilbur".equals(sender.getName())
                    ? sender.getBalance()
                    : recipient.getBalance())
            );
        }
    }
}