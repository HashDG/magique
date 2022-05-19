package fr.lifl.magique.platform.communication;

import fr.lifl.magique.Message;
import fr.lifl.magique.platform.PlatformMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * The thread for managing connection from an external platform to the platform. All the messages received are pulled up
 * to the PlatformServer with the PlatformServer#haveAMessage(Message) method.
 *
 * Manage incoming messages
 * @see fr.lifl.magique.platform.communication.PlatformServer
 */
public class ConnectionInputHandler extends Thread implements Closeable {

    private Socket socket;
    private ObjectInputStream input;
    private PlatformServer platformServer;

    public ConnectionInputHandler(Socket socket, PlatformServer platformServer) throws IOException {
        this.socket = socket;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.platformServer = platformServer;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown()) {
                Object received = input.readObject();
                if (received instanceof PlatformMessage) {
                    platformServer.haveAMessage((Message) received);
                } else if (received instanceof PlatformInfoMessage) {
                    platformServer.haveAInfoMessage((PlatformInfoMessage) received);
                } else {
                    System.err.println("Unreadable message :" + received.getClass().getName() + " > " + received);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        input.close();
        socket.close();
    }
}
