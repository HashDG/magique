package fr.lifl.magique.platform.communication;

import fr.lifl.magique.Message;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Manage outcoming communication from a PlatformServer to an another. The PlatformInfoMessage is used to make aware the
 * receiver of our hostname + port
 *
 * @see fr.lifl.magique.platform.communication.PlatformServer
 */
public class ConnectionOutputHandler extends Thread implements Closeable {

    // messages to send, received from PlatformServer
    private LinkedList<Message> messages;
    private ObjectOutputStream output;
    private Socket socket;
    private boolean connected = false;
    private String host;
    private int port;
    private PlatformServer server;

    public ConnectionOutputHandler(String host, int port, PlatformServer server) {
        messages = new LinkedList<>();
        this.host = host;
        this.port = port;
        this.server = server;
    }

    public ConnectionOutputHandler connect() throws IOException {
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        connected = true;
        return this;
    }

    @Override
    public void run() {
        while (connected) {
            try {
                // message to introduce the platform
                Message info = new PlatformInfoMessage(host, new PlatformInfo(server.getPlatform().getName(), server.getPlatform().getPort()));
                addMessage(info);

                // will send messages
                while (true) {
                    synchronized (messages) {
                        if (!messages.isEmpty()) {
                            Message message = messages.pop();
                            output.writeObject(message);
                            output.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        output.close();
        socket.close();
    }

    public void addMessage(Message message) {
        synchronized (messages) {
            messages.push(message);
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
