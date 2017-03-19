package it.akademija.representative;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import it.akademija.admin.RepresentativePage;
import it.akademija.voting.VotingSystem;

public class MultiMemberTest extends VotingSystem{

	@BeforeClass
	public void setUp(){
		pageMultiMember = new MultiMemberPage(driver);
		pageRepresentative = new RepresentativePage(driver);
		pageLogin.login(pageRepresentative.getRepresentativeUsername(0), pageRepresentative.getRepresentativePassword(0));
	}
	
	@Test(priority = 30)
	public void fillingMultiMemberResults() {
		pageMultiMember.fillOutResultRows();
		Assert.assertTrue(pageMultiMember.alertMessage.getText().contains("Jûsø apylinkës balsai uþregistruoti"));
	}
	
}
