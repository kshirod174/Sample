package ObjectLibrary;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import org.openqa.selenium.*;

public class GoogleResultPage {
	
	public GoogleResultPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
		
	
	
	
	//Image Tab
	 @FindBy(xpath="//a[normalize-space(text())='Images']") public WebElement tabImage;
	 
	
	 
}
	