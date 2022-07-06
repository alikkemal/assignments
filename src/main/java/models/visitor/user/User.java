package models.visitor.user;

import models.browsers.Browser;
import models.pages.Page;
import models.visitor.Visitor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static config.Config.WAITTIME_SMALL;

public class User extends Visitor<User> {
    public User(String email, String password) {
        super(email, password);
    }

    private static final By OUR_LOCATIONS_TEXT = By.xpath("//h3[@class='category-title-media ml-0']");
    private static final By LIFE_AT_INSIDER_TEXT = By.xpath("//h2[text()='Life at Insider']");
    private static final By LIFE_AT_INSIDER_IMAGE = By.xpath("//div[@class='elementor-carousel-image']");
    private static final By MOVE_RIGHT_BUTTON = By.xpath("//div[@class='col-md-6 mt-3 mt-md-0 d-flex justify-content-between justify-content-md-end align-items-end']/a[2]");
    private static final By POSITIONS_LIST = By.xpath("//div[@class='position-list-item-wrapper bg-light']");
    private static final By APPLY_NOW_BUTTON = By.xpath("//div[@class='position-list-item-wrapper bg-light']//a");

    public static <S extends Visitor> Matcher<S> shouldBeRedirectedTo(final Page page) {
        return new BaseMatcher<S>() {
            private Visitor visitor;

            @Override
            public boolean matches(final Object item) {
                this.visitor = (Visitor) item;
                return this.visitor.isRedirectedTo(page);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Visitor must be redirected to ").appendValue(page.url());
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("Visitor not right page on ").appendValue(visitor.browser().currentURL());
            }
        };
    }

    public static <S extends Visitor> Matcher<S> shouldSeeTheTeamsJobFields(List<String> fieldName) {
        return new BaseMatcher<S>() {
            WebElement element;

            @Override
            public boolean matches(Object item) {
                Visitor visitor = (Visitor) item;
                Browser browser = visitor.browser();

                for (String fields : fieldName) {
                    element = browser.findElement(By.xpath("//h3[@class='text-center mb-4 mb-xl-5'][text()='" + fields + "']"));
                    browser.scrollTo(element);
                    browser.visibilityWait(WAITTIME_SMALL, element);
                    browser.isWebElementVisible(element);
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(fieldName).appendText("Button should be displayed on page");
            }
        };
    }

    public static <S extends Visitor> Matcher<S> shouldSeeTheLocationsFields(List<String> fieldName) {
        return new BaseMatcher<S>() {
            WebElement element;

            @Override
            public boolean matches(Object item) {
                Visitor visitor = (Visitor) item;
                Browser browser = visitor.browser();
                browser.scrollTo(browser.findElement(OUR_LOCATIONS_TEXT));

                for (String fields : fieldName) {
                    element = browser.findElement(By.xpath("//p[@class='mb-0'][text()='" + fields + "']"));
                    browser.isWebElementVisible(element);
                    browser.clickToBy(MOVE_RIGHT_BUTTON);
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(fieldName).appendText("Element should be displayed on page");
            }
        };
    }

    public static <S extends Visitor> Matcher<S> shouldSeeTheLifeInsiderBlocks(int imageSize) {
        return new BaseMatcher<S>() {
            boolean elements;

            @Override
            public boolean matches(Object item) {
                Visitor visitor = (Visitor) item;
                Browser browser = visitor.browser();
                browser.scrollTo(browser.findElement(LIFE_AT_INSIDER_TEXT));

                elements = browser.findElement(LIFE_AT_INSIDER_IMAGE).getSize().equals(imageSize);

                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(imageSize).appendText("Size should be displayed on page");
            }
        };
    }

    public static <S extends Visitor> Matcher<S> shouldSeeTheTeamsJobFieldsAs(String department, String location) {
        return new BaseMatcher<S>() {
            boolean isOnPage;

            @Override
            public boolean matches(Object item) {
                Visitor visitor = (Visitor) item;
                Browser browser = visitor.browser();

                List<WebElement> elements = browser.findElements(POSITIONS_LIST);
                for (WebElement element : elements) {
                    isOnPage = element.getText().contains(department) && element.getText().contains(location);
                }
                return isOnPage;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(department).appendText("Should be displayed on tab");
                description.appendValue(location).appendText("Should be displayed on tab");
            }
        };
    }

    public static <S extends Visitor> Matcher<S> shouldSeeTheButton(String button) {
        return new BaseMatcher<S>() {
            boolean isOnPage;
            List<WebElement> buttons;
            List<WebElement> positionList;

            @Override
            public boolean matches(Object item) {
                Visitor visitor = (Visitor) item;
                Browser browser = visitor.browser();

                positionList = browser.findElements(POSITIONS_LIST);
                buttons = browser.findElements(APPLY_NOW_BUTTON);
                for (int x = 0; x < positionList.size(); x++) {
                    for (WebElement element : buttons) {
                        browser.scrollTo(positionList.get(x));
                        isOnPage = browser.mouseOver(element);
                    }
                }
                return isOnPage;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(button).appendText("Should be displayed on page");
            }
        };
    }
}
