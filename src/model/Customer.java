package model;

public class Customer {

	public String cnpj;
	public String name;
	public String business_area;
	
	public Customer(String cnpj, String name, String business_area) {
		this.cnpj = cnpj;
		this.name = name;
		this.business_area = business_area;
	}
}
