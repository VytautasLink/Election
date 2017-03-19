package it.akademija.admin;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import it.akademija.voting.VotingSystem;

public class PartyTest extends VotingSystem{
	
	private int numberOfTimesTestWasRan = 0;

	@Parameters({"usernameAdmin", "password"})
	@BeforeClass
	public void setUp(String usernameAdmin, String password){
		pageParty = new PartyPage(driver);
		pageLogin.login(usernameAdmin, password);
	}
	
	/**
	 * TC08 and TC09
	 */
	@Parameters({"partyName", "partyNumber", "partyCsvFile"})
	@Test (priority = 18, invocationCount = 2, enabled = true)
	public void registerPartyTest(String partyName, String partyNumber,
			String partyCsvFile){
		pageParty.registerParty(partyName, partyNumber, partyCsvFile, "");
		if (numberOfTimesTestWasRan == 0) {
			Assert.assertTrue(pageParty.alert.getText().contains(partyName + " sukurta"));
		} else {
			Assert.assertTrue(pageParty.alert.getText().contains("Partija su tokiu pavadinimu jau užregistruota"));
		}
		numberOfTimesTestWasRan++;
	}
	
	/**
	 * TC42
	 */
	@Parameters({"partyName2", "partyNumber2", "partyCsvFile"})
	@Test (priority = 18, enabled = true)
	public void registerPartyTest2(String partyName2, String partyNumber2,
			String partyCsvFile){
		pageParty.registerParty(partyName2, partyNumber2, partyCsvFile, "");
		Assert.assertTrue(pageParty.alert.getText().contains(partyName2 + " sukurta"));
	}
	
	@Parameters({"partyName2", "partyNumber2", "partyFile"})
	@Test (priority = 17, enabled = true)
	public void registerPartyWithOtherFileTest(String partyName2, String partyNumber2,
			String partyFile){
		pageParty.registerParty(partyName2, partyNumber2, partyFile, "");
		Assert.assertTrue(pageParty.alert.getText().contains("Būtina prisegti *.csv formato failą"));
	}
	
	/**
	 * TC43
	 */
	@Parameters({"partyName2", "partyNumber2", "noFile"})
	@Test (priority = 17, enabled = true)
	public void registerPartyWithoutFileTest(String partyName2, String partyNumber2,
			String noFile){
		pageParty.registerParty(partyName2, partyNumber2, noFile, "");
		Assert.assertTrue(pageParty.alert.getText().contains("Būtina prisegti *.csv formato failą"));
	}
	
	@Parameters({"partyName", "newPartyName", "newPartyNumber"})
	@Test(priority = 19, enabled = true)
	public void editPartyTest(String partyName, String newPartyName, String newPartyNumber){
		pageParty.editParty(partyName, newPartyName, newPartyNumber, "");
		Assert.assertTrue(pageParty.alert.getText().contains("Partija " + newPartyName + " atnaujinta"));
	}

	@Parameters({"newPartyName"})
	@Test(priority = 20, enabled = true)
	public void deletePartyTest(String newPartyName){
		pageParty.deleteParty(newPartyName);
		
	}
	
	@Parameters({"partyName2"})
	@Test(priority = 21, enabled = true)
	public void deleteCandidatesTest(String partyName2){
		pageParty.deleteCandidates(partyName2);
	}
	
}