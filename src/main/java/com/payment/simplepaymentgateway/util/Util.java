package com.payment.simplepaymentgateway.util;

import java.util.UUID;

public class Util {

    public static String generateAuthorizationId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert UUID to string and remove hyphens
        String authorizationId = uuid.toString().replaceAll("-", "");

        return authorizationId;
    }
}
