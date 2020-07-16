package frameworkFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.activity.InvalidActivityException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.interactions.touch.Scroll;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import ObjectLibrary.ApplicationPages;
import cucumber.api.Scenario;
import cucumber.api.testng.TestNGCucumberRunner;
import io.qameta.allure.Allure;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
//import ObjectLibrary.ApplicationPages;




public class BaseTestSuite extends DriverManager {
	
	public static String os = getOSName();
	public static String driverPath= System.getenv("ChromeDriverPath");
	
	public static int counter=0;
	public static Scenario scenario;
	public static String resultLocation;
	
	//public RemoteWebDriverr driver; manasmita
	public static String url,browser,browserNode;
	public static String timeStamp ;
	public static String currentDate;
	
	public static ExtentReports extent;
	//public static ExtentTest test;
	
	public static ThreadLocal<ExtentTest> setExtentTest;
	
	/*for Mobile devices*/
	public static String tool;	
	public static String appPackage,appActivity,seetestAppPackage;
	
	
	public static Fillo fillo;
	public static Recordset recordset;
	public static Connection connection;
	
	public WebDriverWait wait;
	protected TestNGCucumberRunner testNGCucumberRunner;
	
	public static ThreadLocal<HashMap<String, String>> inmap;
	
	
	public static String reportPath;
	
	public static ThreadLocal<ReportManager> lt;
	
	public static ThreadLocal<WritableWorkbook> workbook ;

	public static ThreadLocal<WritableSheet> sheet;
	
	public static int count;
	
	//public static String execType;
	
	public static ThreadLocal<String> className;
	//Test name
    public static ThreadLocal<String> testName;
    //Test Screenshot directory
    public String testScreenShotDirectory;
   
    //Main Directory of the test code
    public static String currentDir = System.getProperty("user.dir");
    
    public static String UploadingPath= currentDir+"\\src\\test\\resources\\UploadingDocs\\";
    
    public static ThreadLocal<ApplicationPages> appPages;
    
   
    public static void deleteFolder(File folder) {
    	
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
	
	public static String getOSName()
	{
		String nameOS= System.getProperty("os.name");
		
		if(nameOS.toLowerCase().contains("win"))
		{
			return "win";
		}
		
		else if(nameOS.toLowerCase().contains("linux"))
		{
			return "linux";
		}
		
		return "Not Identified";
	}

	
	@BeforeSuite
	public void Initialize() throws Throwable{
		
		System.out.println("Identified System OS : "+os);
		
		System.out.println("Driver Path has been set as : "+driverPath);
		
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
		
		BaseTestSuite.currentDate=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        BaseTestSuite.timeStamp=currentDate.replace(".", "");
        
        BaseTestSuite.resultLocation= System.getProperty("user.dir")+"\\Results\\";
        
        deleteFolder(new File(BaseTestSuite.resultLocation));
        
        deleteFolder(new File(System.getProperty("user.dir")+"\\allure-results"));
        
        new File(BaseTestSuite.resultLocation).mkdir();
        
		BaseTestSuite.reportPath=BaseTestSuite.resultLocation+ "Reports\\ExecutionReport";
		
        
        Properties prop = new Properties();
        
        prop.load(new FileInputStream("App.properties"));
        BaseTestSuite.url = prop.getProperty("Url").trim();
        BaseTestSuite.browser = prop.getProperty("browserName").trim();
        BaseTestSuite.browserNode = prop.getProperty("browserNode").trim();
		
        
		
        BaseTestSuite.extent = new ExtentReports(reportPath+".html");
        //BaseTestSuite.extent.loadConfig(new File("src/extent-config.xml"));
        inmap= new ThreadLocal();
        
        className= new ThreadLocal();
        
        setExtentTest= new ThreadLocal();
        
        testName= new ThreadLocal<>();
        
        appPages= new ThreadLocal<>();
        
        lt= new ThreadLocal<>();
        
        workbook= new ThreadLocal<>();
        
        sheet= new ThreadLocal<>();
        
        /*if(execType.equals(""))
        {
        	lt.set(new ReportManager());
            lt.get().onStart();
        }*/
        
		
        BaseTestSuite.fillo=new Fillo();
        
        
        i_Get_Connection("InputDataSheet");
        
        
	}
	
	
	
	
	@AfterSuite
	public void finalize() throws Throwable{
		/*if(execType.equals(""))
        {
			lt.get().onFinish();
        }*/
		extent.flush();
	}
	
	 
	
	
	
	@DataProvider(name = "DP1")  
    public Iterator<Object[]> RegistrationData(Method m){
     
    	className.set((m.getDeclaringClass().getCanonicalName().replace(".",">").split(">"))[1]);
		
		testName.set(m.getName());
	 
	 	int count= getDataSheetRowCount(className.get(), "TC_Name="+testName.get());
		
	 	
	 	
	 	
	 	Collection<Object[]> dp = new ArrayList<Object[]>();
	 	for(int i=1; i<=count;i++)
	 	{
	 		dp.add(new Object[]{i});
	 	}
	 	
	 	return dp.iterator();
	 	
    }
    
    
	
	
	@BeforeMethod(alwaysRun=true)
	public void beforeTC(Method m) throws Exception
	{
		className.set((m.getDeclaringClass().getCanonicalName().replace(".",">").split(">"))[1]);
		
		testName.set(m.getName());
		
		//if(execType.equals(""))
        //{
        	lt.set(new ReportManager(testName.get()));
            lt.get().onStart();
        //}
		
		//if(execType.equals(""))
       // {
			lt.get().writeToReport(testName.get(), BaseTestSuite.browser);
       // }
		
		
		
		launchBrowser(BaseTestSuite.browser);
		
		
		
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS) ;
		driver().manage().window().maximize();
		
		setExtentTest.set(extent.startTest(testName.get()));
		
		
		appPages.set(new ApplicationPages());
		getPage().initiatePageObjects(driver());
		
		//inmap= readFromExcel(className.get(), "TC_Name='"+testName.get()+"' AND Iteration ='1'");
		
		driver().get(BaseTestSuite.url);
		
	}
	
