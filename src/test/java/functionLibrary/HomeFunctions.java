package functionLibrary;



import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import com.codoid.products.fillo.Select;

import frameworkFactory.BaseTestSuite;

public class HomeFunctions extends BaseTestSuite{
	
	public void SetSearchData()
	{
		String valueToBeEntered= inmap.get().get("TextToSearch");
		
		mSetValue(getPage().googleHomePage.txtSearch, valueToBeEntered);
		
		mEditValue(getPage().googleHomePage.txtSearch, valueToBeEntered, Keys.ENTER);
		
		logResult("Pass", "Enter a Search Text :'"+valueToBeEntered+"'", "", "Yes");
	}
	
	public void clickIamFeelingLucky()
	{
		mJSClick(getPage().googleHomePage.btnIamFeelingLucky);
		logResult("Pass", "Click on I'm Feeling Lucky", "", "Yes");
	}
	
}
	
	

