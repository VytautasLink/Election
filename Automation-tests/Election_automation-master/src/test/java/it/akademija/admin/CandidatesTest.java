package it.akademija.admin;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import it.akademija.voting.VotingSystem;

public class CandidatesTest extends VotingSystem {
	
	private CandidatesPage pageCandidates;
	
	@Parameters({ "usernameAdmin", "password"})
	@BeforeClass
	public void setUp(String usernameAdmin, String password){
		pageCandidates = new CandidatesPage(driver);
		pageLogin.login(usernameAdmin, password);
	}
	
	@Parameters({"candidateNumber", "candidateName", "candidateSurname", "candidateBirthDate", "newCandidateName", "newCandidateSurname", "newCandidateBirthDate", "newCandidateInfo", "newCandidateNumber"})
	@Test(priority = 23)
	public void editNameAndSurnameTest(String candidateNumber, String candidateName, String candidateSurname, String candidateBirthDate, String newCandidateName, String newCandidateSurname, String newCandidateBirthDate, String newCandidateInfo, String newCandidateNumber) {
		pageCandidates.editNameAndSurname(candidateNumber, candidateName, candidateSurname, candidateBirthDate, newCandidateName, newCandidateSurname, newCandidateBirthDate, newCandidateInfo, newCandidateNumber);
		pageCandidates.alert.getText().equals("Kandidatas "+ newCandidateName + " " + newCandidateSurname + " atnaujintas");
	}

}
