package com.secretkeeper.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class signInTest {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "chromedriver-win64/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("chrome-win64/chrome.exe");

        // Create an instance of WebDriver
        WebDriver driver = new ChromeDriver(chromeOptions);

        // Navigate to a website
        driver.get("http://localhost:3000/");

        WebElement emailInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement signInButton = driver.findElement(By.name("submit"));

        emailInput.sendKeys("testuser1@gmail.com");
        passwordInput.sendKeys("test123");

        signInButton.click();

        driver.quit();
    }
}
