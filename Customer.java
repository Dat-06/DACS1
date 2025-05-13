package org.example;

// model/Customer.java


public class Customer {
	private String id;
	private String name;
	private String address;
	private String phone;
	
	public Customer(String id, String name, String address, String phone) {
		this.id = id; this.name = name; this.address = address; this.phone = phone;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}

