package models.pages;

import config.Config;
import org.openqa.selenium.By;

public class HomePage extends Page {
    private static final String PAGE_URL = Config.INSIDER_BASE_HTTPS_URL;
    private static final By ACCEPT_COOKIES = By.cssSelector(".wt-cli-accept-all-btn");

    public HomePage() {
        this.setUrl(PAGE_URL);
    }

    public HomePage acceptCookies() {
        browser.clickToBy(ACCEPT_COOKIES);
        return this;
    }

    public HomePage selectNavigationBarItem(String navigationBarItem) {
        browser.clickToBy(By.xpath("//ul[@class='navbar-nav overflow-y']/li/a/span[text()='" + navigationBarItem + "']"));
        return this;
    }

    public HomePage selectNavigationBarSubItem(String navigationBarSubItem) {
        browser.clickToBy(By.xpath("//div[@class='d-flex flex-row pr-3']//h5[text()='" + navigationBarSubItem + "']"));
        return this;
    }
}
