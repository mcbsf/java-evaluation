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
import traits.WorstSalesmanGenerator;

//USER REPOSITORY to add each line?????

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
			}
			catch (Exception e) {
				
				LogController.log_file_exception(e);
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
			} 
			catch(Exception e){
				
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

		//iterate over items array to convert them in an ArrayList of items, each item is being described by an ArrayList of Strings
		for(int i = 0; i<splitted_items.length; i++) {
			
			ArrayList<String> item = new ArrayList(Arrays.asList(splitted_items[i].split("-")));
			sale_info.add(item);
		}
		return sale_info;
	}

	private static void generate_output(ArrayList<Salesman> salesmen, ArrayList<Customer> customers, ArrayList<Sale> sales, String file_name) throws IOException{

		//proccess output info
		String customers_amount = String.valueOf(customers.size());
		String salesmen_amount = String.valueOf(salesmen.size());
		String most_expansive_sale_id = get_most_expansive_sale_id(sales);
		String worst_salesman = WorstSalesmanGenerator.get_worst_salesman(sales, salesmen);

		System.out.println("customers: " + customers_amount);
		System.out.println("salesmen: " + salesmen_amount);
		System.out.println("most expansive sale id: " + most_expansive_sale_id);
		System.out.println("worst salesman: " + worst_salesman);
		
		//generate output file and writer
		String home_path = System.getProperty("user.dir");
		String output_path = home_path+"/data/out/"+file_name.replace(".", ".done.");
		System.out.println(output_path);
		File output_file = new File(output_path);
		FileWriter output_writer = new FileWriter(output_path); 
		
		//writing output_info
		output_writer.write(customers_amount+"\n");
		output_writer.write(salesmen_amount+"\n");
		output_writer.write(most_expansive_sale_id+"\n");
		output_writer.write(worst_salesman);

		output_writer.close();



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
