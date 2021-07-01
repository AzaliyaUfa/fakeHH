package ru.ibs.intern.service;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
public class FakeService {

    public static final String GRANT_TYPE = "client_credentials";

    public static final String CLIENT_ID = "intern";

    public static final String CLIENT_SECRET = "ibs";

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public String verifyClientCredentials(MultiValueMap<String,String> paramMap) {

        boolean verification = Objects.equals(paramMap.getFirst("grant_type"), GRANT_TYPE)&&
                Objects.equals(paramMap.getFirst("client_id"), CLIENT_ID)&&
                Objects.equals(paramMap.getFirst("client_secret"), CLIENT_SECRET);
        if (verification) {
            return generateNewToken();
        } else {
            return null;
        }
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
