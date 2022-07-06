package models.browsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ScheduledUtils;

import java.util.List;

import static config.Config.WAITTIME_INSTANT;
import static config.Config.WAITTIME_TIMEOUT;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class SeleniumBrowser {
    private static final Logger LOGGER = LogManager.getLogger(SeleniumBrowser.class);

    protected WebDriver webDriver;

    public void waitForAjax() {
        try {
            WebDriverWait myWait = new WebDriverWait(webDriver, WAITTIME_TIMEOUT);
            ExpectedCondition<Boolean> conditionToCheck = input -> {
                JavascriptExecutor jsDriver = (JavascriptExecutor) webDriver;
                boolean stillRunningAjax = (Boolean) jsDriver
                        .executeScript("return (window.jQuery != undefined && ($(':animated').length != 0 || jQuery.active != 0)) || document.readyState != \"complete\"");
                return !stillRunningAjax;
            };

            myWait.until(conditionToCheck);
        } catch (RuntimeException rte) {
            LOGGER.error("" + rte);
        }
    }

    public WebElement findElement(By by) {
        return findElement(by, null);
    }

    private WebElement findElement(By by, WebElement element) {
        List<WebElement> elements;
        if (element != null) {
            elements = element.findElements(by);
        } else {
            elements = webDriver.findElements(by);
        }
        return elements.isEmpty() ? null : elements.get(0);
    }

    public WebElement visibilityWait(int timeoutInSeconds, WebElement element) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver).
                withTimeout(ofSeconds(timeoutInSeconds)).
                pollingEvery(ofMillis(WAITTIME_INSTANT)).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
        return wait.until(visibilityOf(element));
    }

    public WebElement waitForClickableOf(int timeoutInSeconds, WebElement elementLocator) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver).
                withTimeout(ofSeconds(timeoutInSeconds)).
                pollingEvery(ofMillis(WAITTIME_INSTANT)).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
        return wait.until(elementToBeClickable(elementLocator));
    }

    public boolean isWebElementVisible(WebElement webElement) {
        if (webElement != null) {
            try {
                return webElement.isDisplayed();
            } catch (NoSuchElementException nsee) {
                LOGGER.info("Element is not visible! " + nsee);
                return false;
            }
        } else {
            return false;
        }
    }

    protected void waitForPageToCompleteState(WebDriver driver) {
        int counter = 0;
        int maxNoOfRetries = 15;
        while (counter != maxNoOfRetries) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
                wait(WAITTIME_TIMEOUT);
            } catch (Exception e) {
                LOGGER.warn("The Page is still loading.");
            }
            counter++;
        }
    }

    public void wait(int seconds) {
        waitMs(seconds * 1000);
    }

    public void waitMs(int milliSeconds) {
        try {
            ScheduledUtils.threadWaiter(milliSeconds);
        } catch (RuntimeException rte) {
            LOGGER.error("Exception on selenium waiting, " + rte);
        }
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public boolean mouseOver(WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element).perform();
        return true;
    }
}
