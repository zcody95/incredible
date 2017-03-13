package com.calpoly.incredible;

// Include the following imports to use table APIs
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

import javax.xml.transform.Source;
import java.util.ArrayList;

/**
 * Created by Zack Cody on 3/8/2017.
 */
public class Backend {
    // Define the connection-string with your values.
    private static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=incrediblestorage;" +
                    "AccountKey=DtQhzOkOqby2ct+FYpwpTwI7ETkPqqyrKn1KtCjK6DMURS9G5z0Xfr0juAKr43d+2+GZElXZ4FUQnplU09rUPg==";
    public static final String ROW_KEY = "incredible";

    private static CloudStorageAccount storageAccount = null;
    private static CloudTableClient client = null;

    public static void connectToBackend() throws Exception{
        try
        {
            // Retrieve storage account from connection-string.
            storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);
            client = storageAccount.createCloudTableClient();
            System.out.println("STORAGE ACCOUNT: " + storageAccount.toString());
        }
        catch (Exception e)
        {
            // Output the stack trace.
            System.out.println("Could not connect");
            throw e;
        }
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

    public static void insertNewSource(String tableName, String name, double score, int numArticles) throws Exception {
        try {
            CloudTable newTable = client.getTableReference(tableName);

            SourceEntity newSource = new SourceEntity(name);
            newSource.setScore(score);
            newSource.setNumArticles(numArticles);

            TableOperation insertSource = TableOperation.insertOrReplace(newSource);

            newTable.execute(insertSource);
        }
        catch (Exception ex) {
            System.out.println("Could not update source");
            throw ex;
        }
    }

    public static void getSource(String tableName, Article article)  throws Exception{
        try {
            CloudTable sourceTable = client.getTableReference(tableName);

            TableOperation retrieveSource = TableOperation.retrieve(article.getSource(), Backend.ROW_KEY, SourceEntity.class);

            SourceEntity specificSource = sourceTable.execute(retrieveSource).getResultAsType();

            if (specificSource != null) {
                article.setSourceScore(((float) specificSource.getScore()));
                article.setTotal(specificSource.getNumArticles() + 1);
            }
            else {
                article.setTotal(1);
            }
        }
        catch (Exception ex) {
            System.out.println("Couldn't get source.");
            throw ex;
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

        try {
            CloudTable weightTable = client.getTableReference(weightTableName);

            TableOperation retrieveWeight = TableOperation.retrieve(name, Backend.ROW_KEY, WeightEntity.class);

            WeightEntity specificWeight = weightTable.execute(retrieveWeight).getResultAsType();

            if (specificWeight != null) {
                System.out.println("NAME: " + specificWeight.getPartitionKey() + "\nVALUE: " + specificWeight.getValue());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void clearSourceTypeTable(String tableName) {
        try {
            CloudTable tableToClear = client.getTableReference(tableName);
            String filter = TableQuery.generateFilterCondition(Backend.ROW_KEY, TableQuery.QueryComparisons.EQUAL, Backend.ROW_KEY);

            TableQuery<SourceEntity> sourceQuery = TableQuery.from(SourceEntity.class);

            for (SourceEntity source : tableToClear.execute(sourceQuery)) {
                TableOperation deleteSource = TableOperation.delete(source);
                tableToClear.execute(deleteSource);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void clearWeightTypeTable(String tableName) {
        try {
            CloudTable tableToClear = client.getTableReference(tableName);
            String filter = TableQuery.generateFilterCondition(Backend.ROW_KEY, TableQuery.QueryComparisons.EQUAL, Backend.ROW_KEY);

            TableQuery<WeightEntity> weightQuery = TableQuery.from(WeightEntity.class);

            for (WeightEntity weight : tableToClear.execute(weightQuery)) {
                TableOperation deleteWeight = TableOperation.delete(weight);
                tableToClear.execute(deleteWeight);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteTable(String tableName) {
        try {
            CloudTable tableToDelete = client.getTableReference(tableName);
            tableToDelete.deleteIfExists();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mergeSourceTables(String firstTable, String secondTable) {
        try {
            CloudTable firstTableRef = client.getTableReference(firstTable);
            CloudTable secondTableRef = client.getTableReference(secondTable);

            String filter = TableQuery.generateFilterCondition(Backend.ROW_KEY, TableQuery.QueryComparisons.EQUAL, Backend.ROW_KEY);
            TableQuery<SourceEntity> sourceQuery = TableQuery.from(SourceEntity.class);

            for (SourceEntity source : firstTableRef.execute(sourceQuery)) {
                TableOperation insertSource = TableOperation.insert(source);
                secondTableRef.execute(insertSource);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void listSources(String tableName) {
        try {
            CloudTable sourceTable = client.getTableReference(tableName);
            String filter = TableQuery.generateFilterCondition(Backend.ROW_KEY, TableQuery.QueryComparisons.EQUAL, Backend.ROW_KEY);
            TableQuery<SourceEntity> sourceQuery = TableQuery.from(SourceEntity.class);

            System.out.println("LIST OF SOURCES IN " + tableName + ":\n");
            for (SourceEntity source : sourceTable.execute(sourceQuery)) {
                System.out.println(source.toString());
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
