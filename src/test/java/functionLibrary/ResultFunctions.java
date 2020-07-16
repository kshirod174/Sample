package functionLibrary;



import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import com.codoid.products.fillo.Select;

import frameworkFactory.BaseTestSuite;

public class ResultFunctions extends BaseTestSuite{
	
	public void validatePageNavigationToResultPage()
	{
		if(mWaitTillPageNavigateTo("Google Search", 10))
		{
			logResult("Pass", "Validate page navigation to 'Result Page'", "", "Yes");
		}
		
		else
		{
			logResult("Pass", "Validate page navigation to 'Result Page'", "Application did not navigate to 'Result Page'", "Yes");
		}
	}
	
	
	
}
	
	

