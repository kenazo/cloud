package CloudComputing.cloud;

import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.*;
import com.microsoft.windowsazure.exception.ServiceException;

import java.sql.Timestamp;
import java.util.Calendar;

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
	private int type;

	public SmartSpeedCamera(int uniqueID, String streetName, String town, int maxSpeed) {
		this.uniqueID = uniqueID;
		this.streetName = streetName;
		this.town = town;
		this.maxSpeed = maxSpeed;
		this.type = 45;

		Calendar calendar = Calendar.getInstance();
		Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());

		Configuration config = ServiceBusConfiguration.configureWithSASAuthentication("speed-information-cloud",
				"RootManageSharedAccessKey", "SNc61/6wG4vRLDzj231ER26wAZy+0Cl1Qve0sAWyWTs=", ".servicebus.windows.net");

		ServiceBusContract service = ServiceBusService.create(config);
		TopicInfo topicInfo = new TopicInfo("CloudTopic");

		// Create a "HighMessages" filtered subscription
		SubscriptionInfo subInfo = new SubscriptionInfo("SmartSpeedCamera");

		try {
			CreateSubscriptionResult result = service.createSubscription("CloudTopic", subInfo);
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		RuleInfo ruleInfo = new RuleInfo("CameraRule");
		ruleInfo = ruleInfo.withSqlExpressionFilter("MessageNumber > 30");

		try {
			CreateRuleResult ruleResult = service.createRule("CloudTopic", "SmartSpeedCamera", ruleInfo);
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Delete the default rule, otherwise the new rule won't be invoked.
		try {
			service.deleteRule("CloudTopic", "SmartSpeedCamera", "$Default");
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		

		BrokeredMessage message = new BrokeredMessage("camera");
		message.setProperty("type", "camera");
		message.setProperty("id", uniqueID);
		message.setProperty("'Street", streetName);
		message.setProperty("Town", town);
		message.setProperty("Limit", maxSpeed);

		try {
			service.sendTopicMessage("CloudTopic", message);
		} catch (ServiceException e) {
			System.out.print("ServiceException encountered: ");
			System.out.println(e.getMessage());
			System.exit(-1);
		}

	}

	{

		// try
		//
		// {
		// CreateTopicResult result = service.createTopic(topicInfo);
		// } catch (ServiceException e)
		//
		// {
		// System.out.print("ServiceException encountered: ");
		// System.out.println(e.getMessage());
		// System.exit(-1);
		// }

	}
}
