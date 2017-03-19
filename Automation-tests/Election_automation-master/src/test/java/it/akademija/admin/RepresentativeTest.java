package it.akademija.admin;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import it.akademija.voting.VotingSystem;

public class RepresentativeTest extends VotingSystem{

	@Parameters({"usernameAdmin", "password"})
	@BeforeClass
	public void setUp(String usernameAdmin, String password){
		pageRepresentative = new RepresentativePage(driver);
		pageLogin.login(usernameAdmin, password);
	}
	
	/**
	 * TC07
	 */
	@Parameters({"representativeName", "representativeSurname", "districtName"})
	@Test (priority = 15, enabled = true)
	public void registerSingleRepresentativeTest(String representativeName, String representativeSurname, String districtName){
		pageRepresentative.registerDistrictRepresentative(representativeName, representativeSurname, districtName);
		Assert.assertTrue(pageRepresentative.alert.getText().contains("Apylinkës atstovas " + representativeName + " " + representativeSurname + " sukurtas."));
	}
	
	@Parameters({"representativeNamesFile", "representativeSurnamesFile", "variousDistrictsFile"})
	@Test (priority = 15, enabled = true)
	public void registerMultipleRepresentativesTest(String representativeNamesFile, String representativeSurnamesFile, String variousDistrictsFile) throws IOException{
		pageRepresentative.registerMultipleDistrictRepresentative(representativeNamesFile, representativeSurnamesFile, variousDistrictsFile);
	}

}
