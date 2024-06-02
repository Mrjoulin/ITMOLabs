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
public class AboutTextIncorrectTest {
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
  public void aboutTextIncorrect() {
    // Test name: AboutTextIncorrect
    // Step # | name | target | value | comment
    // 1 | open | /rating |  | 
    driver.get("https://www.mamba.ru/rating");
    // 2 | setWindowSize | 1125x801 |  | 
    driver.manage().window().setSize(new Dimension(1125, 801));
    // 3 | click | xpath=//div[@id='app-wrapper']/div/header/div[6]/a/img |  | 
    driver.findElement(By.xpath("//div[@id=\'app-wrapper\']/div/header/div[6]/a/img")).click();
    // 4 | click | xpath=//div[@id='app-wrapper']/div[3]/div/div[2]/div/section/div[3]/a/div/div[2] |  | 
    driver.findElement(By.xpath("//div[@id=\'app-wrapper\']/div[3]/div/div[2]/div/section/div[3]/a/div/div[2]")).click();
    // 5 | click | name=textarea |  | 
    driver.findElement(By.name("textarea")).click();
    // 6 | executeScript | var result = ''; const length = 300; const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';     const charactersLength = characters.length;     let counter = 0;     while (counter < length) {       result += characters.charAt(Math.floor(Math.random() * charactersLength));       counter += 1;     } return result; | result | 
    vars.put("result", js.executeScript("var result = \'\'; const length = 300; const characters = \'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\';     const charactersLength = characters.length;     let counter = 0;     while (counter < length) {       result += characters.charAt(Math.floor(Math.random() * charactersLength));       counter += 1;     } return result;"));
    // 7 | type | name=textarea | ${result} | 
    driver.findElement(By.name("textarea")).sendKeys(vars.get("result").toString());
    // 8 | click | xpath=//input[@value='Готово'] |  | 
    driver.findElement(By.xpath("//input[@value=\'Готово\']")).click();
    // 9 | click | xpath=//div[@id='app']/div[3]/div/div/div/div/main/div/div[2]/div/button |  | 
    driver.findElement(By.xpath("//div[@id=\'app\']/div[3]/div/div/div/div/main/div/div[2]/div/button")).click();
    // 10 | click | css=.sc-qyouz1-1 > svg |  | 
    driver.findElement(By.cssSelector(".sc-qyouz1-1 > svg")).click();
  }
}