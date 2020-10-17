package controller;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.Customer;
import model.Sale;
import model.Salesman;

//USER REPOSITORY to add each line?????

/*catch (Exception e) {

 *This is a generic Exception handler which means it can handle
 * all the exceptions. This will execute if the exception is not
 * handled by previous catch blocks.
 
System.out.println("Exception occurred");
}*/

// CHAANGE ARRAY LIST TO MAKE BETTER PERFORMANCE? which is better?
public class FileController {
	public static ArrayList<File> get_files(String path) {

	    File folder = new File(path);
	    
		File[] files = folder.listFiles
			(
				(d, name) -> name.endsWith(".dat")
			);
	    
		return new ArrayList<File>(Arrays.asList(files));
	}
	
	public static ArrayList<File> get_processed_files(ArrayList<File> files, String home_path) {
		
		return get_files(home_path+"/data/out");
	}
	
	public static ArrayList<File> get_unprocessed_files(String home_path) {
		
		ArrayList<File> unprocessed_files= new ArrayList<File>();
		
		ArrayList<File> files = get_files(home_path+"/data/in");
	    ArrayList<File> processed_files = get_files(home_path+"/data/out");

		
		for (File file : files){
			System.out.println("checking file: "+ file.getName());
			for(File processed_file: processed_files) {
				//System.out.println("comparing "+ file.getName()+ " with " + processed_file.getName());
				if(!Objects.equals(file.getName(), processed_file.getName())) {
		        	unprocessed_files.add(file);
		        	System.out.println("  unprocessed" );
		        }
		        
			}
	        //System.out.println(file.getName());
	    }
		return unprocessed_files;
	}

	public static void process_file(File file) throws IOException{
		//add line_index to log mapped errors
		System.out.println("\nprocessing file " + file.getName());
		String[] lines = Files.readAllLines(file.toPath()).toArray(new String[0]);

		ArrayList<Customer> customers = new ArrayList();
		ArrayList<Salesman> salesmen= new ArrayList();
		ArrayList<Sale> sales= new ArrayList();
		ArrayList<String[]> others = new ArrayList();
		
		for(String line: lines) {
			System.out.println(line);
			String[] splitted_line = line.split("ç");
			String id = splitted_line[0];
			
			if(Objects.equals(id, "001")) {
				Salesman salesman = new Salesman(splitted_line[1], splitted_line[2], splitted_line[3]);
				salesmen.add(salesman);
				
			}else if(Objects.equals(id, "002")){
				Customer customer = new Customer(splitted_line[1], splitted_line[2], splitted_line[3]);
				customers.add(customer);
				
			}else if(Objects.equals(id, "003")){
				String[] item_info = splitted_line[2].split("-");
				Sale sale = new Sale(splitted_line[1], item_info, splitted_line[3]);
				sales.add(sale);
				
			}else {
				others.add(splitted_line);
			}
			
			for(String attr: splitted_line) {
				System.out.println("\n   attr: " + attr);
			}
			
		}
		
		generate_output(salesmen, customers, sales);
	}
	
	public static ArrayList<File> process_files(ArrayList<File> unprocessed_files) throws IOException{
		System.out.println("processing files \n");
		
		for (File file : unprocessed_files){
		   process_file(file);
		   break;
	    }
		return null;
	}
	
	public static void generate_output(ArrayList<Salesman> salesmen, ArrayList<Customer> customers, ArrayList<Sale> sales) throws IOException{
		
		for(Salesman salesman: salesmen) {
			System.out.println(salesman.name);
		}
		
		for(Customer customer: customers) {
			System.out.println(customer.name);
		}

	}
}
