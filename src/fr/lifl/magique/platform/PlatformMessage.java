/**
 * PlatformMessage.java
 * <p>
 * <p>
 * Created: Tue Jan 25 10:44:42 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform;

import fr.lifl.magique.Message;

/**
 * Specific messages between platforms : a name and a magique message
 *
 * @see Message
 */

public record PlatformMessage(String recipient, Message content) implements Message {

    public String toString() {
        return "to " + recipient + " content " + content;
    }

    //      private void writeObject(ObjectOutputStream output) throws IOException {
//  	System.out.println("write "+this);
//  	output.defaultWriteObject();
//      }

//      private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
//  	try {
//  	    System.out.println("read "+this);
//  	    input.defaultReadObject();
//  	}
//  	catch (IOException e) {
//  	    e.printStackTrace();
//  	}
//  	catch (ClassNotFoundException e) {
//  	    e.printStackTrace();
//  	}
//      }

} // PlatformMessage
