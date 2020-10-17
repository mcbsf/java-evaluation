package evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import controller.FileController;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    try {
		    
	    	String home_path = System.getProperty("user.dir");
	    	System.out.println("testing");
		    TimeUnit.SECONDS.sleep(3);
		    System.out.println("testing2");
			ArrayList<File> unprocessed_files = FileController.get_unprocessed_files(home_path);
			
			FileController.process_files(unprocessed_files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
