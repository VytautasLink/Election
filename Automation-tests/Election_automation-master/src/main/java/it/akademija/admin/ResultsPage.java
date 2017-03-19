package it.akademija.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import it.akademija.representative.SingleMemberPage;
import utilities.Utilities;

public class ResultsPage {
	
	private WebDriver driver;
	private Utilities utilities;
	private SingleMemberPage pageSingleMember;
	private int districtRowToConfirm;
	public String xpathSingleMemebr = "//*[@id='results1']//tbody/tr";
	public String xpathMultiMemebr = "//*[@id='results2']//tbody/tr";
	
	@FindBy(id = "results-button")
	WebElement menuResults;

	@FindBy(xpath = "//*[@id='page-wrapper']//li[2]/a")
	WebElement buttonSingleMember;
	
	@FindBy(xpath = "//*[@id='page-wrapper']//li[3]/a")
	WebElement buttonMultiMember;
	
	@FindBy(id = "result-refresh")
	WebElement buttonRefresh;
	
	@FindBy(id = "alert-danger-fixed")
	WebElement alertMessage;
	
	@FindBy(id = "alert-success-fixed")
	public WebElement successMessage;
	
	public ResultsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
		pageSingleMember = new SingleMemberPage(driver);
	}
	
	public void deleteResultsInSingleMember(String district) {
		utilities.waitForJavascript();
		menuResults.click();
		buttonSingleMember.click();
		districtRowToConfirm = utilities.findElementForConfirmingResults(xpathSingleMemebr, district);
		driver.findElement(By.xpath("//*[@id='results1']//tbody/tr[" + districtRowToConfirm + "]//*[contains(@id, 'delete-button')]")).click();
		utilities.waitToLoad("//*[contains(@id, 'confirmationModalSingle')]//div[3]//button[1]").click();
		utilities.waitToLoad("//*[@id='alert-danger-fixed']");
		Assert.assertTrue(alertMessage.getText().contains(district + " balsai atmesti."));
	}
	
	public void deleteResultsInMultiMember(String district) {
		utilities.waitForJavascript();
		menuResults.click();
		buttonMultiMember.click();
		districtRowToConfirm = utilities.findElementForConfirmingResults(xpathMultiMemebr, district);
		driver.findElement(By.xpath("//*[@id='results2']//tbody/tr[" + districtRowToConfirm + "]//*[contains(@id, 'delete-button')]")).click();
		utilities.waitToLoad("//*[contains(@id, 'confirmationModalundefined')]//div[3]//button[1]").click();
		utilities.waitToLoad("//*[@id='alert-danger-fixed']");
		Assert.assertTrue(alertMessage.getText().contains(district + " balsai atmesti."));
	}

	public void confirmResultsInSingleMember() {
		utilities.waitForJavascript();
		menuResults.click();
		buttonSingleMember.click();
		buttonRefresh.click();
		districtRowToConfirm = utilities.findElementForConfirmingResults(xpathSingleMemebr, pageSingleMember.getDistrictName());
		driver.findElement(By.xpath("//*[@id='results1']//tbody/tr[" + districtRowToConfirm + "]//*[contains(@id, 'confirm-button')]")).click();
		utilities.waitToLoad("//*[@id = 'alert-success-fixed']");
	}
	
	public void confirmResultsInMultiMember() {
		buttonMultiMember.click();
		buttonRefresh.click();
		districtRowToConfirm = utilities.findElementForConfirmingResults(xpathMultiMemebr, pageSingleMember.getDistrictName());
		driver.findElement(By.xpath("//*[@id='results2']//tbody/tr[" + districtRowToConfirm + "]//*[contains(@id, 'confirm-button')]")).click();
		utilities.waitToLoad("//*[@id = 'alert-success-fixed']");
	}
	
}
