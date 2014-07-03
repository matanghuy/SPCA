package beans;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty phone1;
	private SimpleStringProperty phone2;
	private SimpleStringProperty email1;
	private SimpleStringProperty email2;
	private SimpleStringProperty address;
	private SimpleStringProperty city;
	private SimpleStringProperty type;
    private SimpleStringProperty birthDay;

	private int id;
	
	public Contact() {	
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		phone1 = new SimpleStringProperty();
		phone2 = new SimpleStringProperty();
		email1 = new SimpleStringProperty();
		email2 = new SimpleStringProperty();
		address = new SimpleStringProperty();
		city = new SimpleStringProperty();
		type = new SimpleStringProperty();
        birthDay = new SimpleStringProperty();
		
	}


	public String getFirstName() {
		return firstName.get();
	}
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	public String getLastName() {
		return lastName.get();
	}
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	public String getPhone1() {
		return phone1.get() == null ? " " : phone1.get();
	}
	public void setPhone1(String phone1) {
		this.phone1.set(phone1);
	}
	public String getPhone2() {
        return phone2.get() == null ? " " : phone2.get();
	}
	public void setPhone2(String phone2) {
		this.phone2.set(phone2);
	}
	public String getEmail1() {
		return email1.get() == null ? " " : email1.get();
	}
	public void setEmail1(String email1) {
		this.email1.set(email1);
	}
	public String getEmail2() {
        return email2.get() == null ? " " : email2.get();
	}
	public void setEmail2(String email2) {
		this.email2.set(email2);
	}
	public String getAddress() {
		return address.get();
	}
	public void setAddress(String address) {
		this.address.set(address);
	}
	public String getCity() {
		return city.get();
	}
	public void setCity(String city) {
		this.city.set(city);
	}
	public String getType() {
		return type.get();
	}
	public void setType(String type) {
		this.type.set(type);
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
    public String getBirthDay() {
        return birthDay.get();
    }
    public void setBirthDay(String birthDay) {
        this.birthDay.set(birthDay);
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
    public String getFullAddress() {
        return getAddress() + ", " + getCity();
    }
    public String getPhoneNumbers() {
        return getPhone1() + "\t" + getPhone2();
    }

	@Override
	public String toString() {
		return "Contact [firstName=" + firstName.get() + ", lastName=" + lastName.get()
				+ ", phone1=" + phone1.get() + ", phone2=" + phone2.get() + ", email1="
				+ email1.get() + ", email2=" + email2 + ", address=" + address.get()
				+ ", city=" + city.get() + ", type=" + type.get() + "]";
	}


}
