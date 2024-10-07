package com.creditscoreapp.creditscore;

import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.core.SerializationHelper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class CreditScoringModel {

    private static IBk model;
    private static Instances data;
    private static ExecutorService executor = Executors.newCachedThreadPool();  // Thread pool for predictions

    // Static block to load the model and dataset at startup
    static {
        try {
            String datasetPath = "C:\\Users\\bhara\\OneDrive\\Desktop\\java_package\\final_dataset.csv"; // Path to your dataset
            String modelPath = "C:\\Users\\bhara\\OneDrive\\Desktop\\java_package\\knn_model.model"; // Path to your saved model

            // Load and preprocess the dataset
            data = loadDataset(datasetPath);
            data = preprocessDataset(data);

            // Load the trained K-NN model
            model = loadModel(modelPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load the dataset
    public static Instances loadDataset(String filePath) throws Exception {
        System.out.println("Loading dataset...");
        DataSource source = new DataSource(filePath);
        Instances data = source.getDataSet();

        if (data.numInstances() == 0) {
            throw new Exception("Dataset is empty or improperly formatted.");
        }

        // Set the class index to the last attribute (credit score range)
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }

        System.out.println("Dataset loaded successfully. Number of instances: " + data.numInstances());
        return data;
    }

    // Preprocess the dataset by removing unnecessary attributes (e.g., 'CreditScore')
    public static Instances preprocessDataset(Instances data) throws Exception {
        System.out.println("Preprocessing dataset...");
        int[] indices = {4}; // Remove 'CreditScore' attribute

        // Use Weka's Remove filter to remove attributes
        Remove remove = new Remove();
        remove.setAttributeIndicesArray(indices);
        remove.setInputFormat(data);
        Instances newData = Filter.useFilter(data, remove);

        System.out.println("Dataset preprocessed successfully.");
        return newData;
    }

    // Load the saved model from file
    public static IBk loadModel(String filePath) throws Exception {
        System.out.println("Loading model...");
        IBk model = (IBk) SerializationHelper.read(filePath);
        System.out.println("Model loaded successfully.");
        return model;
    }

    // Method to predict credit score asynchronously (in a separate thread)
    public static Future<Double> predictCreditScoreAsync(String age, String income, String loanAmount, String monthsEmployed,
                                                         String numCreditLines, String interestRate, String loanTerm,
                                                         double dtiRatio, String defaulted, String loanId) {

        // Define the task to be performed in a separate thread
        Callable<Double> predictionTask = () -> {
            try {
                // Create a new instance based on user input
                Instance instance = new DenseInstance(data.numAttributes() - 1);  // -1 for loanId
                instance.setDataset(data);

                // Set values for each attribute
                instance.setValue(0, Double.parseDouble(age));             // Age
                instance.setValue(1, Double.parseDouble(income));          // Income
                instance.setValue(2, Double.parseDouble(loanAmount));      // Loan Amount
                instance.setValue(4, Double.parseDouble(monthsEmployed));  // Months Employed
                instance.setValue(5, Double.parseDouble(numCreditLines));  // Number of Credit Lines
                instance.setValue(6, Double.parseDouble(interestRate));    // Interest Rate
                instance.setValue(7, Double.parseDouble(loanTerm));        // Loan Term (in months)
                instance.setValue(8, dtiRatio);                           // DTI Ratio
                instance.setValue(9, Double.parseDouble(defaulted));       // Defaulted (1 for Yes, 0 for No)

                // Predict the credit range in this thread
                double predictedCreditRange = model.classifyInstance(instance);
                return predictedCreditRange;

            } catch (Exception e) {
                e.printStackTrace();
                return -1.0; // Return a negative value to indicate an error
            }
        };

        // Submit the task to the executor for asynchronous execution
        return executor.submit(predictionTask);
    }

    // Method to predict credit score based on user input from the UI
    public static double predictCreditScoreFromUI(String age, String income, String loanAmount, String monthsEmployed,
                                                  String numCreditLines, String interestRate, String loanTerm,
                                                  double dtiRatio, String defaulted, String loanId) throws Exception {

        // Create a new instance based on user input
        Instance instance = new DenseInstance(data.numAttributes() - 1);  // -1 for loanId
        instance.setDataset(data);

        // Set values for each attribute
        instance.setValue(0, Double.parseDouble(age));             // Age
        instance.setValue(1, Double.parseDouble(income));          // Income
        instance.setValue(2, Double.parseDouble(loanAmount));      // Loan Amount
        instance.setValue(4, Double.parseDouble(monthsEmployed));  // Months Employed
        instance.setValue(5, Double.parseDouble(numCreditLines));  // Number of Credit Lines
        instance.setValue(6, Double.parseDouble(interestRate));    // Interest Rate
        instance.setValue(7, Double.parseDouble(loanTerm));        // Loan Term (in months)
        instance.setValue(8, dtiRatio);                           // DTI Ratio
        instance.setValue(9, Double.parseDouble(defaulted));       // Defaulted (1 for Yes, 0 for No)

        // Predict the credit range
        double predictedCreditRange = model.classifyInstance(instance);
        return predictedCreditRange; // Return the predicted score range
    }

    // Save the model (for future training or updates)
    public static void saveModel(IBk model, String filePath) throws Exception {
        System.out.println("Saving model...");
        SerializationHelper.write(filePath, model);
        System.out.println("Model saved successfully.");
    }

    // Train the K-NN model
    public static IBk trainModel(Instances data) throws Exception {
        System.out.println("Training K-NN model...");
        IBk knn = new IBk();
        knn.setKNN(5); // Set number of nearest neighbors

        knn.buildClassifier(data); // Train the model
        System.out.println("K-NN model trained successfully.");
        return knn;
    }

    public static void main(String[] args) {
        // Example usage of asynchronous prediction
        Future<Double> predictionFuture = predictCreditScoreAsync("30", "50000", "20000", "24", "3", "5.5", "36", 0.35, "0", "12345");

        try {
            // Wait for the result and print it once available
            double predictedCreditScore = predictionFuture.get();
            System.out.println("Predicted Credit Score Range: " + predictedCreditScore);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Shutdown the executor service to release resources
        executor.shutdown();
    }
}
