package model;

import java.util.ArrayList;

public class Sale {
	public String id;
	public ArrayList<ArrayList<String>> item_info;
	public String salesman;
	
	public Sale(String id, ArrayList<ArrayList<String>> item_info, String salesman) {
		this.id = id;
		this.item_info = item_info;
		this.salesman = salesman;
	}
}
