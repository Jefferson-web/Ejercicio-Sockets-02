package client;

import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jefferson
 */
public class User {

    private final int PORT = 9000;

    private final String HOST_ADDRESS = "127.0.0.1";

    private Socket clientSocket;

    private ObjectOutputStream outToServer;

    private ObjectInputStream inFromServer;

    Scanner sc;

    private final Logger logger = Logger.getLogger(User.class);

    public User() {

        try {

            clientSocket = new Socket(HOST_ADDRESS, PORT);

            inicializarStreams();

            sc = new Scanner(System.in);

            Pattern pattern = Pattern.compile("^LOGIN\\s{1}[a-zA-Z0-9]+$");

            Matcher m;

            String input;

            System.out.print("Ingrese el nombre de usuario : ");

            do {

                input = sc.nextLine();

                m = pattern.matcher(input);

                if ( m.matches() ) break;

                System.out.println("\nDebe ingresar de la siguiente forma : LOGIN [username]\n");

                System.out.print("Ingrese el nombre de usuario : ");

            } while (true);

            send(input);

            String response = String.valueOf(receive());

            System.out.println("Server : " + response);

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        } finally {

            try {

                if (outToServer != null) {
                    outToServer.close();
                }
                if (inFromServer != null) {
                    inFromServer.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public void inicializarStreams() throws IOException {
        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        inFromServer = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void send(Object o) throws IOException {
        outToServer.writeObject(o);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return inFromServer.readObject();
    }

    public static void main(String[] args) {

        new User();

    }

}
