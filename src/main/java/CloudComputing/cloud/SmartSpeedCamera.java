package CloudComputing.cloud;

import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.*;
import com.microsoft.windowsazure.exception.ServiceException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import javax.xml.datatype.*;

public class SmartSpeedCamera {
	// The Unique Identifier for the Smart Speed Camera
	private int uniqueID;
	// The street name of the camera
	private String streetName;
	// The town/city the camera is located in
	private String town;
	// The maximum speed limit for the area being monitored (in mph)
	private int maxSpeed;
	private int type = 50;
	private ServiceBusContract service;
	private Random r;

	Calendar calendar = Calendar.getInstance();
	Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());

	public SmartSpeedCamera() {
//		this.uniqueID = uniqueID;
//		this.streetName = streetName;
//		this.town = town;
//		this.maxSpeed = maxSpeed;

		
		
		
		Calendar calendar = Calendar.getInstance();
		Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());

		Configuration config = ServiceBusConfiguration.configureWithSASAuthentication("speed-information-cloud",
				"RootManageSharedAccessKey", "SNc61/6wG4vRLDzj231ER26wAZy+0Cl1Qve0sAWyWTs=", ".servicebus.windows.net");

		service = ServiceBusService.create(config);

//		SubscriptionInfo subInfo = new SubscriptionInfo("SmartSpeedCamera");
//
//		try {
//			CreateSubscriptionResult result = service.createSubscription("CloudTopic", subInfo);
//			RuleInfo ruleInfo = new RuleInfo("myRuleGT3");
//			ruleInfo = ruleInfo.withSqlExpressionFilter("type = " + type);
//			CreateRuleResult ruleResult = service.createRule("CloudTopic", "SmartSpeedCamera", ruleInfo);
//			// Delete the default rule, otherwise the new rule won't be invoked.
//			service.deleteRule("CloudTopic", "SmartSpeedCamera", "$Default");
//		} catch (ServiceException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

}
		
		
	public void sendMessage() {
		
	BrokeredMessage message = new BrokeredMessage("SmartSpeedCamera");
	message.setProperty("type", type);
	message.setProperty("id", getUniqueID());
	message.setProperty("Street", getStreetName());
	message.setProperty("Town", getTown());
	message.setProperty("Limit", getMaxSpeed());

	try {
		service.sendTopicMessage("cloudtopic", message);
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	System.out.println("Camera info: " + uniqueID + " " + streetName + " " + town + " " + maxSpeed);

	}

	public int getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
