package model;

public class Sale {
	public String id;
	public String[] item_info;
	public String salesman;
	
	public Sale(String id, String[] item_info, String salesman) {
		this.id = id;
		this.item_info = item_info;
		this.salesman = salesman;
	}
}
