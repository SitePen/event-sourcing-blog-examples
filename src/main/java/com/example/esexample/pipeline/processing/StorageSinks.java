package com.example.esexample.pipeline.processing;

import com.example.esexample.model.commands.Command;
import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.model.events.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class StorageSinks {

   @Bean
   public Function<KStream<String, AccountEvent>, KStream<String, BankAccount>> getAccountFromEvent() {
       return (eventStream) -> eventStream.mapValues((s, event) -> event.getBankAccount());
   }

   @Bean
   public Consumer<KTable<String, BankAccount>> accountStorageSink() {
       return accountTable -> {
           accountTable.mapValues((accountId, bankAccount) -> {
               log.info("Sinking account #{} to persistent state store: {} [{}]", accountId,
                       accountTable.queryableStoreName(), bankAccount);
               return bankAccount;
           });
       };
   }

   @Bean
   public Consumer<KTable<String, Command>> commandStorageSink() {
       return commandTable -> {
           commandTable.mapValues((accountId, command) -> {
               log.info("Sinking command to persistent state store: {} [{}]", commandTable.queryableStoreName(),
                       command);
               return command;
           });
       };
   }

}
