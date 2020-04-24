package com.example.esexample.service;

import com.example.esexample.dl.BankAccountRepository;
import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@Slf4j
public class BankAccountService {

   @Autowired
   private BankAccountRepository bankAccountRepository;

   @RequestMapping(Constants.RESOURCE_ACCOUNT_GET)
   public BankAccount getAccount(@RequestParam(value = "accountId") String accountId) {
       if (null == accountId || accountId.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account ID specified");
       }

       try {
           final BankAccount account = bankAccountRepository.getAccount(accountId);
           if (account == null) {
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Specified account not found");
           }
           return account;
       } catch (IllegalStateException e) {
           throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                   "This server is currently unable to process the request - please try again later");
       }
   }

}
