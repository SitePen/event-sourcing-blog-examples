package com.example.esexample.util;

import com.example.esexample.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class AccountPrinter {

   @Autowired
   private BankAccountService bankAccountService;

   @Scheduled(fixedRate = 5_000)
   public void printAccountState() {
       log.info("-----------------------------------");
       try {
           log.info("Account state: {}", bankAccountService.getAccount(Constants.UNITY_ACCOUNT_ID));
       } catch (RestClientException e) {
           log.warn(
                   "Account service still starting up (unable to parse response for account " + Constants.UNITY_ACCOUNT_ID + ")");
       } catch (ResponseStatusException e) {
           if (e.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
               log.warn("Account [id: '{}'] not yet available", Constants.UNITY_ACCOUNT_ID);
           } else if (e.getStatus() == HttpStatus.NOT_FOUND) {
               log.warn("Account [id: '{}'] not found", Constants.UNITY_ACCOUNT_ID);
           } else {
               log.error("Failed to fetch account " + Constants.UNITY_ACCOUNT_ID, e);
           }
       } catch (RuntimeException e) {
           log.error("Failed to fetch account " + Constants.UNITY_ACCOUNT_ID, e);
       }
       log.info("-----------------------------------");
   }

}
