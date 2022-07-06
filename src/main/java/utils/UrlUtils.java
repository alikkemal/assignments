package utils;

public class UrlUtils {

    private static final String HTTPS = "https";

    private UrlUtils() {
    }

    public static boolean contains(String url1, String url2) {
        String url1Cropped = url1.replace(HTTPS, "").replace("http", "");
        String url2Cropped = url2.replace(HTTPS, "").replace("http", "");
        return url1Cropped.contains(url2Cropped);
    }
}
