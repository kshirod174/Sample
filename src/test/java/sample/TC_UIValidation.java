package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import frameworkFactory.BaseTestSuite;
import frameworkFactory.ImageValidation;


public class TC_UIValidation extends BaseTestSuite {

	
	
	

	
	//Sample Test Case for Webpage & image validation
	/*prerequisite
	 Download file: ImageMagick-7.0.8-15-portable-Q16-x86 and store to the local computer system and
	 set the path of the file in the above
	 Class: ImageValidation.java
	 Function: compareImagesWithImageMagick()
	 Parameter to ProcessStarter.setGlobalSearchPath("<path>")
	 */
	
	
	@Test
	public void tc_ImageValidation() throws Exception
	{
		
		driver().get("https://www.google.co.in/");
		
		logResult("Pass", "Open Google app", "", "Yes");
		
		//Element to Compare
		WebElement wb= driver().findElement(By.id("lga"));
		
		//create object of Imagevalidation class
		ImageValidation iv= new ImageValidation();
		
		
		//function to validate element image with the baseline image
		iv.validateImage(driver(), wb, "googleLOGO");
		
		//function to validate element image with the baseline image
		iv.validateImage("path\\image_1.PNG", "path\\image_2.PNG", "NameOfTheImage");
		
		
	}
	
	
	
	
}
