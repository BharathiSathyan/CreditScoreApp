package com.creditscoreapp.creditscore;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import org.kordamp.bootstrapfx.BootstrapFX;
import weka.core.Instance;
import javafx.scene.control.Alert.AlertType;

public class CreditScoringUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Credit Scoring Application");



        // Create GridPane for the layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30, 30, 10, 10));

        grid.setVgap(10);
        grid.setHgap(10);


        // Set the scene
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);
        grid.setPadding(new Insets(30, 30, 10, 10));
        grid.setAlignment(Pos.CENTER);





        Scene scene = new Scene(borderPane, 800, 600);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet()); // Apply BootstrapFX styling
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true); // Set the stage to full screen

        primaryStage.show();

        // Add a big bold title
        Label title = new Label(" CREDIT SCORE PREDICTION APP ");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #007bff;");
        grid.add(title, 0, 0, 2, 1);

        // Create input fields
        TextField ageField = new TextField();
        ageField.setPromptText("Enter Age");

        TextField incomeField = new TextField();
        incomeField.setPromptText("Enter Income");

        TextField loanAmountField = new TextField();
        loanAmountField.setPromptText("Enter Loan Amount");

        TextField monthsEmployedField = new TextField();
        monthsEmployedField.setPromptText("Months Employed");

        TextField numCreditLinesField = new TextField();
        numCreditLinesField.setPromptText("Number of Credit Lines");

        TextField interestRateField = new TextField();
        interestRateField.setPromptText("Interest Rate");

        TextField loanTermField = new TextField();
        loanTermField.setPromptText("Loan Term (Months)");

        TextField dtiRatioField = new TextField();
        dtiRatioField.setPromptText("Total Monthly Debt Amount");

        ComboBox<String> defaultedField = new ComboBox<>();
        defaultedField.getItems().addAll("0", "1");
        defaultedField.setPromptText("Defaulted Loan? (1 = Yes, 0 = No)");

        TextField loanIdField = new TextField();
        loanIdField.setPromptText("Enter Loan ID");

        // Label for displaying error messages
        Label errorLabel = new Label();

        // Button for submitting the form
        Button submitButton = new Button("Predict Credit Score");

        // Label for displaying the result
        Label resultLabel = new Label();

        // Button for Help/Instructions
        Button helpButton = new Button("Help");

        // Button for Visualizing Data
        Button visualizeButton = new Button("Show Credit Score Distribution");

        // Add everything to the grid
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);

        grid.add(new Label("Income:"), 0, 2);
        grid.add(incomeField, 1, 2);

        grid.add(new Label("Loan Amount:"), 0, 3);
        grid.add(loanAmountField, 1, 3);

        grid.add(new Label("Months Employed:"), 0, 4);
        grid.add(monthsEmployedField, 1, 4);

        grid.add(new Label("Number of Credit Lines:"), 0, 5);
        grid.add(numCreditLinesField, 1, 5);

        grid.add(new Label("Interest Rate:"), 0, 6);
        grid.add(interestRateField, 1, 6);

        grid.add(new Label("Loan Term:"), 0, 7);
        grid.add(loanTermField, 1, 7);

        grid.add(new Label("Total Monthly Debt Amount:"), 0, 8);
        grid.add(dtiRatioField, 1, 8);

        grid.add(new Label("Defaulted?:"), 0, 9);
        grid.add(defaultedField, 1, 9);

        grid.add(new Label("Loan ID:"), 0, 10);
        grid.add(loanIdField, 1, 10);

        grid.add(submitButton, 1, 11);
        grid.add(resultLabel, 1, 12);
        grid.add(errorLabel, 1, 13);
        grid.add(helpButton, 1, 14);
        grid.add(visualizeButton, 1, 15);

        // Create a Change User button
        Button changeUserButton = new Button("Change User");
        grid.add(changeUserButton,1,17);
        changeUserButton.setOnAction(e -> {
            // Redirect to the home page or login page
            // Here, you can create an instance of your login page and show it
            showLoginPage(primaryStage);
        });


        // Set action for submit button
        // Set action for submit button
        submitButton.setOnAction(e -> {
            // Clear previous error messages
            errorLabel.setText("");
            resultLabel.setText("");

            // Gather the input data and validate
            try {
                // Validate input data
                if (validateInput(ageField, incomeField, loanAmountField, monthsEmployedField, numCreditLinesField,
                        interestRateField, loanTermField, dtiRatioField, defaultedField, errorLabel)) {

                    // Gather the input data
                    String age = ageField.getText();
                    String incomeStr = incomeField.getText();
                    String loanAmount = loanAmountField.getText();
                    String monthsEmployed = monthsEmployedField.getText();
                    String numCreditLines = numCreditLinesField.getText();
                    String interestRate = interestRateField.getText();
                    String loanTerm = loanTermField.getText();
                    String debtStr = dtiRatioField.getText();
                    String defaulted = defaultedField.getValue();
                    String loanId = loanIdField.getText();  // Not used for prediction

                    // Convert income and debt to double for calculation
                    double income = Double.parseDouble(incomeStr);
                    double debt = Double.parseDouble(debtStr);

                    // Calculate DTI Ratio
                    double dtiRatio = (debt / (income / 12)) * 100; // DTI Ratio as a percentage

                    // Call the backend model to get the prediction
                    double predictedRange = CreditScoringModel.predictCreditScoreFromUI(age, incomeStr, loanAmount, monthsEmployed,
                            numCreditLines, interestRate, loanTerm, dtiRatio, defaulted, loanId);

                    // Map the predicted value to a meaningful range description
                    String creditScoreCategory = mapCreditRange(predictedRange);

                    // Display the result in the resultLabel
                    resultLabel.setText("Predicted Credit Score Category: " + creditScoreCategory);
                    resultLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: green;");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error predicting credit score.");
                resultLabel.setStyle("-fx-text-fill: red;");
            }
        });




        // Set action for Help button to display instructions
        helpButton.setOnAction(e -> {
            Alert helpAlert = new Alert(AlertType.INFORMATION);
            helpAlert.setTitle("Help - Credit Scoring Application");
            helpAlert.setHeaderText("Instructions for filling the form");
            helpAlert.setContentText("Please provide the following details:\n" +
                    "- Age: Your age\n" +
                    "- Income: Monthly income in dollars\n" +
                    "- Loan Amount: Amount of loan applied for\n" +
                    "- Months Employed: Number of months employed\n" +
                    "- Number of Credit Lines: Total number of credit lines\n" +
                    "- Interest Rate: Loan interest rate (as percentage)\n" +
                    "- Loan Term: Duration of loan (in months)\n" +
                    "- DTI Ratio: Debt-to-Income ratio\n" +
                    "- Defaulted: Has the loan defaulted before (0 = No, 1 = Yes)");
            helpAlert.showAndWait();
        });

        // Set action for Data Visualization button
        visualizeButton.setOnAction(e -> showCreditScoreDistribution());

        // Set the scene
       // Scene scene = new Scene(grid, 800, 600);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet()); // Apply BootstrapFX styling
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to show the home page
    private void showLoginPage(Stage primaryStage) {
        HomePage homePage = new HomePage(); // Create an instance of HomePage
        try {
            homePage.start(primaryStage);
            primaryStage.setFullScreen(true);// Call the start method to set the scene
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to validate user input
    private boolean validateInput(TextField ageField, TextField incomeField, TextField loanAmountField,
                                  TextField monthsEmployedField, TextField numCreditLinesField, TextField interestRateField,
                                  TextField loanTermField, TextField dtiRatioField, ComboBox<String> defaultedField,
                                  Label errorLabel) {

        try {
            // Check if any fields are empty
            if (ageField.getText().isEmpty() || incomeField.getText().isEmpty() || loanAmountField.getText().isEmpty() ||
                    monthsEmployedField.getText().isEmpty() || numCreditLinesField.getText().isEmpty() ||
                    interestRateField.getText().isEmpty() || loanTermField.getText().isEmpty() ||
                    dtiRatioField.getText().isEmpty() || defaultedField.getValue() == null) {

                errorLabel.setText("Error: All fields must be filled.");
                errorLabel.setStyle("-fx-text-fill: red;");

                return false;
            }

            // Validate if numeric fields contain valid numbers
            Double.parseDouble(ageField.getText());
            Double.parseDouble(incomeField.getText());
            Double.parseDouble(loanAmountField.getText());
            Double.parseDouble(monthsEmployedField.getText());
            Double.parseDouble(numCreditLinesField.getText());
            Double.parseDouble(interestRateField.getText());
            Double.parseDouble(loanTermField.getText());
            Double.parseDouble(dtiRatioField.getText());

            return true;

        } catch (NumberFormatException e) {
            errorLabel.setText("Error: Please enter valid numeric values.");
            errorLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
    }

    // Method to map the predicted range to a credit score description
    private String mapCreditRange(double predictedRange) {

        if (Math.abs(predictedRange - 1)<=0.5) {
            return "Poor \n Credit Range : 300 to 579";
        } else if (Math.abs(predictedRange - 2)<=0.5) {
            return "Fair \n Credit Range : 580 to 669";
        } else if (Math.abs(predictedRange - 3)<=0.5) {
            return "Good \n Credit Range : 670 to 739";
        } else if (Math.abs(predictedRange - 4)<=0.5) {
            return "Very Good \n Credit Range : 740 to 799";
        } else if (Math.abs(predictedRange - 5)<=0.5) {
            return " Excellent \n Credit Range : 800 to 850";
        } else {
            return "Unknown";
        }
    }

    // Method to display the credit score distribution using JFreeChart
    private void showCreditScoreDistribution() {
        // Create a sample pie chart for credit score distribution
        PieChart pieChart = new PieChart();

        PieChart.Data slice1 = new PieChart.Data("Poor (300-579)", 20);
        PieChart.Data slice2 = new PieChart.Data("Fair (580-669)", 30);
        PieChart.Data slice3 = new PieChart.Data("Good (670-739)", 25);
        PieChart.Data slice4 = new PieChart.Data("Very Good (740-799)", 15);
        PieChart.Data slice5 = new PieChart.Data("Excellent (800-850)", 10);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);
        pieChart.getData().add(slice4);
        pieChart.getData().add(slice5);

        // Create a new stage for the pie chart
        Stage chartStage = new Stage();
        VBox vbox = new VBox(pieChart);
        Scene chartScene = new Scene(vbox, 500, 400);
        chartStage.setScene(chartScene);
        chartStage.setTitle("Credit Score Distribution");
        chartStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}