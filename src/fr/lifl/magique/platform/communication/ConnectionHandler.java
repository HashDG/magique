package fr.lifl.magique.platform.communication;

import fr.lifl.magique.Agent;
import fr.lifl.magique.Message;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * The thread for managing connection from an external platform to the platform. All the messages received are pulled up
 * to the PlatformServer with the PlatformServer#haveAMessage(Message) method.
 *
 * @see fr.lifl.magique.platform.communication.PlatformServer
 */
public class ConnectionHandler extends Thread implements Closeable {

    private Socket socket;
    private ObjectInputStream input;
    private PlatformServer platformServer;

    public ConnectionHandler(Socket socket, PlatformServer platformServer) throws IOException {
        this.socket = socket;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.platformServer = platformServer;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown()) {
                platformServer.haveAMessage((Message) input.readObject());
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
