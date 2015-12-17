package CloudComputing.cloud;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;
import com.microsoft.windowsazure.serviceruntime.RoleEnvironment;

public class NoSQLconsumer {

	// Define the connection-string with your values.
	public static String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=kenscloud;" + 
 "AccountKey=IAsRj4mBBQysrEdMarJkBK/x28AoctsOrGWp2qXtkAdS8NWQwvCiT/RaKYZnXeXHajF+eG3Y/Yc+qXIxTWFqEQ==";

	// Retrieve storage account from connection-string.
	// String storageConnectionString = RoleEnvironment.getConfigurationSettings().get("StorageConnectionString");

	{

		try

		{
			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the table client.
			CloudTableClient tableClient = storageAccount.createCloudTableClient();

			// Create the table if it doesn't exist.
			String tableName = "VehicleStorage";
			CloudTable TableStorage = new CloudTable(tableName, tableClient);
			TableStorage.createIfNotExists();
		} catch (Exception e)

		{
			// Output the stack trace.
			e.printStackTrace();
		}
	}
}