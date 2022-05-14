/**
 * PlatformServer.java
 * <p>
 * <p>
 * Created: Fri Jan 21 09:48:38 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform.communication;

import fr.lifl.magique.Message;
import fr.lifl.magique.platform.Platform;
import fr.lifl.magique.util.Name;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * The server thread for gathering messages from other platforms. It will start a thread per platform connection and
 * enable the sending of messages to other platform through the sendingMessage method.
 *
 * @see fr.lifl.magique.platform.communication.ConnectionHandler
 */
public class PlatformServer extends Thread implements Closeable {

    private final Platform platform;
    private ServerSocket server;
    private HashMap<String, Socket> agenda;
    private HashMap<Socket, ObjectOutputStream> outputStreams;

    /** @param platform the platform i am the server of
     */
    public PlatformServer(Platform platform) {
        this.platform = platform;
        this.agenda = platform.getPlatformAgenda();
        this.outputStreams = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(platform.getPort());
            while (true) {
                Socket connection = server.accept();
                ConnectionHandler handler = new ConnectionHandler(connection, this);
                agenda.put(connection.getInetAddress().getHostAddress() + ":" + connection.getLocalPort(), connection);
                outputStreams.put(connection, new ObjectOutputStream(connection.getOutputStream()));
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        server.close();
    }

    /** method remotely invoked by other platform to send a message to my platform
     *
     * @param message the sent message     *
     * @return <em>Boolean.TRUE</em> iff alive
     *
     * @see fr.lifl.magique.platform.rmi.Communicate
     */
    public void haveAMessage(Message message) throws ClassNotFoundException {
        platform.haveAMessage(message);
    }

    public void sendMessage(String destination, Message message) throws IOException {
        if (agenda.containsKey(destination)) {
            Socket socket = agenda.get(destination);
            ObjectOutputStream output;

            if (outputStreams.containsKey(socket)) {
                output = outputStreams.get(socket);
            } else {
                output = new ObjectOutputStream(socket.getOutputStream());
            }
            output.writeObject(message);
            output.flush();
        }
    }

    /** test if the platform <em>hostname</em> is alive
     *
     * @param platformName the platform to test
     *
     * @see fr.lifl.magique.platform.rmi.Connect
     */
    public Boolean ping(String platformName) {
        try {
            String hostname = Name.getHostName(platformName);
            int port = Integer.parseInt(Name.getPort(platformName));
            Socket socket = new Socket(hostname, port);
            socket.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /** method remotely invoked by other platform to connect to my platform
     *
     * @param platformName the name of the platform who wants to be connected to my platform
     *
     * @see fr.lifl.magique.platform.rmi.Connect
     */
    public void connect(String platformName) {
        if (agenda.containsKey(platformName)) {
            System.out.println(platformName + " already known");
        } else {
            System.out.println(platformName + " not yet known");
            boolean connected = false;
            while (!connected) {
                try {
                    String hostname = Name.getPlatformHostname(platformName);
                    int port = Integer.parseInt(Name.getPort(platformName));

                    Socket socket = new Socket(hostname, port);

                    agenda.put(platformName, socket);
                    outputStreams.put(socket, new ObjectOutputStream(socket.getOutputStream()));
                    connected = true;
                    System.out.println("connection with " + platformName + " performed");
                } catch (Exception e) {
                    e.printStackTrace();
                    connected = false;
                }
            }
        }
    }

    /**  performs disconnection fom <it>platformName</it>.
     */
    public void disconnect(String platformName) {
        try {
            System.out.println(platform.getName() + " disconnect from " + platformName);
            Socket socket = agenda.get(platformName);
            agenda.remove(platformName);
            outputStreams.remove(socket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} // PlatformServer
