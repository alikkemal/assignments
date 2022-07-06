package models.pages;

import config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CareersPage extends Page {
    private static final String PAGE_URL = Config.INSIDER_BASE_HTTPS_URL + "careers/";

    private static final By SEE_ALL_TEAMS = By.xpath("//a[text()='See all teams']");
    private static final By SEE_ALL_JOBS_BUTTON = By.xpath("//a[text()='See all QA jobs']");
    private static final By LOCATION_DROPDOWNMENU = By.xpath("//span[text()='All']");
    private static final By DEPARTMENT_DROPDOWNMENU = By.xpath("//span[@title='Quality Assurance']");
    private static final By POSITIONS_LIST = By.xpath("//div[@class='position-list-item-wrapper bg-light']");
    private static final By APPLY_NOW_BUTTON = By.xpath("//div[@class='position-list-item-wrapper bg-light']//a");

    public CareersPage() {
        this.setUrl(PAGE_URL);
    }

    public CareersPage clickSeeAllTeamsButton() {
        browser.clickToBy(SEE_ALL_TEAMS);
        return this;
    }

    public List<String> getAllTeamsField() {
        List<String> fields = new ArrayList<>(Arrays.asList(
                "Customer Success",
                "Sales",
                "Product & Engineering",
                "Finance & Business Support",
                "Marketing",
                "CEO’s Executive Office",
                "Operations",
                "People and Culture",
                "Business Intelligence",
                "Security Engineering",
                "Partnership",
                "Quality Assurance",
                "Mobile Business Unit",
                "Partner Support Development",
                "Product Design"
        ));
        return fields.subList(0, 15);
    }

    public List<String> getAllLocationsField() {
        List<String> fields = new ArrayList<>(Arrays.asList(
                "Indianapolis",
                "Sao Paulo",
                "London",
                "Paris",
                "Amsterdam",
                "Barcelona",
                "Helsinki",
                "Warsaw",
                "Kiev",
                "Moscow",
                "Sydney",
                "Dubai",
                "Tokyo",
                "Seoul",
                "Hong Kong",
                "Singapore",
                "Bangkok",
                "Jakarta",
                "Taipei",
                "Manila",
                "Kuala Lumpur",
                "Ho Chi Minch City",
                "Istanbul",
                "Ankara",
                "Mexico City",
                "Lima",
                "Buenos Aires",
                "Bogota",
                "Santiago"
        ));
        return fields.subList(0, 29);
    }

    public CareersPage selectTeamsItem(String item) {
        browser.clickToBy(By.xpath("//h3[@class='text-center mb-4 mb-xl-5'][text()='" + item + "']"));
        return this;
    }

    public CareersPage seeAllQAJobs() {
        browser.clickToBy(SEE_ALL_JOBS_BUTTON);
        return this;
    }

    public CareersPage filterLocation(String location) {
        browser.clickToBy(LOCATION_DROPDOWNMENU);
        browser.clickToBy(By.xpath("//li[text()='" + location + "']"));
        return this;
    }

    public CareersPage filterDepartment(String department) {
        browser.clickToBy(DEPARTMENT_DROPDOWNMENU);
        browser.clickToBy(By.xpath("//li[text()='" + department + "']"));
        return this;
    }

    public String selectDefaultJobs() {
        List<WebElement> positionList = browser.findElements(POSITIONS_LIST);
        List<WebElement> buttons = browser.findElements(APPLY_NOW_BUTTON);
        browser.scrollTo(positionList.get(0));
        browser.wait(1);
        browser.mouseOver(buttons.get(0));
        return buttons.get(0).getAttribute("href");
    }
}
