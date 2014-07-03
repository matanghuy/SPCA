package beans;

import javafx.beans.property.SimpleStringProperty;

public class PaymentReport {
	private SimpleStringProperty name;
	private SimpleStringProperty amount;
	
	public PaymentReport(){
		this.amount = new SimpleStringProperty();
		this.name = new SimpleStringProperty();
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getAmount() {
		return amount.get().equals("null") ? "0" : amount.get();
	}

	public void setAmount(String amount) {
		this.amount.set(amount) ;
	}
	
}
