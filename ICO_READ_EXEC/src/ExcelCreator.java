import java.awt.Button;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ExcelCreator {

	public File getSaveLocation() {
		   JFileChooser chooser = new JFileChooser();
		   chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
		   int result = chooser.showSaveDialog(null);

		   if (result == chooser.APPROVE_OPTION) { 
		      return chooser.getSelectedFile();
		   } else {
		      return null;
		   }
		}
	
	public void createExcel(String Text){
		
		Workbook wb = new HSSFWorkbook();
        CreationHelper helper = wb.getCreationHelper();
        Sheet spreadsheet_1 = wb.createSheet("ICO List");
        ArrayList<String> arrRows = new ArrayList<String>(Arrays.asList(Text.split("\n")));

	    for (int i = 0; i < arrRows.size(); i++) {

	        Row row = spreadsheet_1.createRow(i);

	        ArrayList<String> arrElement = new ArrayList<String>(Arrays.asList(arrRows.get(i).split(",")));

	        for(int j = 0; j < arrElement.size(); j++) {

	            Cell cell = row.createCell(j);
	            cell.setCellValue(arrElement.get(j));

	        }           
	    }
	
	    FileOutputStream out;
		try {
			
		File fileOut = new File(getSaveLocation().toString()+".xls");
			
			
			if (fileOut.exists()) {
			    int response = JOptionPane.showConfirmDialog(null, //
			            "Do you want to replace the existing file?", //
			            "Confirm", JOptionPane.YES_NO_OPTION, //
			            JOptionPane.QUESTION_MESSAGE);
			    if (response != JOptionPane.YES_OPTION) {
			        return;
			    } 
			}
			else
			{
				out = new FileOutputStream(fileOut);
			wb.close();
			   wb.write(out);
			    out.close();
			    
		}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		
	}
	public static void main(String[] args) throws IOException {
		

		
		    
		    String Text = "Sender Component,Sender Interface,Sender NS,Receiver Party,Receiver Component " +"\n"
		    		+ "eCATT4PI_Sender,BookingOrderPayloadMod_Out,http://sap.com/xi/ECATT4PI/Extended/,,eCATT4PI_Receiver" +"\n"
		    		+"Test1,Multi_Mapping_Out,http://d058359/test,,Test2";
		    ExcelCreator excel = new ExcelCreator();
		excel.createExcel(Text);
	  
	}

	

}
