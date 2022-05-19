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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * The server thread for gathering messages from other platforms. It will start a thread per platform connection and
 * enable the sending of messages to other platform through the sendingMessage method.
 *
 * @see ConnectionInputHandler
 */
public class PlatformServer extends Thread implements Closeable {
    private final Platform platform;
    private ServerSocket server;
    /*
    garder en mémoire le nom de la plateforme et le socket de communication déjà ouvert
     */
    private HashMap<String, ConnectionOutputHandler> agenda;
    /*
    réutiliser les outputStream pour éviter les désynchronisations
     */
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
            System.out.println("Server launched IP : " + InetAddress.getLocalHost().getHostAddress() + ":" + platform.getPort());
            while (true) {
                Socket connection = server.accept();
                ConnectionInputHandler handler = new ConnectionInputHandler(connection, this);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("I STOPPED RUNNING MY SERVER");
            e.printStackTrace();
        }
    }

    public Platform getPlatform() {
        return platform;
    }

    @Override
    public void close() throws IOException {
        System.out.println("I CLOSED MY SERVER");
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

    /**
     * the platform received from another platform its infos (hostname/port) for stocking in agenda
     */
    public void haveAInfoMessage(PlatformInfoMessage msg) throws IOException {
        String receiver = msg.recipient();
        // is the message addressed to me ?
        if (receiver.equals(platform.getName())) {
            PlatformInfo info = msg.platformInfo();
            String hostname = info.hostname();
            int port = info.port();

            // si cette plateforme ne connais pas la plateforme qui lui envoie le message, il initie un agenda avec
            // socket enregistré pour une communication future il stocke aussi un nouvel oos

            if (!agenda.containsKey(hostname)) {
                ConnectionOutputHandler handler = new ConnectionOutputHandler(hostname, port, this);
                agenda.put(hostname, handler);
            }
        }
    }

    public void sendMessage(String destination, Message message) throws IOException {
        if (agenda.containsKey(destination)) {
            ConnectionOutputHandler outputHandler = agenda.get(destination);
            if (!outputHandler.isConnected()) {
                outputHandler.connect();
            }

            outputHandler.addMessage(message);
        } else {
            connect(destination);
            sendMessage(destination, message);
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
            try {
                String hostname = Name.getPlatformHostname(platformName);
                int port = Integer.parseInt(Name.getPort(platformName));



                // send a message to introduce my platform to the remote one
                ConnectionOutputHandler handler = new ConnectionOutputHandler(hostname, port, this).connect();
                handler.start();

                // stocker la platform distante dans mon agenda
                agenda.put(platformName, handler);
                System.out.println("connection with " + platformName + " performed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**  performs disconnection fom <it>platformName</it>.
     */
    public void disconnect(String platformName) {
        try {
            System.out.println(platform.getName() + " disconnect from " + platformName);
            ConnectionOutputHandler connection = agenda.get(platformName);

            agenda.remove(platformName);
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} // PlatformServer
