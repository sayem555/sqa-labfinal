import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            while (true) {
                System.out.print("Enter a number (type 'exit' to quit): ");
                String userInput = consoleReader.readLine();

                if ("exit".equalsIgnoreCase(userInput)) {
                    break; // Exit the loop if the user types 'exit'
                }

                // Send the user input to the server
                writer.println(userInput);

                // Receive and print the result from the server
                String serverResponse = reader.readLine();
                System.out.println("Server response: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
