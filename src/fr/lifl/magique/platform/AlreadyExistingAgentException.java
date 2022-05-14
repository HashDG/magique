/**
 * AlreadyExistingAgentException.java
 * <p>
 * <p>
 * Created: Tue Jan 25 2000
 *
 * @author JC Routier
 * @version
 */
package fr.lifl.magique.platform;

public class AlreadyExistingAgentException extends RuntimeException {

    public AlreadyExistingAgentException(String agentName) {
        System.err.println("Already existing Agent : " + agentName);
    }

} // AlreadyExistingAgentException
