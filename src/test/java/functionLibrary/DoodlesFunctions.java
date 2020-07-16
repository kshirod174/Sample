package functionLibrary;



import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import com.codoid.products.fillo.Select;

import frameworkFactory.BaseTestSuite;

public class DoodlesFunctions extends BaseTestSuite{
	
	public void validatePageNavigationToDoodlePage()
	{
		if(mWaitTillPageNavigateTo("Google Doodles", 10))
		{
			logResult("Pass", "Validate page navigation to 'Doodle Page'", "", "Yes");
		}
		
		else
		{
			logResult("Pass", "Validate page navigation to 'Doodle Page'", "Application did not navigate to 'Doodle Page'", "Yes");
		}
	}
	
}
	
	

