import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Allow the user to choose the base and target currencies
        System.out.print("Enter the base currency code (e.g., USD): ");
        String baseCurrency = scanner.next().toUpperCase();
        
        System.out.print("Enter the target currency code (e.g., EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        // Step 2: Fetch real-time exchange rates from a reliable API
        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

        // Step 3: Take input from the user for the amount they want to convert
        System.out.print("Enter the amount to convert: ");
        double amountToConvert = scanner.nextDouble();

        // Step 4: Convert the input amount using the fetched exchange rate
        double convertedAmount = amountToConvert * exchangeRate;

        // Step 5: Display the result to the user
        System.out.printf("%.2f %s is equal to %.2f %s%n",
                amountToConvert, baseCurrency, convertedAmount, targetCurrency);

        scanner.close();
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            // Replace the following URL with the actual API endpoint
            String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();

                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }

                scanner.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject rates = jsonResponse.getJSONObject("rates");

                return rates.getDouble(targetCurrency);
            } else {
                System.out.println("Error fetching exchange rates. HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}