package it.akademija.representative;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RepresentativeViewPage {

	WebDriver driver;
	private String number;
	private int numberOfVoters;
	public String districtName;

	@FindBy(xpath = "//*[@id='side-menu']/li[2]/a")
	WebElement menuRepresentativePanel;

	@FindBy(xpath = "//*[@id='page-wrapper']//li[5]/span")
	WebElement electorsNumber;

	@FindBy(xpath = "//*[@id='page-wrapper']//li[3]/span")
	WebElement textDistrictName;

	public RepresentativeViewPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public int getElectorsNumber() {
		menuRepresentativePanel.click();
		number = electorsNumber.getText();
		numberOfVoters = Integer.parseInt(number);
		return numberOfVoters;
	}

	protected String getDistrictName() {
		menuRepresentativePanel.click();
		districtName = textDistrictName.getText();
		return districtName;
	}

}
