package test.java;// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadPhotoTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new FirefoxDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void uploadPhoto() {
    // Test name: UploadPhoto
    // Step # | name | target | value | comment
    // 1 | open | /profile/1817836140 |  | 
    driver.get("https://www.mamba.ru/profile/1817836140");
    // 2 | setWindowSize | 1169x851 |  | 
    driver.manage().window().setSize(new Dimension(1169, 851));
    // 3 | click | id=Fill-1 |  | 
    driver.findElement(By.id("Fill-1")).click();
    // 4 | mouseOver | linkText=Поиск |  | 
    {
      WebElement element = driver.findElement(By.linkText("Поиск"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    // 5 | click | css=.hSNDiR |  | 
    driver.findElement(By.cssSelector(".hSNDiR")).click();
    // 6 | click | css=.sc-qyouz1-0:nth-child(4) > .sc-1kafmry-5 |  | 
    driver.findElement(By.cssSelector(".sc-qyouz1-0:nth-child(4) > .sc-1kafmry-5")).click();
    // 7 | click | css=.sc-1763te3-6 |  | 
    driver.findElement(By.cssSelector(".sc-1763te3-6")).click();
    // 8 | type | id=input-file-upload | C:\fakepath\MyGigaChadFace.png | 
    driver.findElement(By.id("input-file-upload")).sendKeys("C:\\fakepath\\MyGigaChadFace.png");
    // 9 | click | id=Fill-11 |  | 
    driver.findElement(By.id("Fill-11")).click();
    // 10 | mouseOver | linkText=Знакомства |  | 
    {
      WebElement element = driver.findElement(By.linkText("Знакомства"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    // 11 | click | css=.hSNDiR |  | 
    driver.findElement(By.cssSelector(".hSNDiR")).click();
  }
}
