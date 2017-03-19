package it.akademija.representative;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import it.akademija.admin.RepresentativePage;
import it.akademija.voting.VotingSystem;

public class SingleMemberTest extends VotingSystem{

	@BeforeClass
	public void setUp(){
		pageSingleMember = new SingleMemberPage(driver);
		pageRepresentative = new RepresentativePage(driver);
		pageLogin.login(pageRepresentative.getRepresentativeUsername(0), pageRepresentative.getRepresentativePassword(0));
	}
	
	@Test(priority = 27)
	public void fillingSingleMemberResults() {
		pageSingleMember.fillOutResultRows();
		Assert.assertTrue(pageSingleMember.alertMessage.getText().contains("Jûsø apylinkës balsai uþregistruoti"));
	}
}
