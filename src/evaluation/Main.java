package evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import controller.FileController;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    String home_path = System.getProperty("user.dir");
	
		    
		ArrayList<File> unprocessed_files = FileController.get_unprocessed_files(home_path);
		
	    try {
			FileController.process_files(unprocessed_files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
