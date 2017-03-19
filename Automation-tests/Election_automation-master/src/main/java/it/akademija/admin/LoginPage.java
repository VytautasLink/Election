package it.akademija.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import utilities.Utilities;

public class LoginPage {

	WebDriver driver;
	private Utilities utilities;

	@FindBy(id = "login-input")
	WebElement fieldUser;

	@FindBy(id = "password-input")
	WebElement fieldPassword;

	@FindBy(id = "login-button")
	WebElement buttonLogin;

	@FindBy(linkText = "Pradinis")
	WebElement textPradinis;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
	}

	public void login(String username, String password) {
		fieldUser.clear();
		fieldUser.sendKeys(username);
		fieldPassword.clear();
		fieldPassword.sendKeys(password);
		buttonLogin.click();
	}

	public void logout() {
		utilities.waitToLoad("//*[@id='logout-button']").click();
		Assert.assertTrue(textPradinis.isDisplayed());
	}
}
