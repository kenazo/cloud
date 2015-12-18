package CloudComputing.cloud;

import java.net.URI;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.serviceruntime.RoleEnvironment;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;

public class NoSQLconsumer {
	private ServiceBusContract service;
	// Define the connection-string with your values.
	final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=kenscloud;"
			+ "AccountKey=IAsRj4mBBQysrEdMarJkBK/x28AoctsOrGWp2qXtkAdS8NWQwvCiT/RaKYZnXeXHajF+eG3Y/Yc+qXIxTWFqEQ==";

	public NoSQLconsumer() {

		Configuration config = ServiceBusConfiguration.configureWithSASAuthentication("speed-information-cloud",
				"RootManageSharedAccessKey", "SNc61/6wG4vRLDzj231ER26wAZy+0Cl1Qve0sAWyWTs=", ".servicebus.windows.net");

		service = ServiceBusService.create(config);

		try

		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

			while (true) {
				ReceiveSubscriptionMessageResult resultSubMsg = service.receiveSubscriptionMessage("CloudTopic",
						"Vehicles", opts);
				BrokeredMessage message = resultSubMsg.getValue();
				if (message != null && message.getMessageId() != null) {
					System.out.println("MessageID: " + message.getMessageId());
					// Display the topic message.
					System.out.print("From topic: ");
					byte[] b = new byte[200];
					String s = null;
					int numRead = message.getBody().read(b);
					while (-1 != numRead) {
						s = new String(b);
						s = s.trim();
						System.out.print(s);
						numRead = message.getBody().read(b);
					}
					System.out.println();
					System.out.println("Vehicle Type: " + message.getProperty("vehicleType"));
					System.out.println("Registration Number: " + message.getProperty("regNumber"));
					System.out.println("Travel Speed: " + message.getProperty("travelSpeed"));
					System.out.println("---------------------------------------------------");

					// Delete message.
					// System.out.println("Deleting this message.");
					// service.deleteMessage(message);
				} else {
					System.out.println("Finishing up - no more messages.");
					break;
					// Added to handle no more messages.
					// Could instead wait for more messages to be added.
				}
			}
		} catch (

		ServiceException e1)

		{
			System.out.print("ServiceException encountered: ");
			System.out.println(e1.getMessage());
			System.exit(-1);
		} catch (

		Exception e)

		{
			System.out.print("Generic exception encountered: ");
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		try

		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

			while (true) {
				ReceiveSubscriptionMessageResult resultSubMsg2 = service.receiveSubscriptionMessage("CloudTopic",
						"SmartSpeedCamera", opts);
				BrokeredMessage message = resultSubMsg2.getValue();
				if (message != null && message.getMessageId() != null) {
					System.out.println("MessageID: " + message.getMessageId());
					// Display the topic message.
					System.out.print("From topic: ");
					byte[] b = new byte[200];
					String s = null;
					int numRead = message.getBody().read(b);
					while (-1 != numRead) {
						s = new String(b);
						s = s.trim();
						System.out.print(s);
						numRead = message.getBody().read(b);
					}
					System.out.println();
					System.out.println("Unique ID: " + message.getProperty("id"));
					System.out.println("Street Name: " + message.getProperty("Street"));
					System.out.println("Town: " + message.getProperty("Town"));
					System.out.println("Speed Limit: " + message.getProperty("Limit"));
					System.out.println("---------------------------------------------------");

					// Delete message.
					// System.out.println("Deleting this message.");
					// service.deleteMessage(message);
				} else {
					System.out.println("Finishing up - no more messages.");
					break;
					// Added to handle no more messages.
					// Could instead wait for more messages to be added.
				}
			}
		} catch (

		ServiceException e1)

		{
			System.out.print("ServiceException encountered: ");
			System.out.println(e1.getMessage());
			System.exit(-1);
		} catch (

		Exception e)

		{
			System.out.print("Generic exception encountered: ");
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		try {
			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the table client.
			CloudTableClient cameraTableClient = storageAccount.createCloudTableClient();

			// Create the table if it doesn't exist.
			String tableName = "CameraTable";
			String storageAccountName = "kenscloud";

			StorageCredentials storageCredentials = StorageCredentials.tryParseCredentials(storageConnectionString);
			URI uriTable = new URI("http://" + storageAccountName + ".table.core.windows.net/" + tableName);
			StorageUri storageURI = new StorageUri(uriTable);
			CloudTable cameraTable = new CloudTable(storageURI, storageCredentials);
			cameraTable.createIfNotExists();

		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}

		class SpeedCameraEntity extends TableServiceEntity {
			public SpeedCameraEntity(int uniqueID, String streetName) {
				this.partitionKey = uniqueID + "";
				this.rowKey = streetName;
			}

			public SpeedCameraEntity() {
			}

			int uniqueID;
			String streetName;
			String town;
			int maxSpeed;

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

		class VehicleEntity extends TableServiceEntity {
			public VehicleEntity(String regNumber, int speedOfTravel) {
				this.partitionKey = regNumber;
				this.rowKey = speedOfTravel + "";
			}

			public VehicleEntity() {
			}

			String regNumber;
			int speedOfTravel;
			Random r;
			String vehicleType;

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
		}

		try {
			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the table client.
			CloudTableClient cameraTableClient = storageAccount.createCloudTableClient();

			// Loop through the collection of table names.
			for (String cameraTable : cameraTableClient.listTables()) {
				// Output each table name.
				System.out.println(cameraTable);
			}
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}

		try {
			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the table client.
			CloudTableClient vehicleTableClient = storageAccount.createCloudTableClient();

			// Loop through the collection of table names.
			for (String vehicleTable : vehicleTableClient.listTables()) {
				// Output each table name.
				System.out.println(vehicleTable);
			}
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}

	}
}
