package models.browsers;

import base.TestContext;
import io.qameta.allure.Attachment;
import models.pages.Page;
import models.visitor.Visitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.ScreenshotException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import static config.Config.*;
import static java.lang.String.format;
import static models.browsers.Browsers.runBrowser;
import static models.browsers.Browsers.runDefault;
import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.OutputType.FILE;
import static utils.FileUtils.getFile;
import static utils.FileUtils.writeFile;
import static utils.ScreenSizeForVideo.SCREENSIZE;

public abstract class Browser extends SeleniumBrowser {
    private static final Logger LOGGER = LogManager.getLogger(Browser.class);

    private Page page;

    private int fileCount = 1;

    private Visitor visitor;

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public abstract void initInLocal();

    @Attachment(value = "Fail screenshot", type = "image/png")
    public byte[] takeScreenshot(String scrFilename) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = ((TakesScreenshot) webDriver).getScreenshotAs(BYTES);
            writeFile(scrFilename, byteArray);
        } catch (ScreenshotException sse) {
            LOGGER.error("Taking screenshot has been failed");
        }
        return byteArray;
    }

    public final Browser changePage(Page page) {
        this.page = page;
        this.page.setBrowser(this);
        return this;
    }

    public static Browser openThe(Page page, String browserName) {
        Browser browser = browserName == null ? runDefault() : runBrowser(browserName);
        return browser.open(page);
    }

    private void initBrowser() {
        LOGGER.info("Open local {" + DEFAULT_BROWSER_NAME + "} models.browser");
        initInLocal();
    }

    private Browser open(Page page) {
        initBrowser();
        this.page = page;
        this.page.setBrowser(this);
        webDriver.get(page.url());
        waitForAjax();
        return this;
    }

    public void refresh() {
        webDriver.get(page.url());
        waitForPageToCompleteState(webDriver);
    }

    public String currentURL() {
        try {
            return webDriver.getCurrentUrl();
        } catch (TimeoutException toe) {
            LOGGER.error("Timeout when trying to get currentUrl!");
            return null;
        } catch (WebDriverException wde) {
            LOGGER.error("Exception occured while trying to get currentUrl, " + wde);
            return null;
        }
    }

    public void close() {
        webDriver.quit();
    }

    public final Page page() {
        return this.page;
    }

    public void clickToBy(By by) {
        try {
            clickTo(visibilityWait(WAITTIME_MEDIUM, findElement(by)));
        } catch (StaleElementReferenceException sere) {
            LOGGER.error("Stale element exception has been thrown, will try again.");
            wait(WAITTIME_SMALL);
            clickToBy(by);
        }
    }

    public void clickTo(WebElement element) {
        highlightElement(element);
        scrollTo(element);
        waitForClickableOf(30, element);
        element.click();
        waitForPageToCompleteState(webDriver);
    }

    public void highlightElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].style.background = 'yellow';", element);
    }

    public Object js(String script) {
        return js(script, null);
    }

    public Object js(String script, WebElement arg) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Object response = "";
        try {
            response = js.executeScript(script, arg);
        } catch (WebDriverException wde) {
            LOGGER.error("Cannot execute script");
        }

        return response;
    }

    public void scrollTo(WebElement element) {
        visibilityWait(30, element);
        scrollTo(element, 250);
    }

    private void scrollTo(WebElement element, int margin) {
        scrollTo(element.getLocation().x, element.getLocation().y - margin);
    }

    public void scrollTo(int x, int y) {
        js("scrollTo(" + x + "," + y + ");");
    }

    public <P extends Page> Browser goTo(P page) {
        this.page = page;
        this.page.setBrowser(this);
        webDriver.navigate().to(page.url());
        waitForPageToCompleteState(webDriver);
        return this;
    }

    public void switchWindow() {
        Set<String> handlers = webDriver.getWindowHandles();
        String mainWindow = "";

        if (handlers.size() > 1) {
            mainWindow = webDriver.getWindowHandle();
        }

        for (String handler : handlers) {
            if (!handler.equals(mainWindow)) {
                webDriver.switchTo().window(handler);
                break;
            }
        }
        waitForPageToCompleteState(webDriver);
    }

    void takeScreenShotForVideo() {
        takeScreenShotForVideo(TestContext.get().getTestMethodName() + "-" + format("%03d", fileCount));
        fileCount++;
    }

    private void takeScreenShotForVideo(String scrFilename) {
        try {
            File byteArray = ((TakesScreenshot) webDriver).getScreenshotAs(FILE);
            BufferedImage originalImage = ImageIO.read(byteArray);
            BufferedImage resizedImage = resizeImage(originalImage);

            String filePath = getFile(scrFilename, new File(""));
            ImageIO.write(resizedImage, "jpg", new File(filePath.replace(".png", ".jpg")));
        } catch (ScreenshotException | IOException sse) {
            LOGGER.error("Taking screenshot has been failed, " + sse);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage) {
        BufferedImage newBufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        BufferedImage resizedImage = new BufferedImage(SCREENSIZE.getWidth(), SCREENSIZE.getHeight(), newBufferedImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(newBufferedImage, 0, 0, SCREENSIZE.getWidth(), SCREENSIZE.getHeight(), null);
        g.dispose();

        return resizedImage;
    }

    public void navigateUrl(String url) {
        webDriver.navigate().to(url);
    }
}
