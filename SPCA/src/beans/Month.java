package beans;

import java.util.HashMap;

public class Month {

	private String name;
	private HashMap<String,Integer> categories;
	
	public Month(String name){
		this.name = name;
		categories = new HashMap<String, Integer>();
	}
	
	public String getName(){
		return name;
	}
	public void addAmountToCategory(String category,Integer amount){
		Integer sum = categories.get(category);
		if(sum == null){
			sum = amount;
		}
		else
			sum += amount;
		
		categories.put(category, sum);
	}
	public Integer getSumPerCategory(String category){
		return categories.get(category);
	}
}
