/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.blackbox.integration;

import fi.silverskin.secureproxy.ProxyConfigurer;
import java.util.Enumeration;
import java.util.Properties;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author corvus
 */
public class PostTest {

    @Test
    public void SimplePostTest() {
        System.out.println("In SimplePostTest");

        Enumeration prop = System.getProperties().propertyNames();
        while (prop.hasMoreElements()) {
            System.out.println(prop);
        }

        String configPath = System.getProperty("secureproxy.integration.config");

        System.out.println("Config read from: '" + configPath + "'");

        ProxyConfigurer configurer = new ProxyConfigurer(configPath);

        String publicUri = configurer.getProperty("publicURI");
        String publicHttpPort = configurer.getProperty("publicHttpPort");

        String url = publicUri + ":" + publicHttpPort + "/post-form.php";

        System.out.println("Fetching page: '" + url + "'");

        WebDriver driver = new HtmlUnitDriver();

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
