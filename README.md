## Credit Score Prediction App

This repository contains the JavaFX application for a credit score prediction system. The application uses a trained K-Nearest Neighbors (KNN) model to predict a user's credit score based on their financial information.

**Features:**

* **User Authentication:** Secure login and signup functionality to manage user accounts.
* **Credit Score Prediction:** Predicts credit score based on user input using a trained KNN model.
* **User-Friendly Interface:** Intuitive JavaFX GUI for easy data input and result visualization.
* **Credit Score Distribution Visualization:** Displays a pie chart showcasing the distribution of credit score categories.
* **Help and Instructions:** Provides readily accessible guidance on how to use the application.

**Technologies:**

* **JavaFX:** The user interface is built using the JavaFX library, providing a rich and interactive experience.
* **Weka:**  The application leverages the Weka machine learning library for model training and prediction.
* **KNN:** The credit score prediction is based on the KNN algorithm, a popular and effective classification method.

**How It Works:**

1. **Training:** The KNN model is trained on a dataset containing historical credit data and corresponding credit score categories. 
2. **Prediction:** When a user provides their financial information, the app creates a new data instance and uses the trained KNN model to predict their credit score category. 
3. **Visualization:** The predicted credit score category is displayed to the user, and a pie chart provides a visual representation of the overall credit score distribution.

**Getting Started:**

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/CreditScoreApp.git
 2. **Install JavaFX:**

Ensure you have Java Development Kit (JDK) installed.
Download the JavaFX SDK from https://openjfx.io/.
Add the JavaFX libraries to your project's build path (refer to your IDE's instructions).

3. **Set up Data and Model:**

* **Dataset:** Place your preprocessed credit score dataset (CSV format) named final_dataset.csv in the root directory of the project.

* **Model:** Create a KNN model named knn_model.model (using Weka) and place it in the root directory. You can train the model using Weka.

* **User Data:** Create a file named "userdata.txt" in the root directory to store usernames and passwords.
4. **Run the Application:**

* Compile and run the **HomePage.java** file using your preferred IDE.
* The application will launch and display the login/signup screen.

**Structure:**

* **HomePage.java:** Contains the main application logic, user authentication, and navigation.

* **CreditScoringUI.java:** Handles the user interface, input data collection, prediction request, and result display.
* **CreditScoringModel.java:** Contains the code for loading, training, and using the KNN model for prediction.
  

**Disclaimer:** Include a disclaimer that the application is for educational purposes and should not be used for financial advice. 
