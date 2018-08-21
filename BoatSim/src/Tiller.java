
public class Tiller {
	private int currentPosition;
	private int lastPosition;
	private int lastDirection;
	private int currentDirection;
	final int MAX=45;
	final int MIN=-45;
	private long totalMovement;
	private long timeNotMoving;
	public Tiller(){
		this.currentPosition=0;
		this.lastPosition=0;
		this.lastDirection=1;
		this.totalMovement=0;
	}
	public Tiller(int position){
		this.currentPosition=position;
		this.lastPosition=position;
		this.lastDirection=1;
	}
	public int getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(int newPosition) {
		this.lastPosition=this.currentPosition;
		this.currentPosition = newPosition;
		if(this.currentPosition>MAX){
			this.currentPosition=this.MAX;
		}
		if(this.currentPosition<MIN){
			this.currentPosition=this.MIN;
		}
		this.lastDirection=this.currentDirection;
		this.currentDirection=this.currentPosition-this.lastPosition;
		this.totalMovement+=Math.abs(this.currentDirection);
	}
	public void changePosition(int change) {
		this.lastPosition=this.currentPosition;
		this.currentPosition += change;
		if(this.currentPosition>MAX){
			this.currentPosition=this.MAX;
		}
		if(this.currentPosition<MIN){
			this.currentPosition=this.MIN;
		}
		this.lastDirection=this.currentDirection;
		this.currentDirection=this.currentPosition-this.lastPosition;
		this.totalMovement+=Math.abs(this.currentDirection);
		if(this.currentDirection==0){
			this.timeNotMoving++;
		}

	}

	public int getLastPosition() {
		return lastPosition;
	}
	public int getLastDirection() {
		return lastDirection;
	}
	public int getCurrentDirection() {
		return currentDirection;
	}
	public long getTotalMovement(){
		return this.totalMovement;
	}
	public long getTimeNotMoving(){
		return this.timeNotMoving;
	}

}
