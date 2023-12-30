import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 2;
    public static int counter=0;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
       
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);
              if(counter>=4){
            serverSocket.close();
         }


            int clientCount = 0;
            while (counter< 5) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

                clientCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                while (true) {
                   
                    if(counter>4){
                         
                    }
                    String clientInput = reader.readLine();
                    if (clientInput == null || "exit".equalsIgnoreCase(clientInput.trim())) {
                    
                        break; // Exit the loop if the client closes the connection or types 'exit'
                    }

                    System.out.println("Received from client: " + clientInput);

                     counter+=1;


                    try {
                        int number = Integer.parseInt(clientInput);
                        StringBuilder result = new StringBuilder("The prime numbers are ");
                        for (int i = 2; i <= number; i++) {
                            if (isPrime(i)) {
                                result.append(i).append(", ");
                            }
                        }
                        result.setLength(result.length() - 2); // Remove the trailing comma and space
                        writer.println(result.toString());
                    } catch (NumberFormatException e) {
                        writer.println("Invalid input. Please send a valid number.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean isPrime(int n) {
            if (n <= 1) {
                return false;
            }
            for (int i = 2; i <= Math.sqrt(n); i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
