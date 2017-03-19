package it.akademija.representative;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Utilities;

public class SingleMemberPage {

	WebDriver driver;
	private int numberOfRows;
	private int numberOfVoters;
	private Utilities utilities;
	private RepresentativeViewPage pageRepresentativeView;
	private int number;
	private static String districtName;
	private static int numberOfPeopleWhoVoted;

	@FindBy(xpath = "//*[@id='side-menu']/li[3]/a")
	WebElement menuSingleMember;

	@FindBy(xpath = "//*[@id='basic-addon2']")
	List<WebElement> rows;

	@FindBy(xpath = "//form/button")
	WebElement buttonSubmit;

	@FindBy(className = "alert")
	public WebElement alertMessage;

	public SingleMemberPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		utilities = new Utilities(driver);
		pageRepresentativeView = new RepresentativeViewPage(driver);
	}

	public void fillOutResultRows() {
		numberOfVoters = pageRepresentativeView.getElectorsNumber();
		districtName = pageRepresentativeView.getDistrictName();
		menuSingleMember.click();
		numberOfRows = rows.size();
		for (int i = 1; i <= numberOfRows; i++) {
			number = utilities.getRandomNumber(numberOfVoters / numberOfRows);
			numberOfPeopleWhoVoted += number;
			String votesNumber = Integer.toString(number);
			driver.findElement(By.xpath(".//form/div[" + i + "]//input")).sendKeys(votesNumber);
		}
		buttonSubmit.click();
	}

	public int getTheNumberOfPeopleWhoVoted() {
		return numberOfPeopleWhoVoted;
	}

	public int getTheNumberOfVoters() {
		return numberOfVoters;
	}

	public String getDistrictName() {
		return districtName;
	}

}
