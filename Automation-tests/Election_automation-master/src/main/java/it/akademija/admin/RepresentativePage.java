package it.akademija.admin;

import java.io.IOException;
import java.util.ArrayList;
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

public class RepresentativePage {

	WebDriver driver;
	private Utilities utilities;
	private String[] alertLine;
	private static List<String> representativeUsernames = new ArrayList<>();
	private static List<String> representativePasswords = new ArrayList<>();
	private Select dropdownDistrict;
	private List<String> representativeName;
	private List<String> representativeSurname;
	private List<String> district;
	private Iterator<String> representativeNameIterator;
	private Iterator<String> representativeSurnameIterator;
	private Iterator<String> districtIterator;
	private String name;
	private String surname;

	@FindBy(id = "representative-button")
	WebElement menuRepresentatives;

	@FindBy(id = "register-button")
	WebElement buttonRegister;

	@FindBy(id = "name-input")
	WebElement fieldRepresentativeName;

	@FindBy(id = "surname-input")
	WebElement fieldRepresentiveSurname;

	@FindBy(id = "district-select")
	WebElement dropDownListDistricts;

	@FindBy(id = "create-button")
	WebElement buttonSubmit;

	@FindBy(className = "alert")
	public WebElement alert;

	public RepresentativePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
	}

	public void registerDistrictRepresentative(String name, String surname, String district) {
		menuRepresentatives.click();
		buttonRegister.click();
		fieldRepresentativeName.sendKeys(name);
		fieldRepresentiveSurname.sendKeys(surname);
		dropdownDistrict = new Select(dropDownListDistricts);
		dropdownDistrict.selectByVisibleText(district);
		buttonSubmit.click();
		utilities.waitToLoad("//*[@id='alert-success-fixed']");
		utilities.waitToLoad("//*[@id='register-button']");
		getRepresentativesLoginAndPassword();
	}

	protected void registerMultipleDistrictRepresentative(String representativeNamesFile,
			String representativeSurnamesFile, String districtFile) throws IOException {
		DataReader dReader = new DataReader();
		representativeName = dReader.getTestData(representativeNamesFile);
		representativeSurname = dReader.getTestData(representativeSurnamesFile);
		district = dReader.getTestData(districtFile);
		representativeNameIterator = representativeName.iterator();
		representativeSurnameIterator = representativeSurname.iterator();
		districtIterator = district.iterator();
		while (representativeNameIterator.hasNext() && representativeSurnameIterator.hasNext()
				&& districtIterator.hasNext()) {
			name = representativeNameIterator.next();
			surname = representativeSurnameIterator.next();
			registerDistrictRepresentative(name, surname, districtIterator.next());
			utilities.waitToLoad("//*[@id='register-button']");
			Assert.assertTrue(alert.getText().contains("Apylinkës atstovas " + name + " " + surname + " sukurtas."));
		}
	}

	protected void getRepresentativesLoginAndPassword() {
		alertLine = driver.findElement(By.xpath("//div[contains(text(),'Vartotojo prisijungimo vardas:')]")).getText()
				.split(" ");
		representativeUsernames.add(alertLine[3]);
		representativePasswords.add(alertLine[5]);
	}

	public String getRepresentativeUsername(int index) {
		return representativeUsernames.get(index);
	}

	public String getRepresentativePassword(int index) {
		return representativePasswords.get(index);
	}
}
