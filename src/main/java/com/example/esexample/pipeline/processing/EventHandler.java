package com.example.esexample.pipeline.processing;

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
public class EventHandler {

   @Bean
   public BiFunction<KStream<String, AccountEvent>, KTable<String, BankAccount>, KStream<String, AccountEvent>> processEvent() {
       return (eventStream, accountTable) -> eventStream
               .leftJoin(accountTable, (event, bankAccount) -> null == bankAccount ? event : null)
               .filter((s, event) -> {
                   if (null != event) {
                       log.info("Sending account event {}", event);
                   }
                   return null != event;
               });
   }

}
