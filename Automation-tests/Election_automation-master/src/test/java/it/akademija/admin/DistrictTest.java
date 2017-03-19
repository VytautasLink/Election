package it.akademija.admin;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import it.akademija.voting.VotingSystem;

public class DistrictTest extends VotingSystem{
	
	private int numberOfTimesTestWasRan = 0;
	
	@Parameters({"usernameAdmin", "password"})
	@BeforeClass
	public void setUp(String usernameAdmin, String password){
		pageDistrict = new DistrictPage(driver);
		pageLogin.login(usernameAdmin, password);
	}
	
	/**
	 * TC06 and TC15
	 */
	@Parameters({"districtName", "districtAddress", "districtVoter", "newCountyName"})
	@Test (priority = 10, invocationCount = 2, enabled = true)
	public void registerDistrictTest(String districtName, String districtAddress, String districtVoter, String newCountyName){
		pageDistrict.registerDistrict(districtName, districtAddress, districtVoter, newCountyName, "");
		if (numberOfTimesTestWasRan == 0){
			Assert.assertTrue(pageDistrict.alert.getText().contains("Apylinkė " + districtName +" sukurta"), "District wasn't succesfully created.");
		} else {
			Assert.assertTrue(pageDistrict.alert.getText().contains("Apylinkė su pavadinimu " + districtName + " jau priskirta šitai apygardai"));
		}
		numberOfTimesTestWasRan++;
	}
	
	/**
	 * TC16
	 */
	@Parameters({"districtName", "districtAddress", "districtVoter", "countyName2"})
	@Test (priority = 11, enabled = true)
	public void registerExistingDistrictAndAssigntToDifferentCountyTest(String districtName, String districtAddress, String districtVoter, String countyName2){
		pageDistrict.registerDistrict(districtName, districtAddress, districtVoter, countyName2, "");
		Assert.assertTrue(pageDistrict.alert.getText().contains("Apylinkė " + districtName +" sukurta"));
	}
	
	@Parameters({"districtFile", "districtAddressFile", "districtVotersFile", "newCountyName"})
	@Test(priority = 11, enabled = true)
	public void registerMultipleDistrictsToOneCountyTest(String districtFile, String districtAddressFile, String districtVotersFile, String newCountyName) throws IOException {
		pageDistrict.registerMultipleDistricts(districtFile, districtAddressFile, districtVotersFile, newCountyName, false);
	}
	
	@Parameters({"variousDistrictsFile", "variousDistrictsAddressFile", "variousDistrictsVotersFile", "countyFile"})
	@Test(priority = 11, enabled = true)
	public void registerMultipleDistrictsToDifferentCountiesTest(String variousDistrictsFile, String variousDistrictsAddressFile, String variousDistrictsVotersFile, String countyFile) throws IOException {
		pageDistrict.registerMultipleDistricts(variousDistrictsFile, variousDistrictsAddressFile, variousDistrictsVotersFile, countyFile, true);
	}
	
	@Parameters({"districtNameToEdit", "newDistrictName", "newDistrictAddress", "newDistrictVoter", "newCountyName"})
	@Test(priority = 12, enabled = true)
	public void editDistrictTest(String districtNameToEdit, String newDistrictName, String newDistrictAddress, String newDistrictVoter, String newCountyName){
		pageDistrict.editDistrict(districtNameToEdit, newDistrictName, newDistrictAddress, newDistrictVoter, newCountyName);
		Assert.assertTrue(pageDistrict.alert.getText().contains("Apylinkė " + newDistrictName + " atnaujinta"));
	}
	
	@Parameters({"districtNameToDelete"})
	@Test(priority = 13, enabled = true)
	public void deleteDistrictTest(String districtNameToDelete){
		pageDistrict.deleteDistrict(districtNameToDelete);
		Assert.assertTrue(pageDistrict.alert.getText().contains("Apylinkė " + districtNameToDelete + " ištrinta."));
	}
	
}
