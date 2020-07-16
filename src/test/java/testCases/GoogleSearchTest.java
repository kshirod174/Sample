package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import frameworkFactory.BaseTestSuite;
import functionLibrary.ResultFunctions;
import functionLibrary.HomeFunctions;


public class GoogleSearchTest extends BaseTestSuite{
	
	HomeFunctions homefunctions;
	ResultFunctions resultfunctions;
	
	@BeforeClass
	public void beforeDiscoverAPI()
	{
		homefunctions=new HomeFunctions();
		resultfunctions= new ResultFunctions();
	}
	
	
	@Test(dataProvider = "DP1",testName="TC_002 Validate the Search Functionality" ,description="To Verify that when the user search for a specific text, the page navigates to the Search Result Page")
	public void TC_SearchInGoogle(int iteration) throws Exception
	{
		setTestData(iteration);
		
		
		//Search for something in Google and press Enter
		homefunctions.SetSearchData();
		
		//Validate if Page navigates to the Result Page
		resultfunctions.validatePageNavigationToResultPage();
		
	}
    
	
}
