package it.akademija.voting;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import it.akademija.admin.CountyPage;
import it.akademija.admin.DistrictPage;
import it.akademija.admin.LoginPage;
import it.akademija.admin.PartyPage;
import it.akademija.admin.RepresentativePage;
import it.akademija.admin.ResultsPage;
import it.akademija.representative.MultiMemberPage;
import it.akademija.representative.RepresentativeViewPage;
import it.akademija.representative.SingleMemberPage;
import utilities.Utilities;

public abstract class VotingSystem {
	
	protected WebDriver driver;
	protected LoginPage pageLogin;
	protected CountyPage pageCounty;
	protected PartyPage pageParty;
	protected DistrictPage pageDistrict;
	protected RepresentativePage pageRepresentative;
	protected RepresentativeViewPage pageRepresentativeView;
	protected SingleMemberPage pageSingleMember;
	protected MultiMemberPage pageMultiMember;
	protected ResultsPage pageResults;
	protected Utilities utilities;

	@Parameters({"browser", "usernameAdmin", "password", "loginLink"})
	@BeforeClass
	public void settingUp(String browser, String usernameAdmin, String password, String loginLink) throws Exception {
		if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		} else if (browser.equalsIgnoreCase("opera")){
			System.setProperty("webdriver.opera.driver", "operadriver.exe");
			driver = new OperaDriver();
		}else {
			throw new Exception("Browser is not correct");
		}
		driver.manage().window().maximize();	
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(loginLink);
		pageLogin = new LoginPage(driver);
	}
	
	@AfterClass
	public void endingTestActivities() {
		driver.close();
	}

}
