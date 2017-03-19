package it.akademija.admin;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import utilities.DataReader;
import utilities.Utilities;

public class DistrictPage {

	private WebDriver driver;
	private int districtRow;
	private Utilities utilities;
	private String firstLetter;

	@FindBy(id = "district-button")
	WebElement menuDistricts;

	@FindBy(id = "register-button")
	WebElement buttonRegister;

	@FindBy(id = "name-input")
	WebElement fieldDistrictName;

	@FindBy(id = "adress-input")
	WebElement fieldAddress;

	@FindBy(id = "voters-input")
	WebElement fieldVoters;

	@FindBy(id = "county-district-select")
	WebElement dropDownListCounties;

	@FindBy(id = "create-button")
	WebElement buttonSubmit;

	@FindBy(className = "alert")
	public WebElement alert;

	@FindBy(xpath = "//*[contains(@id, 'confirm-delete-button')]")
	WebElement buttonDelete;

	public DistrictPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
	}

	private Select dropdownCounty;
	private List<String> districtName;
	private List<String> districtAddress;
	private List<String> districtVoters;
	private List<String> countyName;
	private Iterator<String> districtAddressIterator;
	private Iterator<String> districtVotersIterator;
	private Iterator<String> countyNameIterator;
	private Iterator<String> districtNameIterator;

	public void registerDistrict(String districtName, String districtAddress, String districtVoters, String county,
			String enterOrEdit) {
		if (!enterOrEdit.equals("edit")) {
			menuDistricts.click();
			buttonRegister.click();
		} else {
			clearTheField(fieldDistrictName);
			clearTheField(fieldAddress);
			utilities.waitForJavascript();
		}
		fieldDistrictName.sendKeys(districtName);
		if (enterOrEdit.equals("edit")) {
			clearTheField(fieldAddress);
		}
		fieldAddress.sendKeys(districtAddress);
		if (enterOrEdit.equals("edit")) {
			clearTheField(fieldVoters);
		}
		fieldVoters.sendKeys(districtVoters);
		dropdownCounty = new Select(dropDownListCounties);
		dropdownCounty.selectByVisibleText(county);
		buttonSubmit.click();
	}

	protected void registerMultipleDistricts(String districtFile, String districtAddressFile, String districtVotersFile,
			String county, boolean isCountyPlural) throws IOException {
		DataReader dReader = new DataReader();
		districtName = dReader.getTestData(districtFile);
		districtAddress = dReader.getTestData(districtAddressFile);
		districtVoters = dReader.getTestData(districtVotersFile);
		districtNameIterator = districtName.iterator();
		districtAddressIterator = districtAddress.iterator();
		districtVotersIterator = districtVoters.iterator();
		if (isCountyPlural) {
			countyName = dReader.getTestData(county);
			countyNameIterator = countyName.iterator();
		}
		while (districtNameIterator.hasNext() && districtAddressIterator.hasNext()
				&& districtVotersIterator.hasNext()) {
			String district = districtNameIterator.next();
			if (isCountyPlural) {
				registerDistrict(district, districtAddressIterator.next(), districtVotersIterator.next(),
						countyNameIterator.next(), "");
			} else {
				registerDistrict(district, districtAddressIterator.next(), districtVotersIterator.next(), county, "");
			}
			utilities.waitToLoad("//*[@id='register-button']");
			Assert.assertTrue(alert.getText().contains("Apylinkë " + district + " sukurta"));
		}
	}

	protected void editDistrict(String districtName, String newDistrictName, String newDistrictAddress,
			String newDistrictVoters, String newCounty) {
		menuDistricts.click();
		firstLetter = districtName.toLowerCase().substring(0, 1);
		districtRow = utilities.findElementForDeletingAndEditing(driver.findElement(By.linkText(firstLetter)),
				districtName, "district");
		driver.findElement(By.xpath("//tbody/tr[" + districtRow + "]//a[1]")).click();
		registerDistrict(newDistrictName, newDistrictAddress, newDistrictVoters, newCounty, "edit");
	}

	protected void clearTheField(WebElement fieldName) {
		fieldName.clear();
		utilities.waitForJavascript();
	}

	public void deleteDistrict(String districtName) {
		firstLetter = districtName.toLowerCase().substring(0, 1);
		districtRow = utilities.findElementForDeletingAndEditing(driver.findElement(By.linkText(firstLetter)),
				districtName, "district");
		buttonDelete.click();
		utilities.waitToLoad("//*[starts-with(@id, 'delete-district')]").click();
	}

}
