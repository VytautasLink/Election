package it.akademija.admin;

import org.testng.Assert;
import org.testng.annotations.Test;
import it.akademija.voting.VotingSystem;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class CountyTest extends VotingSystem{

	private int numberOfTimesTestWasRan = 0;
	
	@Parameters({"usernameAdmin", "password"})
	@BeforeClass
	public void setUp(String usernameAdmin, String password){
		pageCounty = new CountyPage(driver);
		pageLogin.login(usernameAdmin, password);
	}
	
	@Parameters({ "countyFile" })
	@Test(priority = 3, enabled = true)
	public void registerMultipleCountiesTest(String countyFile) throws IOException {
		pageCounty.registerMultipleCounties(countyFile);
	}

	/**
	 * TC05 and TC10
	 */
	@Parameters({ "countyName" })
	@Test(priority = 3, invocationCount = 2, enabled = true)
	public void registerCountyTest(String countyName) {
		pageCounty.registerCounty(countyName, "");
		if (numberOfTimesTestWasRan == 0) {
			Assert.assertTrue(pageCounty.alert.getText().contains("Apygarda " + countyName + " sukurta"));
		} else {
			Assert.assertTrue(pageCounty.alert.getText().contains("Apygarda su tokiu pavadinimu jau užregistruota."));
		}
		numberOfTimesTestWasRan++;
	}

	@Parameters({ "countyToDelete" })
	@Test(priority = 5, enabled = true)
	public void deleteCountyTest(String countyToDelete) {
		pageCounty.deleteCounty(countyToDelete);
		Assert.assertTrue(
				pageCounty.alert.getText().contains("Apygarda " + countyToDelete + " ištrinta"));
	}

	@Parameters({ "countyName", "newCountyName" })
	@Test(priority = 4, enabled = true, groups = {"county-name"})
	public void editCountyTest(String countyName, String newCountyName) {
		pageCounty.editCounty(countyName, newCountyName);
		Assert.assertTrue(
				pageCounty.alert.getText().contains("Apygardos pavadinimas pakeistas iš " + countyName + " į " + newCountyName));
	}

	@Parameters({ "countyNameForVoting", "candidatesList" })
	@Test(priority = 6, enabled = true)
	public void addCandidatesListTest(String countyNameForVoting, String candidatesList) {
		pageCounty.addCandidatesList(countyNameForVoting, candidatesList);
	}
	
}
