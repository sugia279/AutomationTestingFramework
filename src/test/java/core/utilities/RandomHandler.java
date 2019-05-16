package core.utilities;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

public class RandomHandler {

    public static String randomPrintExcept(int count, String... except) {
        String randomString;
        do {
            randomString = RandomStringUtils.randomAlphabetic(count);
        } while (Arrays.stream(except).parallel().anyMatch(randomString::contains));
        return randomString;
    }
}
