package models.browsers.chrome;

import models.browsers.Browser;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class Chrome extends Browser {
    public abstract ChromeOptions getOptions(boolean isHeadless);

    @Override
    public void initInLocal() {
        System.setProperty("webdriver.chrome.driver", "src/main/java/models/browsers/chrome/chromedriver");
        webDriver = new ChromeDriver(getOptions(false));
        webDriver.manage().window().maximize();
    }
}
