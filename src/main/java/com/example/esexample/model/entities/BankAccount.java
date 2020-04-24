package com.example.esexample.model.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = BankAccount.BankAccountBuilder.class)
@Builder(builderClassName = "BankAccountBuilder", toBuilder = true)
public class BankAccount {

   String accountId;
   Long balance;

   @JsonPOJOBuilder(withPrefix = "")
   public static class BankAccountBuilder {}

}
