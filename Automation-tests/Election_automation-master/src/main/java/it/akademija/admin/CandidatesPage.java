package it.akademija.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Utilities;

public class CandidatesPage {

	WebDriver driver;
	private Utilities utilities;

	@FindBy(id = "candidates-button")
	WebElement menuCandidates;

	@FindBy(xpath = "//form/div[1]/input[1]")
	WebElement candidateName;

	@FindBy(xpath = "//form/div[2]/input[1]")
	WebElement candidateSurname;

	@FindBy(xpath = "//form/div[3]/input[1]")
	WebElement candidateBirthDate;

	@FindBy(xpath = "//form/div[4]/input[1]")
	WebElement candidateInfo;

	@FindBy(xpath = "//form/div[5]/input[1]")
	WebElement candidateNumber;

	@FindBy(xpath = "//form//button")
	WebElement buttonSubmit;

	@FindBy(className = "alert")
	WebElement alert;

	@FindBy(xpath = "//*[contains(@id, 'edit-button')]")
	WebElement buttonEdit;

	public CandidatesPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
	}

	protected void editNameAndSurname(String number, String name, String surname, String birthDate, String newName,
			String newSurname, String newBirthDate, String newInfo, String newNumber) {
		menuCandidates.click();
		utilities.waitToLoad("//*[@id='searchable-table_filter']/label/input")
				.sendKeys(number + " " + name + " " + surname + " " + birthDate);
		buttonEdit.click();
		enterNewInformationForCandidate(newName, newSurname, newBirthDate, newInfo, newNumber);
		buttonSubmit.click();
	}

	protected void enterNewInformationForCandidate(String name, String surname, String birthDate, String info,
			String number) {
		candidateName.clear();
		candidateName.sendKeys(name);
		candidateSurname.clear();
		candidateSurname.sendKeys(surname);
		candidateBirthDate.clear();
		candidateBirthDate.sendKeys(birthDate);
		candidateInfo.clear();
		candidateInfo.sendKeys(info);
		candidateNumber.clear();
		candidateNumber.sendKeys(number);
	}

}
