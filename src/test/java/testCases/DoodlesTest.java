package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import frameworkFactory.BaseTestSuite;
import functionLibrary.ResultFunctions;
import functionLibrary.DoodlesFunctions;
import functionLibrary.HomeFunctions;


public class DoodlesTest extends BaseTestSuite{
	
	HomeFunctions homeFunctions;
	DoodlesFunctions doodlesfunctions;
	
	@BeforeClass
	public void beforeDiscoverAPI()
	{
		homeFunctions= new HomeFunctions();
		//jjhjlw
		
		doodlesfunctions= new DoodlesFunctions();
	}
	
	
	@Test(testName="TC_001 Validate the Doodles Page" ,description="To Verify that when the user click on I'm Feeling Lucky button, the page navigates to Doodles Page")
	public void TC_ValidateDoodlesPage() throws Exception
	{
		//click on "I'm Feeling Lucky"
		homeFunctions.clickIamFeelingLucky();
		
		//Validate Page Navigation to Doodle Page
		doodlesfunctions.validatePageNavigationToDoodlePage();
	}
    
	
}
