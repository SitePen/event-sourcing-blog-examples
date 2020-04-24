package com.example.esexample.pipeline.input;

import com.example.esexample.model.commands.Command;
import com.example.esexample.model.commands.CommandType;
import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.service.BankAccountService;
import com.example.esexample.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
public class CommandRequestor {

   @Autowired
   private BankAccountService bankAccountService;

   @Bean
   public Supplier<Message<Command>> depositSource() {
       return () -> {
           final String accountId = Constants.UNITY_ACCOUNT_ID;

           try {
               final BankAccount bankAccount = bankAccountService.getAccount(accountId);
               if (null == bankAccount) {
                   return null;
               }
           } catch (RestClientException e) {
               log.warn("Account service still starting up (unable to parse response for account " + accountId + ")");
               return null;
           } catch (ResponseStatusException e) {
               if (e.getStatus() != HttpStatus.NOT_FOUND && e.getStatus() != HttpStatus.SERVICE_UNAVAILABLE) {
                   return null;
               }
           }

           Long amount = Math.round(Math.random() * 9.0d + 1.0d);
           Command depositRequest = Command.builder().commandType(CommandType.DEPOSIT).accountId(accountId)
                   .amount(amount).build();

           log.info("Sending deposit request: {}", depositRequest);

           return MessageBuilder.withPayload(depositRequest).setHeader(Constants.HEADER_ACCOUNT_ID, accountId)
                   .setHeader(Constants.HEADER_CORRELATION_UUID, UUID.randomUUID().toString()).build();
       };
   }

   @Bean
   public Supplier<Message<Command>> withdrawalSource() {
       return () -> {
           final String accountId = Constants.UNITY_ACCOUNT_ID;

           try {
               final BankAccount bankAccount = bankAccountService.getAccount(accountId);
               if (null == bankAccount) {
                   return null;
               }
           } catch (RestClientException e) {
               log.warn("Account service still starting up (unable to parse response for account " + accountId + ")");
               return null;
           } catch (ResponseStatusException e) {
               if (e.getStatus() != HttpStatus.NOT_FOUND && e.getStatus() != HttpStatus.SERVICE_UNAVAILABLE) {
                   return null;
               }
           }

           Long amount = Math.round(Math.random() * 49.0d + 1.0d);
           Command withdrawRequest = Command.builder().commandType(CommandType.WITHDRAW).accountId(accountId)
                   .amount(amount).build();

           log.info("Sending withdrawal request: {}", withdrawRequest);

           return MessageBuilder.withPayload(withdrawRequest).setHeader(Constants.HEADER_ACCOUNT_ID, accountId)
                   .setHeader(Constants.HEADER_CORRELATION_UUID, UUID.randomUUID().toString()).build();
       };
   }

}
