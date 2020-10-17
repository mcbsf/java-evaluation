package model;

import java.util.ArrayList;

public class Sale {
	
	public String id;
	public ArrayList<ArrayList<String>> item_info;
	public String salesman;
	
	public float amount;
	
	public Sale(String id, ArrayList<ArrayList<String>> item_info, String salesman) {
		this.id = id;
		this.item_info = item_info;
		this.salesman = salesman;
	}
	
	public float get_amount() {
		float total_amount = 0;
		for(ArrayList<String> item: item_info) {
			total_amount+= Float.parseFloat(item.get(2));
		}
		return total_amount;
	}
	
	public void simplify_amount() {
		this.amount = get_amount();
	}
}
