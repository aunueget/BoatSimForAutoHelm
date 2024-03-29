
public class AutoTunePid extends PidData{
	
	private double sigma;
	private double rho;
	private boolean reset;
	public static final int ITERATIONS= 600;
	public static final int ALLOWED_CALCS_PER_RUN= 600;
	public static final int POSSIBLE_KIS= 100;
	public int loopMax;
	public AutoTunePid(){
		super();
		sigma=0;
		rho=0;
		reset=false;
		loopMax=0;
		//TODO: initialize all K set data
	
	}
	//start one past the first position because rFiction needs one previous values
	public double calcFictiousR(int sVal,int position,float Kp,double Ki,float Kd){
		//TODO: what about across 360 to 0 is this going to create bad calculations
		//TODO: use degree difference
//		return ((Kd*this.y[position])
//					+(Kd*(r[position-1]-this.y[position-1]))
//					+(Ki*(y[position]-this.integral[position-1]))
//					+(Kp*this.y[position])
//					+this.u[position])
//				/(Kd*Ki*Kp);
		//TODO: find out why numbers are so big is derivative not right in PID
		return (this.y[position-1]
				+((sVal/((sVal*Kp)+Ki))*(this.u[position]+(Kd*(this.y[position-1])))));
	}
	public char calcTspec(int Kp,float Ki,int Kd){
		//TODO:make it so that the kp,ki,kd don't get changed until one of the new
		//chosen Kp,Ki,Kd pass on the current saved r,u,y pid data
		int start=1;
		int oppisiteStart=this.currCount-1;
		int allowedLoops=this.currCount-1;
		if(resetCount>0){
			start=handleLoop(this.currCount);
			oppisiteStart=this.currCount-1;
			allowedLoops=ALLOWED_CALCS_PER_RUN;
		}
		double ans=0;
		double convoW2U=0;
		double convoW1Error=0;
		int sumR=0;
		double rFiction=0;
		//start i one past the first because rFiction needs one previous values
		for(int i=0;start+i<allowedLoops&&start+i!=this.currCount;i++){//i<ITERATIONS-1;i++){
			rFiction=calcFictiousR(i,start+i,Kp,Ki,Kd);
			convoW1Error += productW1Error(i,handleLoop(oppisiteStart-i),rFiction);
			convoW2U += prouductW2U(i,handleLoop(oppisiteStart-i));
			sumR+=(rFiction*rFiction);
			ans=convoW1Error+convoW2U-(sigma*sigma)-sumR;
			if(ans<=0){
				//reset is to make sure transition is smooth when switching Kp,Ki,Kd
				resetKiKd();
				return 0;
			}
			if(i>=this.loopMax){
				this.loopMax=i;
				//System.out.println("The loop Count Max is up to: "+i+"current ans: "+ans+"Kp,Ki,Kd: "+Kp+","+Ki+","+Kd);				
			}
		}
		return 1;
	}
	//formula for w1
	double calcWOne(int s){
		return (s+10)/(10*(s+0.1));//(s+10)/(2*(s+0.1));
	}
	//formula for w2
	double calcWTwo(int s){
		return 0.1/(1.2*((s+1)*(s+1)*(s+1)));//0.1/(1.2*((s+1)*(s+1)*(s+1)));
	}
	//convo of the ficticious error (rFiction-y) with w1
	double productW1Error(int sVal,int timeXminus,double rFiction){
		double w1=calcWOne(sVal);
		//TODO: rFiction needs to be checked for in bounds if its been calculated \right
		double error=degreeDifferance(false,rFiction,this.y[timeXminus]);
		double absConvo=Math.abs(w1*error);
		return (absConvo*absConvo);//squared
	}
	//convo of the pid output with w2
	double prouductW2U(int sVal, int timeXminus){
		double w2=calcWTwo(sVal);
		double absConvo=Math.abs(w2*this.u[timeXminus]);
		return (absConvo*absConvo);//squared
	}
	int handleLoop(int position){
		if(position>ITERATIONS){
			position-=ITERATIONS;
		}
		else if(position<0){
			position+=ITERATIONS;
		}
		return position;
	}
	void resetKiKd(){
		reset=true;
		//PID.previous_error=0;
		//PID.integral=0;
	}
	void setResetKd(){
		reset=false;
	}
	public boolean isReset() {
		return reset;
	}
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	/* if the absoluteValue is set to 0 then degreeDifferance will have a sign pos or  neg */
	public static double degreeDifferance(boolean absoluteValue,double current,double desired){
		double compDiff=0;
		int accrossN=0;
		compDiff=Math.abs(current-desired);
		if(compDiff>180){
			compDiff=Math.abs(compDiff-360);
			accrossN=1;
		}
		if(absoluteValue || ((current>desired && accrossN==0) || (accrossN==1 && current<desired))){
			return compDiff;
		}
		else{
			return (-1*compDiff);
		}
	}
	double handle360(double degrees){
		if(degrees>=360){
			return degrees-360;
		}
		else if(degrees<0){
			return degrees+360;
		}
		return degrees;
	}

}
