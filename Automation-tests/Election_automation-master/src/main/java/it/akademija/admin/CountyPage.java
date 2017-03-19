package it.akademija.admin;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import utilities.DataReader;
import utilities.Utilities;

public class CountyPage {

	private WebDriver driver;
	private Utilities utilities;
	private List<String> county;
	private DataReader dReader;
	private int countyRow;
	private WebElement buttonToUploadFile;
	private WebElement alertSuccessMessage;

	@FindBy(id = "county-button")
	public WebElement menuCounty;

	@FindBy(id = "register-button")
	WebElement buttonRegister;

	@FindBy(id = "name-input")
	WebElement fieldCountyName;

	@FindBy(id = "create-button")
	WebElement buttonSubmit;

	@FindBy(className = "alert")
	public WebElement alert;

	public CountyPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
	}

	public void registerCounty(String countyName, String enterOrEdit) {
		if (!enterOrEdit.equals("edit")) {
			menuCounty.click();
			buttonRegister.click();
		}
		fieldCountyName.sendKeys(countyName);
		buttonSubmit.click();
	}

	protected void registerMultipleCounties(String countyFile) throws IOException {
		dReader = new DataReader();
		county = dReader.getTestData(countyFile);
		for (String item : county) {
			registerCounty(item, "");
			Assert.assertTrue(alert.getText().contains("Apygarda " + item + " sukurta"));
		}
	}

	public void deleteCounty(String countyName) {
		countyRow = utilities.findElementForDeletingAndEditing(menuCounty, countyName, "county");
		if (countyRow != 0) {
			utilities.waitToLoad("//tbody/tr[" + countyRow + "]/td[2]/a[3]").click();
			utilities.waitToLoad("//tr[" + countyRow + "]//button[contains(@id,'delete-button')]").click();
			utilities.waitToLoad("//*[@id='register-button']");
			Assert.assertTrue(alert.getText().contains("Apygarda " + countyName + " ištrinta"));
		}
	}

	public void deleteMultipleCounties(String countyFile) throws IOException {
		dReader = new DataReader();
		county = dReader.getTestData(countyFile);
		for (String item : county) {
			deleteCounty(item);
		}
	}

	protected void editCounty(String countyName, String newCountyName) {
		countyRow = utilities.findElementForDeletingAndEditing(menuCounty, countyName, "county");
		driver.findElement(By.xpath("//tbody/tr[" + countyRow + "]/td[2]/a[2]")).click();
		fieldCountyName.clear();
		registerCounty(newCountyName, "edit");
		utilities.waitToLoad("//*[@id='alert-success-fixed']");
	}

	public void addCandidatesList(String countyName, String candidatesList) {
		utilities.waitToLoad("//*[@id='register-button']");
		countyRow = utilities.findElementForDeletingAndEditing(menuCounty, countyName, "county");
		driver.findElement(By.xpath("//tbody/tr[" + countyRow + "]/td[2]/button[1]")).click();
		buttonToUploadFile = driver.findElement(By.xpath("//tbody/tr[" + countyRow + "]//input"));
		utilities.attachFile(buttonToUploadFile, candidatesList);
		driver.findElement(By.xpath("//tr[" + countyRow + "]//button[contains(@id, 'add-county-single-list')]"))
				.click();
		alertSuccessMessage = driver.findElement(By.xpath("//tr[" + countyRow + "]//*[@id='alert-success']"));
		utilities.waitForJavascript();
		Assert.assertTrue(alertSuccessMessage.getText().contains("Apygardai sėkmingai įkeltas"));
		utilities.waitForJavascript();
		driver.findElement(By.xpath("//tr[" + countyRow + "]//td[2]/div[1]//*[@id='modal-close-button']")).click();
		utilities.waitToLoad("//*[@id='logout-button']");
		utilities.waitForJavascript();
	}

}