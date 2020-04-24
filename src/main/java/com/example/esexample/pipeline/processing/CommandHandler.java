package com.example.esexample.pipeline.processing;

import com.example.esexample.model.commands.Command;
import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.model.events.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
@Slf4j
public class CommandHandler {

   @Bean
   public BiFunction<KStream<String, Command>, KTable<String, BankAccount>, KStream<String, AccountEvent>> processCommand() {
       return (commandStream, accountTable) -> commandStream.join(accountTable, (command, account) -> {
           if (command.canProcessForAccount(account)) { return command.processForAccount(account); }
           log.warn("Rejecting command {} (account status = {})", command, account);
           return null;
       }).filter((s, command) -> {
           if (null != command) {
               log.info("Sending command {}", command);
           }
           return null != command;
       });
   }

}
