
public class PidData {

	public static final int POSSIBLE_KIS= 100;
	public static final int ITERATIONS=600;
	//saved pid data r,y at each time
	//rFiction is the calculated r (from y,u,integral)
	//that would give u with the proper Ki, Kd, Kp(i think need to double check this)
	//saved pid integral at each time
	public int[] u,r,y,integral;
	//the current position of the most recent data
	public int currCount;
	//this is the number of times we have stored a full array of
	//pid data and started overwriting from 0 on reading at a time
	public int resetCount;
	//this is the position in the array of tested Ki, Kd, Kp possibilities
	public int currKSet;
	//array of possible Ki, Kd, Kp options
	public int[] Ki,Kp,Kd;
	//the count of pid data that fit this set of Ki, Kd, Kp before it failed
	public int kSetIterations[];


	public PidData(){
		u=new int[ITERATIONS];
		r=new int[ITERATIONS];
		y=new int[ITERATIONS];
		integral=new int[ITERATIONS];
		currCount=0;
		resetCount=0;
		currKSet=0;
		Ki=new int[POSSIBLE_KIS];
		Kp=new int[POSSIBLE_KIS];
		Kd=new int[POSSIBLE_KIS];
		kSetIterations=new int[POSSIBLE_KIS];
		
	}

}
