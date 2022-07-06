package config;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.http.util.TextUtils.isBlank;

public class Config {
    public static final String DEFAULT_BROWSER_NAME;
    public static final Platform platform;
    public static final String SCREENSHOT_PATH;
    public static final String USER_DIR;
    public static final String API_BASE_PATH;

    protected static final Properties properties;

    public static final String INSIDER_BASE_HTTPS_URL;

    public static final int WAITTIME_INSTANT = 500;
    public static final int WAITTIME_TOO_SMALL = 1;
    public static final int WAITTIME_SMALL = 5;
    public static final int WAITTIME_MEDIUM = 30;
    public static final int WAITTIME_TIMEOUT = 60;

    static {
        DEFAULT_BROWSER_NAME = getBrowser();
        platform = getPlatform();

        properties = loadProperties();
        API_BASE_PATH = properties.getProperty("api.base.path");
        INSIDER_BASE_HTTPS_URL = properties.getProperty("insider.prod");
        USER_DIR = System.getProperty("user.dir");
        SCREENSHOT_PATH = USER_DIR + "/screenshots/";
    }

    private static Properties loadProperties() {
        String configFileName = "properties/config.properties";
        InputStream in = ClassLoader.getSystemResourceAsStream(configFileName);
        Properties properties = new Properties();

        try {
            properties.load(in);
        } catch (IOException ioe) {
            throw new IllegalStateException("Exception on loading {" + configFileName + "} conf file from classpath", ioe);
        }
        return properties;
    }

    private static String getBrowser() {
        String browser = System.getProperties().getProperty("models/browsers");

        if (isBlank(browser)) {
            browser = "chrome"; // Default
        }
        return browser;
    }

    private static Platform getPlatform() {
        String platformConf = System.getProperties().getProperty("platform");

        if (StringUtils.isBlank(platformConf)) {
            return null;
        }
        return Platform.valueOf(platformConf.toUpperCase());
    }
}
