package com.calpoly.incredible;

// Include the following imports to use table APIs
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

/**
 * Created by Zack Cody on 3/8/2017.
 */
public class Backend {
    // Define the connection-string with your values.
    private static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=incrediblestorage;" +
                    "AccountKey=DtQhzOkOqby2ct+FYpwpTwI7ETkPqqyrKn1KtCjK6DMURS9G5z0Xfr0juAKr43d+2+GZElXZ4FUQnplU09rUPg==";

    private static CloudStorageAccount storageAccount = null;
    private static CloudTableClient client = null;

    public static void connectToBackend() {
        try
        {
            // Retrieve storage account from connection-string.
            storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);
            client = storageAccount.createCloudTableClient();
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
        System.out.println("STORAGE ACCOUNT: " + storageAccount.toString());
    }

    public static void createNewTable(String tableName) {
        try {
            CloudTable newTable = client.getTableReference(tableName);
            newTable.createIfNotExists();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void insertNewSource(String name, double score, int numArticles) {
        String sourceTable = "Source";

        try {
            CloudTable newTable = client.getTableReference(sourceTable);

            SourceEntity newSource = new SourceEntity(name);
            newSource.setScore(score);
            newSource.setNumArticles(numArticles);

            TableOperation insertSource = TableOperation.insertOrReplace(newSource);

            newTable.execute(insertSource);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getSource(String name) {
        String sourceTableName = "Source";
        String rowKey = "incredible";

        try {
            CloudTable sourceTable = client.getTableReference(sourceTableName);

            TableOperation retrieveSource = TableOperation.retrieve(name, rowKey, SourceEntity.class);

            SourceEntity specificSource = sourceTable.execute(retrieveSource).getResultAsType();

            if (specificSource != null) {
                System.out.println("NAME: " + specificSource.getPartitionKey() + "\nSCORE: " + specificSource.getScore() + "\nNUM_ARTICLES: " + specificSource.getNumArticles());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void insertNewWeight(String name, double value) {
        String weightTable = "Weight";

        try {
            CloudTable newTable = client.getTableReference(weightTable);

            WeightEntity newWeight = new WeightEntity(name);
            newWeight.setValue(value);

            TableOperation insertWeight = TableOperation.insertOrReplace(newWeight);

            newTable.execute(insertWeight);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getWeight(String name) {
        String weightTableName = "Weight";
        String rowKey = "incredible";

        try {
            CloudTable weightTable = client.getTableReference(weightTableName);

            TableOperation retrieveWeight = TableOperation.retrieve(name, rowKey, WeightEntity.class);

            WeightEntity specificWeight = weightTable.execute(retrieveWeight).getResultAsType();

            if (specificWeight != null) {
                System.out.println("NAME: " + specificWeight.getPartitionKey() + "\nVALUE: " + specificWeight.getValue());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
