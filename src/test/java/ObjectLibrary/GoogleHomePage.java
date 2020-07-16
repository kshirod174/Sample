package ObjectLibrary;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import org.openqa.selenium.*;

public class GoogleHomePage {
	
	public GoogleHomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
		
	
	
	
	//Search Box
	@FindBy(xpath="//*[@title='Search']") public WebElement txtSearch;
	 
	//I'm Feeling Lucky Button
	@FindBy(xpath="//*[contains(@class,'FPdoLc')]//*[@name='btnI']") public WebElement btnIamFeelingLucky; 
	 
	
	 
}
	