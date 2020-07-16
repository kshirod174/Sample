package frameworkFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;

/**
 * Implementing the TestNG listener to customize the console out put and also
 * push the execution results to a excel file with case ID and test methods
 * 
 * 
 * 
 */
public class ReportManager extends BaseTestSuite {

	String testName=null;
	
	public ReportManager(String testName)
	{
		this.testName= testName;
	}
	
	
	public int row;
	static String outputReportDir= "d:\\Screen";
	private int m_passCount = 0;
	private int m_failCount = 0;
	private int m_skipCount = 0;
	String url = null;
	String browser = null;
	String todaydate;
	 
	// Log the test case details and result to a csv file
	
	static File dirPath;
	//static WritableWorkbook workbook ;
	
	static String fileName;
	static File directory;
	File file;
	
	public void onStart() throws IOException {

		 java.util.Date date= new java.util.Date();
		fileName = testName+".xls";

	    //Saving file in external storage
	    
	    directory = new File(BaseTestSuite.resultLocation+"Reports");

	    //create directory if not exist
	    if(!directory.isDirectory()){
	        directory.mkdirs();
	    }

	    //file path
	    
	    WorkbookSettings wbSettings = new WorkbookSettings();
	    wbSettings.setLocale(new Locale("en", "EN"));
	    
	    
	    	file = new File(directory, fileName);
	   
	    	workbook.set(Workbook.createWorkbook(file, wbSettings));
	    
	        
	    //Excel sheet name. 0 represents first sheet
	    sheet.set(workbook.get().createSheet("result", 0));



	        //Cursor  cursor = mydb.rawQuery("select * from Contact", null);

	        try {
	        	
	        	String Header[] = new String[7];            
				 Header[0] = "TEST_CASE NAME";            
				 Header[1] = "BROWSER";           
				 Header[2] = "STEPS";            
				 Header[3] = "STATUS";
				 Header[4] = "COMMENTS";
				 //Header[5] = "DURATION(secs)";
				 Header[5] = "SCREENSHOTS";
				 
				 //Setting Background colour for Cells            
				 Colour bckcolor = Colour.AQUA;           
				 WritableCellFormat cellFormat = new WritableCellFormat();            
				 cellFormat.setBackground(bckcolor);            
				 //Setting Colour & Font for the Text             
				 WritableFont font = new WritableFont(WritableFont.ARIAL);            
				 font.setColour(Colour.BLACK);
				 cellFormat.setFont(font); 
				// cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
				 // Write the Header to the excel file            
				 for (int i = 0; i < Header.length; i++) {               
					 Label label = new Label(i, 0, Header[i],createFormattedCell(10, null, true,
								false,  null,Border.ALL, BorderLineStyle.THIN,
								null));                
					 sheet.get().addCell(label);               
					 WritableCell cell = sheet.get().getWritableCell(i, 0);               
					 cell.setCellFormat(cellFormat);  
					 }
	        
	        	
	            
	        	
	           
	        } catch (RowsExceededException e) {
	            e.printStackTrace();
	        } catch (WriteException e) {
	            e.printStackTrace(); }
	    }
	      
	    
		
	 

	
	public void onFinish() {
		try {
			workbook.get().write();
			workbook.get().close(); 
		} catch (IOException e) {
		
			e.printStackTrace();
		} 
		
		 catch (WriteException e) {
				
				e.printStackTrace();
			}  
		
		
		  
		/*(new PrintWriter(m_csvWriter)).close();*/
	}

	
	

	
	
	public WritableCellFormat createFormattedCell(int pointSize,
			jxl.write.WritableFont.FontName fontName, boolean isBold,
			boolean italic, UnderlineStyle underLineStyle, Border border,
			BorderLineStyle lineStyle, Alignment alignment) {
		WritableFont font = new WritableFont(null != fontName ? fontName
				: jxl.write.WritableFont.TIMES, pointSize,
				isBold==true ? WritableFont.BOLD : WritableFont.NO_BOLD, italic,
				null != underLineStyle ? underLineStyle
						: UnderlineStyle.NO_UNDERLINE);

		try {
			font.setColour(jxl.format.Colour.BLACK);
		} catch (WriteException e1) {
			System.out.println(e1.getMessage());
		}

		WritableCellFormat writableCellFormat = new WritableCellFormat(font);
		if (null == lineStyle) {
			lineStyle = BorderLineStyle.HAIR;
		}
		if (null == border) {
			border = Border.NONE;
		}
		if (null == alignment) {
			alignment = Alignment.CENTRE;
		}
		try {
			writableCellFormat.setBorder(border, lineStyle);
			writableCellFormat.setAlignment(alignment);
			//writableCellFormat.setBackground(jxl.format.Colour.YELLOW);
		} catch (WriteException e) {
			System.out.println(e.getMessage());
		}
		return writableCellFormat;
	}
	
	
	public void writeToReport(String tc, String browsername)throws Exception//, String status, String comment, String date) throws Exception
	{
		 try {
			 	row= sheet.get().getRows()+1;
			 	sheet.get().addCell(new Label(0, row, tc));
			 	sheet.get().addCell(new Label(1, row, browsername));
		 } 	
		 catch (RowsExceededException e) {
	            e.printStackTrace();
	        } catch (WriteException e) {
	            e.printStackTrace();
	        }
	     
	 } 
	    
	public void reportResult(String step, String status, String... comment) throws Exception//, String status, String comment, String date) throws Exception
	{
		 try {
			 	
			 	sheet.get().addCell(new Label(2, row, step));
			 	
			 	sheet.get().addCell(new Label(3, row, status));
			 	
			 	WritableCellFormat cellFormat = new WritableCellFormat();
			 	WritableFont font = new WritableFont(WritableFont.ARIAL);
			 	font.setBoldStyle(WritableFont.BOLD);
			 	if(status.equalsIgnoreCase("fail"))
			 	{
			 		font.setColour(Colour.RED);
			 	}
			 	else if(status.equalsIgnoreCase("pass"))
			 	{
			 		font.setColour(Colour.GREEN);
			 	}
			 	cellFormat.setFont(font);
			 	cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			 	WritableCell cell = sheet.get().getWritableCell(3, row);
			 	cell.setCellFormat(cellFormat);
			 	
			 	 if (comment.length > 0) 
			 	 {
			         if (comment[0] != null) 
			         {
			        	 sheet.get().addCell(new Label(4, row, comment[0]));
			         }
			 	 }
			            
			 	//sheet.addCell(new Label(5, row, time));
			 	
		 	}
		  catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	 } 
		 

	public void reportScreenShot(String sreenshotName) throws Exception//, String status, String comment, String date) throws Exception
	{
		 try {
			 
			 	WritableHyperlink hlk =new WritableHyperlink(5, row,  5, row, new File("../Screenshots/"+sreenshotName));          
			 	sheet.get().addHyperlink(hlk);                                                                   
			 	WritableCellFormat cellFormat = new WritableCellFormat();
			 	WritableFont font = new WritableFont(WritableFont.ARIAL);
			 	font.setBoldStyle(WritableFont.BOLD);
			 	font.setColour(Colour.BLUE);
			 	
			 	cellFormat.setFont(font);
			 	cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			 	WritableCell cell = sheet.get().getWritableCell(5, row);
			 	cell.setCellFormat(cellFormat);
			 	
			 	
			 	
		 	}
		  catch (Exception e) {
	            e.printStackTrace();
	        }
	      
	 } 
		
	

}
