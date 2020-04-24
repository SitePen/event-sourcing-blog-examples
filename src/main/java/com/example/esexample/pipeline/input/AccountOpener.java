package com.example.esexample.pipeline.input;

import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.model.events.AccountEvent;
import com.example.esexample.model.events.AccountEventType;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Slf4j
@Service
public class AccountOpener {

   @Autowired
   private BankAccountService bankAccountService;

   private AtomicBoolean accountOpened = new AtomicBoolean(false);

   @Bean
   public Supplier<Message<AccountEvent>> openAccount() {
       return () -> {
           final String accountId = Constants.UNITY_ACCOUNT_ID;

           try {
               bankAccountService.getAccount(accountId);
               return null;
           } catch (RestClientException e) {
               log.warn("Account service still starting up (unable to parse response for account " + accountId + ")");
               return null;
           } catch (ResponseStatusException e) {
               if (e.getStatus() != HttpStatus.NOT_FOUND) {
                   return null;
               }
           }

           if (!accountOpened.compareAndSet(false, true)) {
               return null;
           }
           log.info("Requesting opening of account '{}'", accountId);

           final BankAccount bankAccount = BankAccount.builder().accountId(accountId).balance(0l).build();

           final AccountEvent accountEvent = AccountEvent.builder().accountId(accountId)
                   .eventType(AccountEventType.ACCOUNT_OPENED).bankAccount(bankAccount).build();

           log.info("Sending account event: {}", accountEvent);

           return MessageBuilder.withPayload(accountEvent).setHeader("messageKey", accountId)
                   .setHeader(Constants.HEADER_ACCOUNT_ID, accountId)
                   .setHeader(Constants.HEADER_EVENT_TYPE, accountEvent.getEventType())
                   .setHeader(Constants.HEADER_CORRELATION_UUID, UUID.randomUUID().toString()).build();
       };
   }

}
