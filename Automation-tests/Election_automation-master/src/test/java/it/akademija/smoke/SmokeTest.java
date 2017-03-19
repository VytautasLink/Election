package it.akademija.smoke;

import java.io.IOException;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import it.akademija.admin.CountyPage;
import it.akademija.admin.DistrictPage;
import it.akademija.admin.PartyPage;
import it.akademija.admin.RepresentativePage;
import it.akademija.admin.ResultsPage;
import it.akademija.representative.MultiMemberPage;
import it.akademija.representative.RepresentativeViewPage;
import it.akademija.representative.SingleMemberPage;
import it.akademija.voting.VotingSystem;
import utilities.Utilities;

/**
 * Smoke test
 * @author User
 *
 */
public class SmokeTest extends VotingSystem {

	private List<String> listCounty;
	private String county;
	private List<String> listDistrict;
	private String district;
	private List<String> listParty;
	private String party;
	
	@BeforeClass
	public void setUp() {
		pageCounty = new CountyPage(driver);
		pageDistrict = new DistrictPage(driver);
		pageRepresentative = new RepresentativePage(driver);
		pageParty = new PartyPage(driver);
		pageRepresentativeView  = new RepresentativeViewPage(driver);
		pageSingleMember = new SingleMemberPage(driver);
		pageMultiMember = new MultiMemberPage(driver);
		pageResults = new ResultsPage(driver);
		utilities = new Utilities(driver);
	}

	/**
	 * Administrator logins to the voting system
	 * @param usernameAdmin
	 * @param password
	 */
	@Parameters({ "usernameAdmin", "password" })
	@Test(priority = 1, enabled = true)
	public void loginAdministrator(String usernameAdmin, String password) {
		pageLogin.login(usernameAdmin, password);

	}

	/**
	 * Administrator registers county
	 * @param countyRandom
	 * @throws IOException
	 */
	@Parameters({ "countyRandom" })
	@Test(priority = 2, enabled = true)
	public void registerCounty(String countyRandom) throws IOException {
		listCounty = utilities.putTextFromFileToAList(countyRandom);
		county = utilities.getRandomLine(listCounty);
		pageCounty.registerCounty(county, "");
		Assert.assertTrue(pageCounty.alert.getText().contains("Apygarda " + county + " sukurta"));
	}

	/**
	 * Adminstrator registers district
	 * @param districtRandom
	 * @param districtAddress
	 * @throws IOException
	 */
	@Parameters({ "districtRandom", "districtAddress" })
	@Test(priority = 3, enabled = true)
	public void registerDistrict(String districtRandom, String districtAddress) throws IOException {
		listDistrict = utilities.putTextFromFileToAList(districtRandom);
		district = utilities.getRandomLine(listDistrict);
		pageDistrict.registerDistrict(district, districtAddress, Integer.toString(utilities.getRandomNumber(10000)), county, "");
		Assert.assertTrue(pageDistrict.alert.getText().contains("Apylinkė " + district +" sukurta"));
	}

	/**
	 * Administrator registers district's representative
	 * @param representativeName
	 * @param representativeSurname
	 */
	@Parameters({ "representativeName", "representativeSurname" })
	@Test(priority = 4, enabled = true)
	public void registerRepresentative(String representativeName, String representativeSurname) {
		pageRepresentative.registerDistrictRepresentative(representativeName, representativeSurname, district);
		Assert.assertTrue(pageRepresentative.alert.getText()
				.contains("Apylinkės atstovas " + representativeName + " " + representativeSurname + " sukurtas."));
	}

	/**
	 * Administrator registers party
	 * @param partyRandom
	 * @param partyCsvFile
	 * @throws IOException
	 */
	@Parameters({ "partyRandom", "partyCsvFile" })
	@Test(priority = 5, enabled = true)
	public void registerParty(String partyRandom, String partyCsvFile) throws IOException {
		listParty = utilities.putTextFromFileToAList(partyRandom);
		party = utilities.getRandomLine(listParty);
		pageParty.registerParty(party, Integer.toString(utilities.getRandomNumber(100)), partyCsvFile, "");
		Assert.assertTrue(pageParty.alert.getText().contains(party + " sukurta"));
	}

	/**
	 * Administrator add candidates list to a county
	 * @param candidatesList
	 */
	@Parameters({"candidatesList" })
	@Test(priority = 6, enabled = true)
	public void addCandidatesList(String candidatesList) {
		pageCounty.menuCounty.click();
		pageCounty.addCandidatesList(county, candidatesList);
		pageLogin.logout();
	}

	/**
	 * Representative registers all the votes in single member county
	 */
	@Test(priority = 8, enabled = true)
	public void fillingSingleMemberResults() {
		pageLogin.login(pageRepresentative.getRepresentativeUsername(0), pageRepresentative.getRepresentativePassword(0));
		pageSingleMember.fillOutResultRows();
		Assert.assertTrue(pageSingleMember.alertMessage.getText().contains("Jūsų apylinkės balsai užregistruoti"));
	}

	/**
	 * Representative registers all the votes in multi member county
	 */
	@Test(priority = 9, enabled = true)
	public void fillingMultiMemberResults() {
		pageMultiMember.fillOutResultRows();
		Assert.assertTrue(pageMultiMember.alertMessage.getText().contains("Jūsų apylinkės balsai užregistruoti"));
		pageMultiMember.buttonLogoutRepresentative.click();
	}

	/**
	 * Administrator confirms results of single member county
	 * @param usernameAdmin
	 * @param password
	 */
	@Parameters({ "usernameAdmin", "password" })
	@Test(priority = 11, enabled = true)
	public void confirmingSingleMemberResults(String usernameAdmin, String password) {
		pageLogin.login(usernameAdmin, password);
		pageResults.confirmResultsInSingleMember();
		Assert.assertTrue(pageResults.successMessage.getText().contains(pageSingleMember.getDistrictName() + " balsai patvirtinti."));
	}

	/**
	 * Adminstrator confirms results of multi county
	 * @throws InterruptedException
	 */
	@Test(priority = 12, enabled = true)
	public void confirmingMultiMemberResults() throws InterruptedException {
		pageResults.confirmResultsInMultiMember();
		Assert.assertTrue(pageResults.successMessage.getText().contains(pageSingleMember.getDistrictName() + " balsai patvirtinti."));
		pageMultiMember.buttonLogoutRepresentative.click();
	}

}
