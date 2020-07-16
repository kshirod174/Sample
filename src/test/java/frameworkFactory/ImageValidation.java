package frameworkFactory;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.im4java.process.StandardStream;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImageValidation {
	
	private String testName;
	
	
	
	//Main Directory of the test code
	private String currentDir = System.getProperty("user.dir");
    
  //Test Screenshot directory
	private String testScreenShotDirectory;

    //Main screenshot directory
	private String parentScreenShotsLocation = currentDir + "\\Element_Screenshots\\";

    //Main differences directory
	private String parentDifferencesLocation = currentDir + "\\Differences\\";
    
  //Element screenshot paths
	private String baselineScreenShotPath;
	private String actualScreenShotPath;
	private String differenceScreenShotPath;

    //Image files
	private File baselineImageFile;
	private File actualImageFile;
	private File differenceImageFile;
	private File differenceFileForParent;
    
    
    public void validateImage(WebDriver driver, WebElement element, String ElementName) throws Exception
    {
    	testName= ElementName;
    	setup();
    	
    	//Take ScreenShot with AShot
        Screenshot ScreenShot = takeScreenshot(driver,element);
 
        //Write actual screenshot to the actual screenshot path
        writeScreenshotToFolder(ScreenShot, actualImageFile);
 
        //Do image comparison
        doComparison(ScreenShot);
    	
    }
    
    
    public void validateImage(String actualImgPath, String ExpectedImagePath, String ExpectedName) throws Exception
    {
    	testName= ExpectedName;
    	setup();
    	
 
    	compareImagesWithImageMagick(actualImgPath, ExpectedImagePath, differenceScreenShotPath); 
    	
    }
    
    
    
    private void setup() throws IOException {
    	
        //Create screenshot and differences folders if they are not exist
        createFolder(parentScreenShotsLocation);
        createFolder(parentDifferencesLocation);
        

        //Create a specific directory for a test
        testScreenShotDirectory = parentScreenShotsLocation + testName + "\\";
        createFolder(testScreenShotDirectory);

        //Declare element screenshot paths
        //Concatenate with the test name.
        declareScreenShotPaths(testName+"_Baseline.png", testName+"_Actual.png", testName + "_Diff.png");

    }
    
  //Create Folder Method
    private void createFolder (String path) {
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + path + " is created!" );
            } else {
                System.out.println("Failed to create directory: " + path);
            }
        } else {
            System.out.println("Directory already exists: " + path);
        }
    }
    
  //Take Screenshot with AShot
    private Screenshot takeScreenshot (WebDriver driver, WebElement element) {
        //Take screenshot with Ashot
        Screenshot elementScreenShot = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(driver,element);

        //Print element size
        String size = "Height: " + elementScreenShot.getImage().getHeight() + "\n" +
                "Width: " + elementScreenShot.getImage().getWidth() + "\n";
        System.out.print("Size: " + size);

        return elementScreenShot;
    }
    
    
    //Write
    private void writeScreenshotToFolder (Screenshot screenshot, File imageFile) throws IOException {
        ImageIO.write(screenshot.getImage(), "PNG", imageFile);
    }

    //Screenshot paths
    private void declareScreenShotPaths (String baseline, String actual, String diff) {
        //BaseLine, Actual, Difference Photo Paths
        baselineScreenShotPath = testScreenShotDirectory + baseline;
        actualScreenShotPath = testScreenShotDirectory + actual;
        differenceScreenShotPath = testScreenShotDirectory + diff;

        //BaseLine, Actual Photo Files
        baselineImageFile = new File(baselineScreenShotPath);
        actualImageFile = new File(actualScreenShotPath);
        differenceImageFile = new File (differenceScreenShotPath);

        //For copying difference to the parent Difference Folder
        differenceFileForParent = new File (parentDifferencesLocation + diff);
    }

    //ImageMagick Compare Method
    private void compareImagesWithImageMagick (String expected, String actual, String difference) throws Exception {
        // This class implements the processing of os-commands using a ProcessBuilder.
        // This is the core class of the im4java-library where all the magic takes place.
        ProcessStarter.setGlobalSearchPath("C:\\Users\\1265811\\Downloads\\ImageMagick-7.0.8-15-portable-Q16-x86");

        // This instance wraps the compare command
        CompareCmd compare = new CompareCmd();

        // Set the ErrorConsumer for the stderr of the ProcessStarter.
        compare.setErrorConsumer(StandardStream.STDERR);

        // Create ImageMagick Operation Object
        IMOperation cmpOp = new IMOperation();

        //Add option -fuzz to the ImageMagick commandline
        //With Fuzz we can ignore small changes
        cmpOp.fuzz(10.0);

        //The special "-metric" setting of 'AE' (short for "Absolute Error" count), will report (to standard error),
        //a count of the actual number of pixels that were masked, at the current fuzz factor.
        cmpOp.metric("AE");

        // Add the expected image
        cmpOp.addImage(expected);

        // Add the actual image
        cmpOp.addImage(actual);

        // This stores the difference
        cmpOp.addImage(difference);

        try {
            //Do the compare
            System.out.println ("Comparison Started!");
            compare.run(cmpOp);
            System.out.println ("Comparison Finished!");
        }
        catch (Exception ex) {
            System.out.print(ex);
            System.out.println ("Comparison Failed!");
            //Put the difference image to the global differences folder
            Files.copy(differenceImageFile,differenceFileForParent);
            throw ex;
        }
    }

    //Compare Operation
    private void doComparison (Screenshot elementScreenShot) throws Exception {
        //Did we capture baseline image before?
        if (baselineImageFile.exists()){
            //Compare screenshot with baseline
            System.out.println("Comparison method will be called!\n");

            System.out.println("Baseline: " + baselineScreenShotPath + "\n" +
                    "Actual: " + actualScreenShotPath + "\n" +
                    "Diff: " + differenceScreenShotPath);

            //Try to use IM4Java for comparison
            compareImagesWithImageMagick(baselineScreenShotPath, actualScreenShotPath, differenceScreenShotPath);
        } else {
            System.out.println("BaselineScreenshot is not exist! We put it into test screenshot folder.\n");
            //Put the screenshot to the specified folder
            ImageIO.write(elementScreenShot.getImage(), "PNG", baselineImageFile);
        }
    }

}
