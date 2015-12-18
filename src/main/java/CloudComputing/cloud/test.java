package CloudComputing.cloud;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		
		SmartSpeedCamera test1 = new SmartSpeedCamera();

		File file = new File("SmartSpeedCameras.txt");

	    try {

	        Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	            String i = sc.nextLine();
	            String [] x = i.split(", ");
	            test1.setUniqueID(Integer.parseInt(x[0]));
	            test1.setStreetName(x[1]);
	            test1.setTown(x[2]);
	            test1.setMaxSpeed(Integer.parseInt(x[3]));;
	            test1.sendMessage();
	            
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    
			
		Vehicle test11 = new Vehicle();
		Vehicle test2 = new Vehicle();
		Vehicle test3 = new Vehicle();
		Vehicle test4 = new Vehicle();

	NoSQLconsumer test = new NoSQLconsumer();

	}

}
