package ObjectLibrary;

import org.openqa.selenium.WebDriver;

public  class ApplicationPages {
	
	
	public GoogleHomePage googleHomePage;
	public GoogleResultPage googleResultPage;
	

	public void initiatePageObjects(WebDriver driver)
	{
		
		googleHomePage= new GoogleHomePage(driver);
		googleResultPage= new GoogleResultPage(driver);

	}
	

}