	public ApplicationPages getPage()
	{
		return appPages.get();
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterTC()
	{
		
		driver().quit();
		
		//if(execType.equals(""))
       // {
		lt.get().onFinish();
       // }
	}
	
	public void launchBrowser(String browser) throws MalformedURLException
	{
		if(browserNode.equalsIgnoreCase("remote"))
		{
			DriverManager.launchRemoteBrowserInstance(browser);
		}
		
		else
		{
			DriverManager.launchLocalBrowserInstance(browser);
		}
		
		
		
		
	}
	
	public void setTestData(int iteration)
	{
		inmap.set(readFromExcel(className.get(), "TC_Name='"+testName.get()+"' AND Iteration ='"+iteration+"'"));
	}
	
	
    
   
    
	
    
    public ExtentTest test() {
        ExtentTest test = setExtentTest.get();
        
        return test;
	}
    
		public boolean i_Get_Connection(String fileName) throws Throwable {
		
			File xls= new File(System.getProperty("user.dir")+"/src/test/resources/InputData/"+fileName+".xls");
			File xlsx= new File(System.getProperty("user.dir")+"/src/test/resources/InputData/"+fileName+".xlsx");
			 
			if(xls.exists())
			{
				connection= fillo.getConnection(xls.getAbsolutePath());
			}
			
			else if(xlsx.exists())
			{
				connection= fillo.getConnection(xlsx.getAbsolutePath());
			}
		
		 
		return true;
	}
    

	
	    
	    
	  
		
	
	public void logResult(String Status,String Step,String Decripion,String ScreenShot){
		 
		counter++;
		
		 if(ScreenShot.equalsIgnoreCase("yes"))
		 {
			 String screenshotName= "img"+counter+".jpg";
			 File scrFile = ((TakesScreenshot)driver()).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(resultLocation+"/Screenshots/"+screenshotName));
				
				
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
			attachScreenshotToAllure(scrFile, Step);
			
			
			//String screenShotPath= resultLocation+"\\Screenshots\\"+screenshotName;
			String screenShotPath= "../Screenshots/"+screenshotName;
			String image= test().addScreenCapture(screenShotPath);
				if(Status.equalsIgnoreCase("Pass") || Status.toLowerCase().contains("pass"))
				{
					test().log(LogStatus.PASS, Step ,image);
					
				}else
				{
					test().log(LogStatus.FAIL, Step ,image);
					test().log(LogStatus.INFO, Decripion, "");
					
				}
				
				try{
				
					BaseTestSuite.lt.get().reportResult(Step, Status, Decripion);
					BaseTestSuite.lt.get().reportScreenShot(screenshotName);
					BaseTestSuite.lt.get().row++;
				}
				
				catch(Exception e)
				{
					System.out.println("Exception while trying to Report result to Excelsheet");
					e.printStackTrace();
				}
				
				
		 }
		 
		 if(ScreenShot.equalsIgnoreCase("no")) 
		 {
			 Reporter.log(Step);
			 
			 if(Status.equalsIgnoreCase("Pass") || Status.toLowerCase().contains("pass"))
				{
				 test().log(LogStatus.PASS,Step +'\n'+Decripion, "Pass");
				 
				 
				}else
				{
					test().log(LogStatus.FAIL,Step, "Fail");
					test().log(LogStatus.INFO, Decripion, "");
					
				}
			 
			 try{

					BaseTestSuite.lt.get().reportResult(Step, Status, Decripion);
					BaseTestSuite.lt.get().row++;
		        
			 }
			 
			 catch(Exception e)
				{
					System.out.println("Exception while trying to Report result to Excelsheet");
					e.printStackTrace();
				}
			 
			 	
			 
		 }
		 
		 extent.endTest(test());
		 extent.flush();
		 
		 
	 }
	
	
	public void attachScreenshotToAllure(File screenshot, String step)
	{
		InputStream is=null;
		try{
			is= new FileInputStream(screenshot);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		Allure.addAttachment("<SCREENSHOT> --> " +step, is);
	}
	
	public void sleep(long millis)
	{
		try{
			Thread.sleep(millis);
		}
		
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public static int getDataSheetRowCount(String sheetName, String condition)
	{
		int rowCount=0;
		
		String[] arrCond= condition.split("=");
		
		String primCol= arrCond[0].trim();
		String primVal= arrCond[1].replace("'", "").trim();
		
		//String keyQuery= "Select * from "+sheetName;
		String Query= "Select * from "+sheetName+" where "+primCol+"='"+primVal+"'";
		System.out.println(Query);
		
		try {
			
				System.out.println("Setup Query");
				recordset=connection.executeQuery(Query);
		
				rowCount= recordset.getCount();
				System.out.println("Row Count: "+rowCount);
		
				recordset.close();
		
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			Assert.assertTrue(false,">>>DATA CONFLICT<<< in Sheet: "+sheetName);
		}
		
		return rowCount; 
		
		
	
	}
	
	
	public static String fGetRandomNumUsingTime()
	  {
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(calendar.getTime());
		  int month = Calendar.MONTH;
		  int day = Calendar.DAY_OF_MONTH;	  
		  int hours = calendar.get(Calendar.HOUR_OF_DAY);
		  int minutes = calendar.get(Calendar.MINUTE);
		  int seconds = calendar.get(Calendar.SECOND);
		  String Rand = Integer.toString(month)+Integer.toString(day)+Integer.toString(hours)+Integer.toString(minutes)+Integer.toString(seconds);
		  return Rand;		  
	  }
	
	
	public int getDataSheetRowCount(String sheetName)
	{
		int rowCount=0;
		
		//String keyQuery= "Select * from "+sheetName;
		String Query= "Select * from "+sheetName;
		System.out.println(Query);
		
		try {
				recordset=connection.executeQuery(Query);
		
				rowCount= recordset.getCount();
		
		
		
		
		
		 
		recordset.close();
		
		}
		
		catch(Exception e)
		{
			Assert.assertTrue(false,">>>DATA CONFLICT<<< in Sheet: "+sheetName);
		}
		
		return rowCount; 
		
		
	
	}
	
	
	
	
	
	public boolean fileExists(String path, String fileName)
	{
		File dir = new File(path);
	    File[] dir_contents = dir.listFiles();
	    String temp = path+"\\"+fileName;
	    boolean check = new File(temp).exists();
	    System.out.println("Check" + check); 

	    for (int i = 0; i < dir_contents.length; i++) {
	        if (dir_contents[i].getName().equals(fileName))
	        return true;	
	    }
	    
	    return false;

	}
	
	
	public HashMap<String, String> readFromExcel(String sheetName, String condition)
	{
		HashMap<String, String> mapDet=new HashMap<String, String>();
		
		
		//String keyQuery= "Select * from "+sheetName;
		String Query= "Select * from "+sheetName+" where "+condition;
		System.out.println(Query);
		
		try {
				recordset=connection.executeQuery(Query);
		
				
		
				ArrayList <String> keys= recordset.getFieldNames();
				ArrayList <String> values= new ArrayList<String>();
				
				for (int i=0; i<keys.size();i++)
				{
					String key= keys.get(i);
					recordset.next();
					String value= recordset.getField(key);
					values.add(value);
					
				}
				
				for(int j=0; j<keys.size();j++)
				{
					
					mapDet.put(keys.get(j), values.get(j));
					
					//System.out.println("Key Value Pair::  "+keys.get(j)+ "   ----->>>   "+values.get(j));
						
				}
				
				
				
				 
				recordset.close();
		
		}
		
		catch(Exception e)
		{
			Assert.assertTrue( false,">>>DATA CONFLICT<<< in Row where "+condition+" in Sheet: "+sheetName);
		}
		
		
		
		
		return(mapDet);
	}
	
	public boolean writeToExcel(String sheetName ,String condition, String column, String value) throws FilloException
	{
		
		String[] arrCond= condition.split("=");
		
		String primCol= arrCond[0].trim();
		String primVal= arrCond[1].replace("'", "").trim();
		
		boolean flag= false;
		
		
		
		String Query1= "Select * from "+sheetName+" where "+primCol+"='"+primVal+"'";
		System.out.println(Query1);
		
		try
		{
			recordset=connection.executeQuery(Query1);
			
			/*while(recordset.next())
			{
				String iteration=recordset.getField("ITERATION");
				if(recordset.getField("ITERATION").equals(row+""))
				{
					flag=true;
				}
			}*/
			
			
			recordset.close();
			flag=true;
		}
		
		catch(Exception e)
		{
			String Query2= "INSERT INTO "+sheetName+"("+primCol+") VALUES('"+primVal+"')";
			
			
			System.out.println(Query2);
			
			connection.executeUpdate(Query2);
			flag=true;
		}
		
		
	
		
			if(flag==false)
			{
				String Query2= "INSERT INTO "+sheetName+"("+primCol+") VALUES('"+primVal+"')";
				
				
				System.out.println(Query2);
				
				connection.executeUpdate(Query2);
			}
			
		
		
		
		
		
		String Query3= "UPDATE "+sheetName+" SET "+column+"='"+value+"' where "+primCol+"='"+primVal+"'";
		
		
		System.out.println(Query3);
		
		connection.executeUpdate(Query3);
		 
		
		 
		
		
		
		
		
		return(true);
	}
	
	
	
	public boolean mIsElementPresent(WebElement element)
	{
		try {
		element.getAttribute("");
		return true;
		}
		
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean mIsElementPresent(String xPath)
	{
		try {
		driver().findElement(By.xpath(xPath));
		return true;
		}
		
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean mIsFramePresent(String frameName)
	{
		try {
		driver().switchTo().frame(frameName);
		driver().switchTo().defaultContent();
		return true;
		}
		
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean mIsElementDisplayed(WebElement element)
	{
		try {
			
			if(element.isDisplayed())
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean mWaitTillElementPresent(WebElement element, int time)
	{
		
		wait = new WebDriverWait(driver(), time);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		
		return mIsElementPresent(element);
		
	
	}
	
	public boolean mWaitTillElementDisappear(WebElement element, int time)
	{
		
		wait = new WebDriverWait(driver(), time);
		wait.until(ExpectedConditions.invisibilityOf(element));
		
		return mIsElementPresent(element);
		
	
	}
	
	public boolean mWaitTillElementPresent(String xPath, int time)
	{
		
		wait =  new WebDriverWait(driver(), time);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
		
		return mIsElementPresent(xPath);
		
	
	}
	
	public boolean mWaitTillFramePresentAndSwitch(String frameName, int time)
	{
		
		wait =  new WebDriverWait(driver(),time);
		driver().switchTo().defaultContent();
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
		
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver();
		String currentFrame = jsExecutor.executeScript("return self.name").toString();
		
		if(!(currentFrame.contains((frameName.toLowerCase()))) && !(currentFrame.contains((frameName.toUpperCase()))))
		{
			return false;
		
		}
		
		else {
		return true;
		}
	
	}
	
	
	
	public void mJSClick(WebElement element)
	{
		JavascriptExecutor executor = (JavascriptExecutor)driver();
		executor.executeScript("arguments[0].click();", element);
		
		sleep(2000);
	}
	
	public void mJSClick(String xpath)
	{
		JavascriptExecutor executor = (JavascriptExecutor)driver();
		executor.executeScript("arguments[0].click();", driver().findElement(By.xpath(xpath)));
		
		sleep(2000);
	}
	
	public void mExecuteJS(String script, WebElement element)
	{
		JavascriptExecutor executor = (JavascriptExecutor)driver();
		executor.executeScript(script, element);
		
		sleep(2000);
		
	}
	
	
	public String mParseXML(String xmlFilePath, String tagName, String attributeName)
	{
		String value="";
		 try {

				File fXmlFile = new File(xmlFilePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName(tagName);
				
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
					
					Element eElement = (Element) nNode;
					
					value= eElement.getAttribute(attributeName);
				}
				
				
				
				
				
		 }
		 
		 catch (Exception e) {
				e.printStackTrace();
				
			    }
		 
		 return value;

	}
	
	public String mTextFromPDF(String URLorFile, String docPDF) throws Exception
	{
		String text="";
		PDDocument document=null;
		PDFTextStripper pdfStripper;
		
		if(URLorFile.equalsIgnoreCase("url"))
		{
			URL TestURL = new URL(docPDF);

			
			
			
		}
		else if(URLorFile.equalsIgnoreCase("file"))
		{
			
			File file = new File(docPDF);
			
			document = PDDocument.load(file);
			
			
			
		}
		
		else{
			throw new InvalidActivityException();
		}
		
		pdfStripper = new PDFTextStripper();
		text = pdfStripper.getText(document);
		
		return text;
		
		
		
	}
	
	public boolean sleep(int milliseconds)
	{
		try{
			Thread.sleep(milliseconds);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	
	
	public String mGetPageContent(String url)
	{
		ServiceHandler servHandler= new ServiceHandler();
		
		servHandler.setHeader("User-Agent", "Mozilla/5.0");
		return servHandler.GetPageContent(url);
	}
	
	
	public boolean mClick(WebElement wb)
	{
		try
		{
			wb.click();
		}
		
		catch(ElementNotVisibleException e1)
		{
			try
			{
				((JavascriptExecutor) driver()).executeScript("arguments[0].click();", wb);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			
			
			
		}
		
		catch(ElementClickInterceptedException e2)
		{
			try
			{
				((JavascriptExecutor) driver()).executeScript("arguments[0].click();", wb);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			
		}
		
		sleep(3000);
		
		return true;
	}
	
	public boolean mClick(By by)
	{
		WebElement wb= driver().findElement(by);
		try
		{
			
			wb.click();
		}
		
		catch(ElementNotVisibleException e1)
		{
			try
			{
				((JavascriptExecutor) driver()).executeScript("arguments[0].click();", wb);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			
		}
		
		catch(ElementClickInterceptedException e2)
		{
			try
			{
				((JavascriptExecutor) driver()).executeScript("arguments[0].click();", wb);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			
		}
		
		
		return true;
	}
	
	public boolean mClick(String xpath)
	{
		WebElement wb= driver().findElement(By.xpath(xpath));
		try
		{
			
			wb.click();
		}
		
		catch(ElementNotVisibleException e1)
		{
			try
			{
				((JavascriptExecutor) driver()).executeScript("arguments[0].click();", wb);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			
		}
		
		catch(ElementClickInterceptedException e2)
		{
			try
			{
				((JavascriptExecutor) driver()).executeScript("arguments[0].click();", wb);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			
		}
		
		return true;
	}
	
	public boolean mSetValue(WebElement wb, String value)
	{
		
			wb.click();
			wb.sendKeys(value);
		
		
		
		return true;
	}
	
	public boolean mSetValue(By by, String value)
	{
		
			WebElement wb= driver().findElement(by);
			wb.click();
			wb.sendKeys(value);
		
		
		
		return true;
	}
	
	public boolean mSetValue(String xpath, String value)
	{
		
		WebElement wb= driver().findElement(By.xpath(xpath));
		wb.click();
		wb.sendKeys(value);
	
		
		
		
		return true;
	}
	
	
	public boolean mEditValue(WebElement wb, String value)
	{
		
			wb.click();
			
			wb.sendKeys(Keys.END);
			
			for(int i=0; i<wb.getAttribute("value").length();i++)
			{
				wb.sendKeys(Keys.BACK_SPACE);
				sleep(1000);
			}
			
			
			wb.sendKeys(value);
		
		
		
		return true;
	}
	
	public boolean mEditValue(WebElement wb, String value, Keys keys)
	{
		
			wb.click();
			
			wb.sendKeys(Keys.END);
			
			for(int i=0; i<wb.getAttribute("value").length();i++)
			{
				wb.sendKeys(Keys.BACK_SPACE);
				sleep(1000);
			}
			
			
			wb.sendKeys(value, keys);
		
		
		
		return true;
	}
	
	public boolean mEditValue(By by, String value)
	{
		
			WebElement wb= driver().findElement(by);
			wb.click();
			
			wb.sendKeys(Keys.END);
			
			for(int i=0; i<wb.getAttribute("value").length();i++)
			{
				wb.sendKeys(Keys.BACK_SPACE);
				sleep(1000);
			}
			
			wb.sendKeys(value);
		
		
		
		return true;
	}
	
	public boolean mEditValue(String xpath, String value)
	{
		
		WebElement wb= driver().findElement(By.xpath(xpath));
		wb.click();
		
		wb.sendKeys(Keys.END);
		
		for(int i=0; i<wb.getAttribute("value").length();i++)
		{
			wb.sendKeys(Keys.BACK_SPACE);
			sleep(1000);
		}
		
		wb.sendKeys(value);
	
		
		
		
		return true;
	}
	
	public boolean mSelectFromSelectBox(WebElement wb, String VisibleText_or_Index_or_Value, String optionValue)
	{
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VISIBLETEXT"))
		{
			sel.selectByVisibleText(optionValue);
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("INDEX"))
		{
			sel.selectByIndex(Integer.parseInt(optionValue));
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VALUE"))
		{
			sel.selectByValue(optionValue);
		}
		
		return true;
		
		
	}
	
	public boolean mSelectFromSelectBox(By by, String VisibleText_or_Index_or_Value, String optionValue)
	{
		WebElement wb= driver().findElement(by);
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VISIBLETEXT"))
		{
			sel.selectByVisibleText(optionValue);
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("INDEX"))
		{
			sel.selectByIndex(Integer.parseInt(optionValue));
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VALUE"))
		{
			sel.selectByValue(optionValue);
		}
		
		return true;
		
		
	}
	
	public boolean mSelectFromSelectBox(String xpath, String VisibleText_or_Index_or_Value, String optionValue)
	{
		WebElement wb= driver().findElement(By.xpath(xpath));
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VISIBLETEXT"))
		{
			sel.selectByVisibleText(optionValue);
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("INDEX"))
		{
			sel.selectByIndex(Integer.parseInt(optionValue));
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VALUE"))
		{
			sel.selectByValue(optionValue);
		}
		
		return true;
		
		
	}
	
	public boolean mDeSelectFromSelectBox(WebElement wb, String VisibleText_or_Index_or_Value, String optionValue)
	{
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VISIBLETEXT"))
		{
			sel.deselectByVisibleText(optionValue);
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("INDEX"))
		{
			sel.deselectByIndex(Integer.parseInt(optionValue));
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VALUE"))
		{
			sel.deselectByValue(optionValue);
		}
		
		return true;
		
		
	}
	
	public boolean mDeSelectFromSelectBox(By by, String VisibleText_or_Index_or_Value, String optionValue)
	{
		WebElement wb= driver().findElement(by);
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VISIBLETEXT"))
		{
			sel.deselectByVisibleText(optionValue);
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("INDEX"))
		{
			sel.deselectByIndex(Integer.parseInt(optionValue));
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VALUE"))
		{
			sel.deselectByValue(optionValue);
		}
		
		return true;
		
		
	}
	
	public boolean mDeSelectFromSelectBox(String xpath, String VisibleText_or_Index_or_Value, String optionValue)
	{
		WebElement wb= driver().findElement(By.xpath(xpath));
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VISIBLETEXT"))
		{
			sel.deselectByVisibleText(optionValue);
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("INDEX"))
		{
			sel.deselectByIndex(Integer.parseInt(optionValue));
		}
		
		else if(VisibleText_or_Index_or_Value.replace(" ", "").equalsIgnoreCase("VALUE"))
		{
			sel.deselectByValue(optionValue);
		}
		
		return true;
		
		
	}
	
	public boolean mDeSelectAllFromSelectBox(WebElement wb)
	{
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		sel.deselectAll();
		
		return true;
		
		
	}
	
	public boolean mDeSelectAllFromSelectBox(By by)
	{
		WebElement wb= driver().findElement(by);
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		sel.deselectAll();
		
		return true;
		
		
	}
	
	public boolean mDeSelectAllFromSelectBox(String xpath)
	{
		WebElement wb= driver().findElement(By.xpath(xpath));
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		sel.deselectAll();
		
		return true;
		
		
	}
	
	
	public boolean mDropDownClickSelect(WebElement wb, String text)
	{
		
		wb.click();
		sleep(2000);
		
		driver().findElement(By.xpath("//*[text()[normalize-space()='"+text+"']]")).click();
		
		return true;
		
		
	}
	
	public boolean mDropDownClickSelect(By by, String text)
	{
		WebElement wb= driver().findElement(by);
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		driver().findElement(By.xpath("//*[text()[normalize-space()='"+text+"']]")).click();
		
		return true;
		
		
	}
	
	public boolean mDropDownClickSelect(String xpath, String text)
	{
		WebElement wb= driver().findElement(By.xpath(xpath));
		
		Select sel= new Select(wb);
		
		wb.click();
		sleep(2000);
		
		driver().findElement(By.xpath("//*[text()[normalize-space()='"+text+"']]")).click();
		
		return true;
		
		
	}
	
	public boolean mRightClickOnAndSelect(WebElement wb1, WebElement wb2)
	{
		Actions act= new Actions(driver());
		
		act.contextClick(wb1).perform();
		sleep(2000);
		
		wb2.click();
		
		return true;
		
		
	}
	
	public boolean mRightClickOnAndSelect(By by1, By by2)
	{
		WebElement wb1= driver().findElement(by1);
		
		Actions act= new Actions(driver());
		
		act.contextClick(wb1).perform();
		sleep(2000);
		
		driver().findElement(by2).click();
		
		return true;
		
		
	}
	
	public boolean mRightClickOnAndSelect(String xpath1, String xpath2 )
	{
		WebElement wb1= driver().findElement(By.xpath(xpath1));
		
		Actions act= new Actions(driver());
		
		act.contextClick(wb1).perform();
		sleep(2000);
		
		driver().findElement(By.xpath(xpath2)).click();
		
		return true;
		
		
	}
	
	public boolean mRightClickOnAndSelect(WebElement wb1, String xpath2 )
	{
		
		Actions act= new Actions(driver());
		
		act.contextClick(wb1).perform();
		sleep(2000);
		
		driver().findElement(By.xpath(xpath2)).click();
		
		return true;
		
		
	}
	
	public boolean mRightClickOnAndSelect(WebElement wb1, By by2 )
	{
		
		Actions act= new Actions(driver());
		
		act.contextClick(wb1).perform();
		sleep(2000);
		
		driver().findElement(by2).click();
		
		return true;
		
		
	}
	
	public boolean mMouseOverElement(WebElement wb)
	{
		Actions act= new Actions(driver());
		act.moveToElement(wb).perform();
		sleep(1000);
		
		return true;
	}
	
	public boolean mMouseOverElement(By by)
	{
		WebElement wb= driver().findElement(by);
		Actions act= new Actions(driver());
		act.moveToElement(wb).perform();
		sleep(1000);
		
		return true;
	}
	
	public boolean mMouseOverElement(String xpath)
	{
		WebElement wb= driver().findElement(By.xpath(xpath));
		Actions act= new Actions(driver());
		act.moveToElement(wb).perform();
		sleep(1000);
		
		return true;
	}
	
	
	public boolean mClickAndDragMouse(WebElement wb1, WebElement wb2)
	{
		Actions act= new Actions(driver());
		act.clickAndHold(wb1).perform();
		act.moveToElement(wb2).perform();
		act.release(wb2);
		
		return true;
	}
	
	public boolean mClickAndDragMouse(By by1, By by2)
	{
		WebElement wb1= driver().findElement(by1);
		WebElement wb2= driver().findElement(by2);
		
		Actions act= new Actions(driver());
		act.clickAndHold(wb1).perform();
		act.moveToElement(wb2).perform();
		act.release(wb2);
		
		return true;
	}
	
	public boolean mClickAndDragMouse(String xpath1, String xpath2)
	{
		WebElement wb1= driver().findElement(By.xpath(xpath1));
		WebElement wb2= driver().findElement(By.xpath(xpath2));
		
		Actions act= new Actions(driver());
		act.clickAndHold(wb1).perform();
		act.release(wb2);
		
		return true;
	}
	
	public boolean mWaitTillElementLocated(By by, int timeInSeconds)
	{
		WebDriverWait wait= new WebDriverWait(driver(), timeInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		
		return true;
	}
	
	public boolean mWaitTillElementTextLocated(String text, int timeInSeconds)
	{
		WebDriverWait wait= new WebDriverWait(driver(), timeInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='"+text+"']")));
		
		return true;
	}
	
	public boolean mWaitTillElementVisible(By by, int timeInSeconds)
	{
		WebDriverWait wait= new WebDriverWait(driver(), timeInSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		
		return true;
	}
	
	public boolean mWaitTillElementVisible(WebElement element, int timeInSeconds)
	{
		boolean flag= false;
		for(int i = 0; i<timeInSeconds; i++)
		{
			if(element.isDisplayed())
			{
				flag=true;
				break;
			}
			
			else
			{
				sleep(1000);
			}
		}
		
		return flag;
	}
	
	public boolean mWaitTillElementClickable(By by, int timeInSeconds)
	{
		WebDriverWait wait= new WebDriverWait(driver(), timeInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(by));
		
		return true;
	}
	
	
	
	public boolean mWaitTillElementDisappear(By by, int timeInSeconds)
	{
		WebDriverWait wait= new WebDriverWait(driver(), timeInSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		
		return true;
	}
	
	public void mSetWaitForPageLoad(int timeInSeconds)
	{
		driver().manage().timeouts().pageLoadTimeout(timeInSeconds, TimeUnit.SECONDS);
	}
	
	public void mSetImplicitWaitTime(int timeInSeconds)
	{
		driver().manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS);
	}
	
	public void mStaticWait(int timeInSeconds) throws Exception
	{
		driver().manage().timeouts().wait(timeInSeconds*1000);
	}
	
	
	
	public boolean mIsElementTextPresent(String text)
	{
		if(mIsElementPresent("//*[normalize-space(text())='"+text+"']"))
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public  void mWaitTillPageLoad(int timeInSeconds)
	{
		ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        
            WebDriverWait wait = new WebDriverWait(driver(), 30);
            wait.until(expectation);
        
    }
	
	/*public  void mWaitTillPageNavigateTo(String pageTitle, int timeInSeconds) throws Exception
	{
		boolean flag= false;
		for(int i=0; i<(timeInSeconds*1000);i++)
		{
			
				if(driver().getTitle().contains(pageTitle))
				{
					flag=true;
					break;
				}
			
				else{
					sleep(1000);
				}
		}
		
		if(!flag)
		{
			logResult("Pass", "Page Could not navigate to page: '"+pageTitle+"'", "", "Yes");
		}
    }*/
	
	public  String mGetPageTitle()
	{
		return driver().getTitle();
	}
	
	public String mReadValueFromSelectBox(String fieldName)
	{
		WebElement selectBox=null;
		Select sel=null;
		String value= "";
		
		selectBox= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::select)[1]"));
		
		sel= new Select(selectBox);
		
		value= sel.getFirstSelectedOption().getText().trim();
		
		
		
		
		
		return value;
		
		
		
	}
	
	public void mSetValueForTextBox(String fieldName, String fieldVal)
	{
		WebElement txtBox=null;
		
		txtBox= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::input)[1]"));
		
		txtBox.clear();
		
		txtBox.sendKeys(fieldVal);
		
		
	}
	
	public void mClickOnButtonWithValue(String button)
	{

		WebElement btn= driver().findElement(By.xpath("//button[normalize-space(@value)='"+button+"']"));
		
		if(!mIsElementDisplayed("//button[normalize-space(@value)='"+button+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//button[normalize-space(@value)='"+button+"']")));
		}
		
		mJSClick(btn);
		
		sleep(4000);
		
	}
	
	public void mClickOnButtonWithText(String button)
	{
		
		WebElement btn= driver().findElement(By.xpath("//button[normalize-space(text())='"+button+"']"));
		
		if(!mIsElementDisplayed("//button[normalize-space(text())='"+button+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//button[normalize-space(text())='"+button+"']")));
		}
		
		mJSClick(btn);
		
		sleep(4000);
		
	}
	
	public void mClickOnButtonWithID(String button)
	{
		
		WebElement btn= driver().findElement(By.xpath("//button[normalize-space(@id)='"+button+"']"));
		
		//mExecuteJS("arguments[0].scrollIntoView(true);", btn);
		if(!mIsElementDisplayed("//button[normalize-space(@id)='"+button+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//button[normalize-space(@id)='"+button+"']")));
		}
		
		
		mJSClick(btn);
		
		sleep(4000);
		
		
		
	}
	
	
	
	public void mClickOnTab(String tabName)
	{
		if(!mIsElementDisplayed("//*[normalize-space(text())='"+tabName+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//*[normalize-space(text())='"+tabName+"']")));
		}
		
		
		mJSClick(driver().findElement(By.xpath("//*[normalize-space(text())='"+tabName+"']")));
		sleep(3000);
		
	}
	
	public void mClickOnLinkWithText(String linkText)
	{
		if(!mIsElementDisplayed("//a[normalize-space(text())='"+linkText+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//a[normalize-space(text())='"+linkText+"']")));
		}
		
		mJSClick(driver().findElement(By.xpath("//a[normalize-space(text())='"+linkText+"']")));
		
		sleep(3000);
		
	}
	
	public void mClickOnLinkWithTitle(String title)
	{
		if(!mIsElementDisplayed("//a[normalize-space(@title)='"+title+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//a[normalize-space(@title)='"+title+"']")));
		}
		
		mJSClick(driver().findElement(By.xpath("//a[normalize-space(@title)='"+title+"']")));
		
		sleep(3000);
		
	}
	
	public void mClickOnElementWithID(String id)
	{
		if(!mIsElementDisplayed("//*[normalize-space(@id)='"+id+"']"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//*[normalize-space(@id)='"+id+"']")));
		}
		
		
		mJSClick(driver().findElement(By.xpath("//*[normalize-space(@id)='"+id+"']")));
		
		sleep(3000);
		
	}
	
	public void mClickOnElementWithPartialText(String text)
	{
		if(!mIsElementDisplayed("//*[contains(text(),'"+text+"')]"))
		{
		
			mClickOnElementWithScroll(driver().findElement(By.xpath("//*[contains(text(),'"+text+"')]")));
		}
		
		
		mJSClick(driver().findElement(By.xpath("//*[contains(text(),'"+text+"')]")));
		sleep(3000);
		
	}
	
	public String mReadValueFromTextBox(String fieldName)
	{
		
		WebElement txtBox=null;
		String value="";
		
			txtBox= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::input)[1]"));
			value= txtBox.getAttribute("value").trim();
		
	
		return value;
	}
	
	
	public String readValueFromDateSelectBox(String fieldName)
	{
		
		WebElement monthWE= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::select)[1]"));
		WebElement dayWE= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::select)[2]"));
		WebElement yearWE= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::select)[3]"));
		
		Select sel= new Select(monthWE);
		
		String month= sel.getFirstSelectedOption().getText().trim();
		
		sel= new Select(dayWE);
		
		String day= sel.getFirstSelectedOption().getText().trim();
		
		sel= new Select(yearWE);
		
		String year= sel.getFirstSelectedOption().getText().trim();
		
		return month+'/'+day+'/'+year;
	}
	
	public void mSelectFromSelectBoxBy(String type,String fieldName, String value) throws Exception {
		
		
		WebElement wb= driver().findElement(By.xpath("(//*[normalize-space(text())='"+fieldName+"']//following::select)[1]"));
	
		Select sel= new Select(wb);
		
		if(type.equalsIgnoreCase("index"))
		{
			sel.selectByIndex(Integer.parseInt(value));
		}
		
		else if(type.equalsIgnoreCase("value"))
		{
			sel.selectByValue(value);
		}
		
		else if(type.equalsIgnoreCase("text"))
		{
			sel.selectByVisibleText(value);
		}
		
		
		
	}
	
	public void mSelectFromSelectBoxByPartialText(WebElement wb, String value) {
		
		Select sel= new Select(wb);
			
			List<WebElement>listOfOptions=sel.getOptions();
			
			int count=1;
			
			for(int i=0;i<listOfOptions.size();i++)
			{
				sel.selectByIndex(i);
				
				List<WebElement> selectedOption= sel.getAllSelectedOptions();
				
				String option= selectedOption.get(0).getText();
				if(option.contains(value))
				{
					break;
				}
				
				count++;
				if(count>listOfOptions.size())
				{
					Assert.fail("The Option : '"+value+"' is not available for the Select box: '"+wb.getAttribute("id")+"'");
				}
			}
			
			
	}
	
	public boolean mIsElementTextVisible(String text)
	{
		if(mIsElementDisplayed("//*[normalize-space(text())='"+text+"']"))
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public  boolean mIsElementDisplayed(String xpath)
	{
		if(mIsElementPresent(xpath))
		{
			return driver().findElement(By.xpath(xpath)).isDisplayed();
		}
		
		else
		{
			return false;
		}
	}
	
	public  void mAcceptAlert(String alertMessage)
	{
		Alert alert= driver().switchTo().alert();
		
		Assert.assertTrue(alert.getText().trim().contains(alertMessage), "The Alert with Message: '"+alertMessage+"' is not available. Available Alert Message: '"+alert.getText().trim()+"'");
		
		alert.accept();
		
	
	}
	
	public  void mCancelAlert(String alertMessage)
	{
		Alert alert= driver().switchTo().alert();
		
		Assert.assertTrue(alert.getText().trim().contains(alertMessage), "The Alert with Message: '"+alertMessage+"' is not available. Available Alert Message: '"+alert.getText().trim()+"'");
		
		alert.dismiss();
	
	}
	
	public  void mClickOnElementWithScroll(WebElement ele) {
		mExecuteJS("arguments[0].scrollIntoView(true);", ele);
		mClick(ele);
		sleep(1000);
	}
	
	public  void mScrollToElement(WebElement ele) {
		mExecuteJS("arguments[0].scrollIntoView(true);", ele);
		sleep(1000);
	}
	
	public  void mScrollToElement(String xpath) {
		mExecuteJS("arguments[0].scrollIntoView(true);", driver().findElement(By.xpath(xpath)));
		sleep(1000);
	}
	
	public  void mDoubleClickOnElement(WebElement ele) throws Exception {
		Actions act= new Actions(driver());
		
		
		act.doubleClick(ele).perform();
		
		sleep(2000);
		
		
	}
	
	public  String mGetCurrentDate() {
		  
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
	    String today = formatter.format(date);
		return today;
	    
	}
	
	
	public  String mReadTextFromElement(WebElement wbElmnt)
	{
		try {
			return wbElmnt.getText().trim();
		}
		
		catch(Exception e)
		{
			return "";
		}
	}
	
	public  String mReadTextFromElement(By by)
	{
		WebElement wbElmnt= driver().findElement(by);
		try {
			return wbElmnt.getText().trim();
		}
		
		catch(Exception e)
		{
			return "";
		}
	}
	
	public  String mReadTextFromElement(String xpath)
	{
		WebElement wbElmnt= driver().findElement(By.xpath(xpath));
		try {
			return wbElmnt.getText().trim();
		}
		
		catch(Exception e)
		{
			return "";
		}
		
	}
	
	public  void mSelectFromSelectBoxBy(String type,WebElement wb, String value) throws Exception {
		
		
		Select sel= new Select(wb);
		
		if(type.equalsIgnoreCase("index"))
		{
			sel.selectByIndex(Integer.parseInt(value));
		}
		
		else if(type.equalsIgnoreCase("value"))
		{
			sel.selectByValue(value);
		}
		
		else if(type.equalsIgnoreCase("text"))
		{
			sel.selectByVisibleText(value);
		}
		
		
	}
	
	public  void mSetValueForTextBox(WebElement txtBox, String fieldVal)
	{
		
		txtBox.clear();
		
		txtBox.sendKeys(fieldVal);
		
	}
	
	public  void mSetValueForTextBox(WebElement txtBox, String fieldVal, Keys key)
	{
		
		txtBox.clear();
		
		txtBox.sendKeys(fieldVal,key);
		
	}
	
	public  String mReadValueFromSelectBox(WebElement selectBox) throws Exception
	{
		Select sel=null;
		
		sel= new Select(selectBox);
		
		return sel.getFirstSelectedOption().getText().trim();
		
		
	}
	
	public  String mReadValueFromTextBox(WebElement txtBox) throws Exception
	{
		String s="";
		
		s=  txtBox.getAttribute("value").trim();
		
		return s;
	}
	
	
	
	public  boolean mWaitTillElementPresent(By by, int timeInSeconds)
	{
		try{
		WebDriverWait wait= new WebDriverWait(driver(), timeInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		
		return true;
		}
		
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	
	public  String mGetPageURL()
	{
		return driver().getCurrentUrl();
	}
	
	public  boolean mSwitchToWindow(String title)
	{
		boolean flag = false;
		
		for(int i=1; i<=10; i++)
		{	for(String handle: driver().getWindowHandles())
			{
				driver().switchTo().window(handle);
				System.out.println("Current Page Title : "+mGetPageTitle());
				if(mGetPageTitle().contains(title))
				{
					System.out.println("Now Switched To :"+mGetPageTitle());
					flag = true;
					break;
				}
				
			}
		
			if(flag)
			{
				break;
			}
		}
		return flag;
	}
	public void mSwitchToWindowWithPageTitle(String Title)
	{
		String currentWindow = driver().getWindowHandle();  //will keep current window to switch back
		for(String winHandle : driver().getWindowHandles()){
		   if (driver().switchTo().window(winHandle).getTitle().equals(Title)) {
		     //This is the one you're looking for
		     break;
		   } 
		   else {
		      driver().switchTo().window(currentWindow);
		   } 
		}
	}
	public void mCloseCurrentWindowAndNavigateToParent()
	{
		String parentWindow="";
		String currentWindow= driver().getWindowHandle();
		Set<String> allWindows= driver().getWindowHandles();
		
		Iterator it= allWindows.iterator();
		parentWindow= it.next().toString();
		if(it.hasNext())
		{
			driver().switchTo().window(parentWindow);
			String winId= it.next().toString();
			driver().switchTo().window(winId);
			if(winId.equals(currentWindow))
			{
				driver().close();
				it.remove();
			}
		}
		
		driver().switchTo().window(parentWindow);	
		
	}

	
	public String mGetRandomNumber(int lengthMaxUpto10) {
		//String value = UUID.randomUUID().toString().replace("-", "");
		String value= String.valueOf(lengthMaxUpto10 < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, lengthMaxUpto10 - 1)) - 1)
                + (int) Math.pow(10, lengthMaxUpto10 - 1));
		return value;
	}

	public String mGetRandomString(int length, boolean includeLetters, boolean includeNumbers) {
		String value = RandomStringUtils.random(length, includeLetters, includeNumbers);
		return value;
	} 
	
	
	public boolean mWaitTillPageNavigateTo(String title, int time)
	{
		int count=0;
		while(!mGetPageTitle().contains(title) && count<time)
		{
			sleep(1000);
			count++;
		}
		
		if(!mGetPageTitle().contains(title) && count==time)
		{
			return false;
		}
		
		return true;
	}
	
	public void NavigateToPreviousPageInApplication()
	{
		driver().navigate().back();
		driver().navigate().refresh();
	}
	

}
