/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.blackbox.integration;

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
    public void PostTest() {
        WebDriver driver = new HtmlUnitDriver();
        
        driver.get("http://localhost:8084/post-form.php");
        
        System.out.println("Page title is: " + driver.getTitle());
        
        WebElement element = driver.findElement(By.name("input"));
        
        String testInput = "testi";
        
        element.sendKeys(testInput);
        
        element.submit();
        
        System.out.println("Page title is: " + driver.getTitle());
        
        assertEquals(testInput, driver.getTitle());
    }
    
    
}
