package frameworkFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

public class DriverManager{
	
	//private static ThreadLocal<RemoteWebDriver> webDriver= new ThreadLocal();
	private static ThreadLocal<RemoteWebDriver> webDriver= new ThreadLocal();
	
	private static ThreadLocal<WiniumDriver> winDriver= new ThreadLocal();
	
	public static WiniumDriverService service;
	
	
	public static void launchRemoteBrowserInstance(String browser) throws MalformedURLException {
		try{
		webDriver.set(new RemoteWebDriver(new URL("http://10.18.15.201:4444/wd/hub"), getBrowserCapabilities(browser)));;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
		}
		 //return new RemoteWebDriver(new URL("http://10.18.15.201:4444/wd/hub"), getBrowserCapabilities(browser));
	}
	
	private static DesiredCapabilities getBrowserCapabilities(String browserType) {
		switch (browserType) {
		case "firefox":
			System.out.println("Opening firefox driver");
			return DesiredCapabilities.firefox();
		case "chrome":
			System.out.println("Opening chrome driver");
			return DesiredCapabilities.chrome();
		case "IE":
			System.out.println("Opening IE driver");
			return DesiredCapabilities.internetExplorer();
		default:
			System.out.println("browser : " + browserType + " is invalid, Launching Firefox as browser of choice..");
			return DesiredCapabilities.firefox();
		}
	}
	
	public static void launchLocalBrowserInstance(String browser) throws MalformedURLException {
		if(browser.equalsIgnoreCase("firefox"))
		{
			
		}
	 if(browser.equalsIgnoreCase("Chrome"))
	{	
		 String chromeDriverString= "";
		 if(BaseTestSuite.os.contains("win"))
		 {
			 chromeDriverString= System.getProperty("user.dir")+"/Drivers/"+BaseTestSuite.os+"/chromedriver.exe";
			 
		 }
		 
		 else if (BaseTestSuite.os.contains("linux"))
		 {
			 chromeDriverString=BaseTestSuite.driverPath;
		 }
		 
		 System.out.println("Chrome Driver Available at : \""+chromeDriverString+"\"");
		 
		 //String chromeDriver= BaseTestSuite.driverPath+BaseTestSuite.os+"\\chromedriver.exe";
//		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Drivers\\chromedriver.exe");
		 System.setProperty("webdriver.chrome.driver",chromeDriverString);
//		 WebDriverManager.getInstance(CHROME).setup();
		 //WebDriverManager.chromedriver().setup();
		 webDriver.set(new ChromeDriver());
		 
		
		 
        
			
	}
	else if(browser.equalsIgnoreCase("IE"))
	{
		
	}
} 
	 
	public RemoteWebDriver driver() {
	        RemoteWebDriver driver = webDriver.get();
	        if (driver == null) {
	            throw new IllegalStateException("Driver should have not been null.");
	        }
	        return driver;
	}
	
	
	public static WiniumDriver setupWinDriver(String applicationPath, boolean existing) throws IOException, InterruptedException {
		
		//turnOffWinDriver();
	    
	    String winiumDriverPath = System.getProperty("user.dir")+"\\Drivers\\Winium.Desktop.Driver.exe";
	 
	    DesktopOptions options = new DesktopOptions(); //Initiate Winium Desktop Options
	    options.setApplicationPath(applicationPath);
	    options.setDebugConnectToRunningApp(existing);
	 
	    File drivePath = new File(winiumDriverPath); //Set winium driver path
	 
	    service = new WiniumDriverService.Builder()
	    		.usingDriverExecutable(drivePath)
	    		.usingPort(9999)
	    		.withVerbose(true)
	    		.withSilent(false)
	    		.buildDesktopService();
	    		
	    service.start(); //Build and Start a Winium Driver service
	    WiniumDriver driver = new WiniumDriver(service, options); //Start a winium driver
	    
	    Thread.sleep(4000);
		
/*		Set<String> set= driver.getWindowHandles();

	    Iterator it= set.iterator();
	    
	    while(it.hasNext())
	    {
	    	driver.switchTo().window(it.next().toString());
	    }*/
	    
	 
	    return driver;
	    //winDriver.set(driver);
	 
	}
	
	public static void turnOffWinDriver() throws IOException  {
		try{
	
		Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe /t");
		}
		
		catch(Exception e)
		{
			System.out.println("Process doesn't exists");
		}
	}
	
	public WiniumDriver winDriver() {
		WiniumDriver driver = winDriver.get();
        if (driver == null) {
            throw new IllegalStateException("Driver should have not been null.");
        }
        return driver;
}
	
	
	
}
	

	

	
	
	
	


