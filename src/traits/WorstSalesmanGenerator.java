package traits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.Sale;
import model.Salesman;

public class WorstSalesmanGenerator {
	
	//Class to receive sales and salesman, and return the salesman with the worst performance(sales_amount/salary)
	public static String get_worst_salesman(ArrayList<Sale> sales, ArrayList<Salesman> salesmen) {
		
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
			float performance = amount/salary;
			
			salesmen_performance.put(salesman_name, performance);
			System.out.println("salesman "+salesman_name); 
			System.out.println(salary);
			
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
}
