package utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.base.Function;

public class Utilities {

	WebDriver driver;
	private int numberOfRows;
	private String searchingName;
	private String searchingSurname;
	private String searchingBirthDay;
	private String searchingPartyName;
	private int randomInt;
	private WebDriverWait wait;
	private WebElement element;
	private List<String> list;
	private Random rand = new Random();

	@FindBy(xpath = "//tbody/tr")
	List<WebElement> rows;

	public Utilities(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	public void waitForJavascript() {
		new WebDriverWait(driver, 100).until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
			}
		});
	}

	public WebElement waitToLoad(String path) {
		wait = new WebDriverWait(driver, 100);
		element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
		return element;
	}

	public int findElementForDeletingAndEditing(WebElement menu, String name, String elementForSearching) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		menu.click();
		waitToLoad("//*[@id='register-button']");
		numberOfRows = rows.size();
		for (int i = 1; i <= numberOfRows; i++) {
			if (!elementForSearching.equals("party")) {
				searchingName = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[1]")).getText();
			} else {
				searchingName = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[2]")).getText();
			}
			if (searchingName.equals(name)) {
				return i;
			}
		}
		return 0;
	}

	public int findElementForConfirmingResults(String xpath, String name) {
		numberOfRows = driver.findElements(By.xpath(xpath)).size();
		for (int i = 1; i <= numberOfRows; i++) {
			searchingName = driver.findElement(By.xpath(xpath + "[" + i + "]/td[1]")).getText();
			if (searchingName.equals(name)) {
				return i;
			}
		}
		return 0;
	}

	public int findElementForEditingCandidate(String name, String surname, String birthDay, String partyName) {
		numberOfRows = rows.size();
		for (int i = numberOfRows; i >= 1; i--) {
			searchingSurname = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[3]")).getText();
			if (searchingSurname.equals(surname)) {
				searchingName = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[2]")).getText();
				searchingBirthDay = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[4]")).getText();
				searchingPartyName = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[5]")).getText();
				if (searchingName.equals(name) && searchingBirthDay.equals(birthDay)
						&& searchingPartyName.equals(partyName)) {
					return i;
				}
			}
		}
		return 0;
	}

	public Utilities attachFile(WebElement button, String filePath) {
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(classLoader.getResource(filePath).getPath());
		button.sendKeys(file.getPath());
		return this;
	}

	public int getRandomNumber(int number) {
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(number);
		return randomInt;
	}

	public String getRandomLine(List<String> list) {
		rand = new Random();
		String randomInt = list.get(rand.nextInt(list.size()));
		return randomInt;
	}

	public List<String> putTextFromFileToAList(String file) throws IOException {
		DataReader dReader = new DataReader();
		list = dReader.getTestData(file);
		return list;
	}

	public void dropDownListRandom(String id) {
		Select countyDistrictSelect = new Select(driver.findElement(By.id(id)));
		List<WebElement> weblist = countyDistrictSelect.getOptions();
		int optionsCount = weblist.size();
		Random num = new Random();
		int randomSelect = num.nextInt(optionsCount);
		countyDistrictSelect.selectByIndex(randomSelect);
	}

}
