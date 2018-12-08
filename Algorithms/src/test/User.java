package test;

public class User {

	@AliasAnnotation(alias="_id")
	private int id;
	@AliasAnnotation(alias="_name")
	private String name;
	private int age;
	
	public User(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.age = 1;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
