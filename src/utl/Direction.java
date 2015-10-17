package utl;

public enum Direction {
	TOP(0),
	BOTTOM(1),
	LEFT(2),
	RIGHT(3);
	
	public void setId(int id) {
		this.id = id;
	}

	private int id;
	
	private  Direction(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
