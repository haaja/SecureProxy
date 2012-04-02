/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.blackbox.integration;

import fi.silverskin.secureproxy.ProxyConfigurer;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author corvus
 */
public class PostTest {

    String configPath = System.getProperty("secureproxy.integration.config");
    ProxyConfigurer configurer = new ProxyConfigurer(configPath);

    @Test
    public void SimplePostTest() {
        System.out.println("Config read from: '" + configPath + "'");
        
        WebDriver driver = new HtmlUnitDriver();

        String publicUri = configurer.getProperty("publicURI");
        String publicHttpPort = configurer.getProperty("publicHttpPort");
        
        String url = publicUri + ":" + publicHttpPort + "/post-form.php";
        
        System.out.println("Fetching page: '" + url + "'");
        
        driver.get(url);

        System.out.println("Before POST, page title is: '" + driver.getTitle() + "'");

        WebElement element = driver.findElement(By.name("input"));

        String testInput = "testi";

        element.sendKeys(testInput);

        element.submit();

        System.out.println("After POST, page title is: '" + driver.getTitle() + "'");

        assertEquals(testInput, driver.getTitle());
    }
}
