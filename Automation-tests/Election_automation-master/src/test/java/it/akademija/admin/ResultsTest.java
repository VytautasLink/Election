package it.akademija.admin;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import it.akademija.voting.VotingSystem;

public class ResultsTest extends VotingSystem {
	
	@Parameters({"usernameAdmin", "password"})
	@BeforeClass
	public void setUp(String usernameAdmin, String password){
		pageResults = new ResultsPage(driver);
		pageLogin.login(usernameAdmin, password);
	}
	
	@Parameters({"districtForVoting"})
	@Test (priority = 35)
	public void deleteResultsInSingleMemberTest(String districtForVoting){
		pageResults.deleteResultsInSingleMember(districtForVoting);
	}
	
	@Parameters({"districtForVoting"})
	@Test (priority = 36)
	public void deleteResultsInMultiMemberTest(String districtForVoting){
		pageResults.deleteResultsInMultiMember(districtForVoting);
	}

}
