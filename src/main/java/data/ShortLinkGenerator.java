package data;

import java.util.Random;

public class ShortLinkGenerator {
    private static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String BASE_URL = "https://short.link/";
    private static final int SHORT_LINK_LENGTH = 8;

    private final Random random = new Random();

    public String generateShortLink() {
        return BASE_URL + generateRandomString();
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_LINK_LENGTH; i++) {
            int index = random.nextInt(SYMBOLS.length());
            sb.append(SYMBOLS.charAt(index));
        }
        return sb.toString();
    }
}