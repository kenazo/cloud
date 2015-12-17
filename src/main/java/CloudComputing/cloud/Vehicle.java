package CloudComputing.cloud;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateRuleResult;
import com.microsoft.windowsazure.services.servicebus.models.CreateSubscriptionResult;
import com.microsoft.windowsazure.services.servicebus.models.RuleInfo;
import com.microsoft.windowsazure.services.servicebus.models.SubscriptionInfo;

public class Vehicle {

	// The vehicle registration plate number
	private String regNumber;
	// The type of the vehicle
	private int speedOfTravel;
	private String vehicleType;
	private int camId;
	private Random r;
	private int type = 30;
	private ServiceBusContract service;

	public Vehicle() {
		setRegNumber();
		getRegNumber();
		setVehicleType();
		getVehicleType();
		setSpeedOfTravel();
		getSpeedOfTravel();
		// String model[] = { "Truck", "Car", "Motorcycle" };
		// r = new Random();
		// speedOfTravel = r.nextInt(100);
		// regNumber = RandomStringUtils.randomAlphanumeric(8);
		// regNumber = regNumber.toUpperCase();
		// int index = r.nextInt(model.length);
		// String randomModel = model[index];
		// vehicleType = randomModel;

		Configuration config = ServiceBusConfiguration.configureWithSASAuthentication("speed-information-cloud",
				"RootManageSharedAccessKey", "SNc61/6wG4vRLDzj231ER26wAZy+0Cl1Qve0sAWyWTs=", ".servicebus.windows.net");

		service = ServiceBusService.create(config);
		//
		// SubscriptionInfo subInfo = new SubscriptionInfo("Vehicles");
		// try {
		// CreateSubscriptionResult result =
		// service.createSubscription("CloudTopic", subInfo);
		// RuleInfo ruleInfo = new RuleInfo("myRuleGT3");
		// ruleInfo = ruleInfo.withSqlExpressionFilter("type = "+type);
		// CreateRuleResult ruleResult =
		// service.createRule("CloudTopic", "Vehicles", ruleInfo);
		// // Delete the default rule, otherwise the new rule won't be invoked.
		// service.deleteRule("CloudTopic", "Vehicles", "$Default");
		// } catch (ServiceException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		BrokeredMessage message = new BrokeredMessage("Vehicles");
		message.setProperty("type", type);
		message.setProperty("regNumber", regNumber);
		message.setProperty("vehicleType", vehicleType);
		message.setProperty("travelSpeed", speedOfTravel);

		try {
			service.sendTopicMessage("CloudTopic", message);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Vehicle information: " + regNumber + " " + vehicleType + " " + speedOfTravel);

	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber() {
		regNumber = RandomStringUtils.randomAlphanumeric(8);
		regNumber = regNumber.toUpperCase();
	}

	public int getSpeedOfTravel() {
		return speedOfTravel;
	}

	public void setSpeedOfTravel() {
		r = new Random();
		speedOfTravel = r.nextInt(100);
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType() {
		String model[] = { "Truck", "Car", "Motorcycle" };
		r = new Random();
		int index = r.nextInt(model.length);
		String randomModel = model[index];
		vehicleType = randomModel;
	}

	// Configuration config =
	// ServiceBusConfiguration.configureWithSASAuthentication("speed-information-cloud",
	// "RootManageSharedAccessKey",
	// "SNc61/6wG4vRLDzj231ER26wAZy+0Cl1Qve0sAWyWTs=",
	// ".servicebus.windows.net");
	//
	// ServiceBusContract service = ServiceBusService.create(config);

	// Create a "LowMessages" filtered subscription

}
