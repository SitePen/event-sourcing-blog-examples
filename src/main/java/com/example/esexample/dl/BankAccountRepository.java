package com.example.esexample.dl;

import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Repository
public class BankAccountRepository {

   @Autowired
   private InteractiveQueryService interactiveQueryService;

   public BankAccount getAccount(String accountId) {
       HostInfo hostInfo = interactiveQueryService
               .getHostInfo(Constants.EVENT_STORE_ACCOUNTS, accountId, new StringSerializer());

       if (null == hostInfo || null == hostInfo.host()) {
           throw new IllegalStateException("Unable to determine host responsible for the account");
       }

       if (interactiveQueryService.getCurrentHostInfo().equals(hostInfo)) {
           log.debug("Fetching account from local host {}:{}", hostInfo.host(), hostInfo.port());
           final ReadOnlyKeyValueStore<String, BankAccount> accountStore = interactiveQueryService
                   .getQueryableStore(Constants.EVENT_STORE_ACCOUNTS,
                           QueryableStoreTypes.<String, BankAccount>keyValueStore());

           return accountStore.get(accountId);
       } else {
           log.debug("Fetching account from remote host {}:{}", hostInfo.host(), hostInfo.port());

           RestTemplate restTemplate = new RestTemplate();
           return restTemplate.getForEntity(String.format("http://%s:%d%s%s", hostInfo.host(), hostInfo.port(),
                   Constants.RESOURCE_ACCOUNT_GET_PREFIX, accountId), BankAccount.class).getBody();
       }
   }

}
