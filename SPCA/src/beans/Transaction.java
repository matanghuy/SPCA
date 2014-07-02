package beans;

import javafx.beans.property.SimpleStringProperty;

public class Transaction {
	private SimpleStringProperty ContactName;
	private SimpleStringProperty TransactionDate;
	private SimpleStringProperty TotalAmountPayed;
	

	private SimpleStringProperty TotalAmountToPay;
	private SimpleStringProperty TransactionTypeName;
	private SimpleStringProperty Comments;
	
	public Transaction(){
		this.ContactName = new SimpleStringProperty();
		this.TotalAmountPayed = new SimpleStringProperty();
		this.TotalAmountToPay = new SimpleStringProperty();
		this.TransactionDate = new SimpleStringProperty();
		this.TransactionTypeName = new SimpleStringProperty();
		this.Comments = new SimpleStringProperty();
	}

	public String getComments() {
		return  Comments.get() == null ? "": Comments.get();
	}

	public void setComments(String Comments) {
		this.Comments.set(Comments);
	}
	
	
	public String getContactName() {
		return ContactName.get() == null ? "": ContactName.get();
	}

	public void setContactName(String contactName) {
		this.ContactName.set(contactName);
	}

	public String getTransactionDate() {
		return TransactionDate.get()  == null ? "0": TransactionDate.get();
	}

	public void setTransactionDate(String transactionDate) {
		this.TransactionDate.set(transactionDate);
	}

	public String getTotalAmountPayed() {
		return TotalAmountPayed.get()  == null ? "0": TotalAmountPayed.get()  ;
	}

	public void setTotalAmountPayed(String totalAmountPayed) {
		this.TotalAmountPayed.set(totalAmountPayed);
	}

	public String getTotalAmountToPay() {
		return TotalAmountToPay.get()  == null ? "0" : TotalAmountToPay.get()  ;
	}

	public void setTotalAmountToPay(String totalAmountToPay) {
		this.TotalAmountToPay.set(totalAmountToPay);
	}

	public String getTransactionTypeName() {
		return TransactionTypeName.get()  == null ? "": TransactionTypeName.get();
	}

	public void setTransactionTypeName(String transactionTypeName) {
		this.TransactionTypeName.set(transactionTypeName);
	}
	
	@Override
	public String toString() {
		return "Transaction [ContactName=" + ContactName + ", TransactionDate="
				+ TransactionDate + ", TotalAmountPayed=" + TotalAmountPayed
				+ ", TotalAmountToPay=" + TotalAmountToPay
				+ ", TransactionTypeName=" + TransactionTypeName + "]";
	}

}
