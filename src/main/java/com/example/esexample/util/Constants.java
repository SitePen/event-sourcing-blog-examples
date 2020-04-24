package com.example.esexample.util;

public class Constants {

   public static final String UNITY_ACCOUNT_ID = "unity";

   public static final String HEADER_ACCOUNT_ID = "accountId";
   public static final String HEADER_EVENT_TYPE = "eventType";
   public static final String HEADER_CORRELATION_UUID = "correlationUuid";

   public static final String EVENT_STORE_ACCOUNTS = "stores.accounts";

   public static final String RESOURCE_ACCOUNT_GET_PREFIX = "/account/";
   public static final String RESOURCE_ACCOUNT_GET = RESOURCE_ACCOUNT_GET_PREFIX + "{accountId}";

   private Constants() throws IllegalAccessException {
       throw new IllegalAccessException("Utility class");
   }

}
