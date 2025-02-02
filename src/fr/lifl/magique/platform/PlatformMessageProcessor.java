/**
 * PlatformMessageProcessor.java
 * <p>
 * <p>
 * Created: Mon Jan 18 18:22:39 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform;


/**
 * this is simply use to treat a given PlatformMessage in a thread. Each
 * PlatformMessage is run in a separate thread which is instantiated by siuch
 * an object.
 *
 * @see Runnable
 */
public class PlatformMessageProcessor implements Runnable {

    /* the object that must execute PlatformMessage */
    private final Platform platform;
    /* the PlatformMessage to execute */
    private final PlatformMessage platformMessage;

    /**
     * @param platform           the object that must execute platformMessage
     * @param platformMessage the platformMessage to execute
     */
    public PlatformMessageProcessor(Platform platform, PlatformMessage platformMessage) {
        this.platform = platform;
        this.platformMessage = platformMessage;
    }

    /**
     * treats the PlatformMessage
     */
    public void run() {
        platform.treatMessage(platformMessage);
    }
} // RequestProcessor
