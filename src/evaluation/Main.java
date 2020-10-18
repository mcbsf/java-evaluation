package evaluation;

import java.io.File;
import java.util.ArrayList;

import controller.FileController;
import controller.LogController;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while(true) {
			
		
		    try {

		    	String home_path = System.getProperty("user.dir");    
				ArrayList<File> unprocessed_files = FileController.get_unprocessed_files(home_path);
				FileController.process_files(unprocessed_files);
			} 
		    catch (Exception e) {
			
		    	LogController.log_exception(e);
			}

		}
	
	}

}
