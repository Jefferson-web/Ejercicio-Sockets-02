package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Jefferson
 */
public class AuthenticationServer implements Runnable {

    private final int PORT = 9000;

    private ServerSocket serverSocket;

    private Socket clientSocket;

    private ObjectOutputStream outToClient;

    private ObjectInputStream inFromClient;

    Vector<String> users;

    public AuthenticationServer() {

        try {

            serverSocket = new ServerSocket(PORT);

            users = new Vector<>();

            while (true) {

                System.out.println("Listen on port " + PORT);

                clientSocket = serverSocket.accept();

                System.out.println("Atendiendo al usuario con IP " + clientSocket.getInetAddress().getHostAddress());

                Thread t = new Thread(this);

                t.start();

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (outToClient != null) {
                    outToClient.close();
                }
                if (inFromClient != null) {
                    inFromClient.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    @Override
    public void run() {

        try {

            inicializarStreams();

            String input = String.valueOf(receive());

            String username = getUsername(input);

            String message;

            if (usernameExists(username)) {

                message = "ACCESO DENEGADO";

            } else {

                message = "BIENVENIDO " + username + ", USTED ES EL USUARIO N° " + getVectorSize();

            }

            send(message);

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    }

    public void inicializarStreams() throws IOException {
        outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
        inFromClient = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void send(Object o) throws IOException {
        outToClient.writeObject(o);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return inFromClient.readObject();
    }

    public boolean usernameExists(String username) {
        if (!users.contains(username)) {
            users.add(username);
            return false;
        }
        return true;
    }

    public String getUsername(String input) {
        return input.split(" ")[1];
    }

    public int getVectorSize() {
        return users.size();
    }

    public static void main(String[] args) {

        new AuthenticationServer();

    }

}
