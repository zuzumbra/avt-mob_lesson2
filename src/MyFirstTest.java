import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class MyFirstTest {
    private AppiumDriver driver;

    String Text;

    @Before
    public void  setUp() throws Exception
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("deviceName","AndroidTestDevice");
        capabilities.setCapability("platformVersion","8.1");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","D:\\Projects\\avt-mob_lesson2\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void testWaitForTextAndFail()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia'",
                5
        );

        WebElement title_element = waitForElementPresent(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Cannot find 'Search…'",
                5
        );

        String required_text = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected text",
                "Search…",
                required_text
        );

    }

    @Test
    public void cancelSearch()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia'",
                5
        );

        waitForElementAndSendKeys(
                "PHP",
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Cannot find input",
                5
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Cannot find any articles",
                5
        );

       boolean elements_number = checkForMultipleResults(
                By.id("org.wikipedia:id/page_list_item_title")
        );

        Assert.assertEquals(
                "It's just one article",
                true,
                elements_number
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Search results still present on the page",
                5
        );

    }

    @Test
    public void testWaitForWord()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia'",
                5
        );

        waitForElementAndSendKeys(
                "PHP",
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Cannot find input",
                5
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Cannot find any articles",
                5
        );

        String test_result = waitForWordInEachElement(By.id("org.wikipedia:id/page_list_item_title"));

        Assert.assertEquals(
                "Not every result contains the search word",
                "Test passed",
                test_result
        );

    }


    private WebElement waitForElementPresent(By by, String error_massege, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_massege + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    private WebElement waitForElementPresent(By by, String error_massege)
    {
        return waitForElementPresent(by, error_massege, 5);
    }

    private boolean waitForElementNotPresent (By by, String error_massege, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_massege + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    private WebElement waitForElementAndClick(By by, String error_massege, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_massege, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(String value, By by, String error_massege, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_massege, timeoutInSeconds);
        element.sendKeys(value);
        this.Text = value;
        return element;
    }

    private boolean checkForMultipleResults(By by)
    {
        List<WebElement> elements = driver.findElements(by);
           return (elements.size() > 1);
    }

    private String waitForWordInEachElement (By by) {
        List<WebElement> elements = driver.findElements(by);
        String result = null;
        for (WebElement e : elements) {
            if (containsIgnoreCase(e.getText(), this.Text)) {
                result = result + 1;
            }
            else {
                result = result + 0;
            }
        }
        if (result.contains("0")){
            return "Test failed";
        }
        else{
            return "Test passed";
        }
    }

}
