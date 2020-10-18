package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
	public static ArrayList<File> get_files(String path, String data_type) {

	    File folder = new File(path);
	    
		File[] files = folder.listFiles
			(
				(d, name) -> name.endsWith(data_type)
			);
	    
		return new ArrayList<File>(Arrays.asList(files));
	}
	
	public static ArrayList<File> get_processed_files(ArrayList<File> files, String home_path) {
		
		return get_files(home_path+"/data/out", ".dat");
	}
	
	public static ArrayList<File> get_unprocessed_files(String home_path) {
		
		ArrayList<File> unprocessed_files= new ArrayList<File>();
		
		ArrayList<File> files = get_files(home_path+"/data/in", ".dat");
	    ArrayList<File> processed_files = get_files(home_path+"/data/out", ".dat");
	    
		
		for (File file : files){
			boolean isProcessed = false;
			for(File processed_file: processed_files) {
				System.out.println("comparing "+ file.getName().replace(".", ".done.")+ " with " + processed_file.getName());
				if(Objects.equals(file.getName().replace(".", ".done."), processed_file.getName())) {
		        	isProcessed = true;
		        }
		        
			}
			if(!isProcessed) {
				unprocessed_files.add(file);
				System.out.println(file.getName() + " UNPROCESSED");
			}
	        //System.out.println(file.getName());
	    }
		return unprocessed_files;
	}

	

	public static ArrayList<File> process_files(ArrayList<File> unprocessed_files) throws IOException{
		
		for (File file : unprocessed_files){
		   try{
			   process_file(file);
		   }catch (Exception e) {
			   LogController.log_exception(e);
		   }
	    }
		return null;
	}
	
	
	
	
	
	private static void process_file(File file) throws IOException{
		//add line_index to log mapped errors
		System.out.println("\nprocessing file " + file.getName());
		String[] lines = Files.readAllLines(file.toPath()).toArray(new String[0]);
		
		//using linkedList to get better performance
		LinkedList<Customer> customers = new LinkedList();
		LinkedList<Salesman> salesmen= new LinkedList();
		LinkedList<Sale> sales= new LinkedList();
		LinkedList<String[]> others = new LinkedList();
		
		
		
		//iterate over lines to build models and fill local repositories 
		for(String line: lines) {
			try {

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
					
					ArrayList<ArrayList<String>> sale_info = get_sale_info(splitted_line[2]);
					
					Sale sale = new Sale(splitted_line[1], sale_info, splitted_line[3]);
					sale.simplify_amount();
					System.out.println("    SALE "+ String.valueOf(sale.amount) + "\n");
					sales.add(sale);
					
				}else {
					others.add(splitted_line);
				}
			
			
			} catch(Exception e){
				LogController.log_line_exception(e);
			}	
		}
		ArrayList<Customer> arr_customers = new ArrayList(customers);
		ArrayList<Salesman> arr_salesmen= new ArrayList(salesmen);
		ArrayList<Sale> arr_sales= new ArrayList(sales);
		ArrayList<String[]> arr_others = new ArrayList(others);
		
		generate_output(arr_salesmen, arr_customers, arr_sales, file.getName());
	}
	
	private static ArrayList<ArrayList<String>> get_sale_info(String items) {
		
		items = items.replace("[", "").replace("]", "");
		ArrayList<ArrayList<String>> sale_info = new ArrayList(new ArrayList());
		
		String[] splitted_items = items.split(",");
		
		//iterate over items to add in an ArrayList of items, each item is described by an ArrayList
		for(int i = 0; i<splitted_items.length; i++) {
			ArrayList<String> item = new ArrayList(Arrays.asList(splitted_items[i].split("-")));
			sale_info.add(item);
		}
		return sale_info;
	}
	
	private static void generate_output(ArrayList<Salesman> salesmen, ArrayList<Customer> customers, ArrayList<Sale> sales, String file_name) throws IOException{
		
		
		String customers_amount = String.valueOf(customers.size());
		String salesmen_amount = String.valueOf(salesmen.size());
		String most_expansive_sale_id = get_most_expansive_sale_id(sales);
		String worst_salesman = get_worst_salesman(sales, salesmen);
		
		System.out.println("customers: " + customers_amount);
		System.out.println("salesmen: " + salesmen_amount);
		System.out.println("most expansive sale id: " + most_expansive_sale_id);
		System.out.println("worst salesman: " + worst_salesman);

	    String home_path = System.getProperty("user.dir");
	    String output_path = home_path+"/data/out/"+file_name.replace(".", ".done.");
	    System.out.println(output_path);
		File output_file = new File(output_path);
		FileWriter output_writer = new FileWriter(output_path); 
		
		output_writer.write(customers_amount+"\n");
		output_writer.write(salesmen_amount+"\n");
		output_writer.write(most_expansive_sale_id+"\n");
		output_writer.write(worst_salesman);
		
		output_writer.close();
		
	

	}
	
	private static String get_worst_salesman(ArrayList<Sale> sales, ArrayList<Salesman> salesmen) {
		// sum_sales/salary
		Map<String, Float> salesmen_amount = get_salesmen_by_sales_amount(sales);
		Map<String, Float> salesmen_performance = get_performance_by_salesman(salesmen_amount, salesmen);
		
		String worst_salesman_name = "";
		float worst_salesman_performance = 1000000000;
		
		for (Map.Entry<String, Float> entry : salesmen_performance.entrySet()) {
			
			String actual_salesman_name = entry.getKey();
			float actual_performance = entry.getValue();
		    
			if(actual_performance<worst_salesman_performance) {
		    	worst_salesman_performance = actual_performance;
		    	worst_salesman_name = actual_salesman_name;
		    }
			
			System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		return worst_salesman_name;
	}
	
	private static Map<String, Float> get_performance_by_salesman(Map<String, Float> salesmen_amount,
			ArrayList<Salesman> salesmen) {
		
		Map<String, Float> salesmen_performance = new HashMap<String, Float>();
		
		for (Map.Entry<String, Float> entry : salesmen_amount.entrySet()) {
		    String salesman_name = entry.getKey();
		    float amount = entry.getValue();
		    float salary = get_salary_by_salesman_name(salesman_name, salesmen);
		    System.out.println("salesman "+salesman_name); 
		    System.out.println(salary);
		    float performance = amount/salary;
		    salesmen_performance.put(salesman_name, performance);
		}
		return salesmen_performance;
	}
	
	private static float get_salary_by_salesman_name(String name, ArrayList<Salesman>salesmen) {
		float salary = 0;
		for(Salesman salesman:salesmen) {
			if(Objects.equals(salesman.name, name)) {
				return Float.parseFloat(salesman.salary);
			}
		}
		return salary;
	}

	private static Map<String, Float> get_salesmen_by_sales_amount(ArrayList<Sale>sales){
		
		Map<String, Float> salesmen_amount = new HashMap<String, Float>();
		
		for(Sale sale:sales) {
			
			Float actual_amount = salesmen_amount.get(sale.salesman);
			
			if(actual_amount == null) {
			
				salesmen_amount.put(sale.salesman, (float)0);
				actual_amount = (float)0;
			}
			actual_amount += sale.amount;
			salesmen_amount.put(sale.salesman, actual_amount);
		}
		
		return salesmen_amount;
	}

	public static String get_most_expansive_sale_id(ArrayList<Sale> sales) {
		String most_expansive_sale_id = "";
		float most_expansive_sale_amount = 0;
		for(Sale sale: sales) {
			float actual_amount = sale.amount;
			
			if(actual_amount>most_expansive_sale_amount) {
				most_expansive_sale_id = sale.id;
				most_expansive_sale_amount = actual_amount;
			}
		}
		return most_expansive_sale_id;
	}
}
