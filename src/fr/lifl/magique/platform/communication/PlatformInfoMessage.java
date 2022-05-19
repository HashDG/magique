package fr.lifl.magique.platform.communication;

import fr.lifl.magique.Message;

/**
 * Specific message for transferring platforms information
 *
 * Specific messages between platforms : a name and a magique message
 *
 * @param recipient
 * @param platformInfo
 * @see Message
 */
public record PlatformInfoMessage(String recipient, PlatformInfo platformInfo) implements Message {

    public String toString() {
        return "to " + recipient + " content " + platformInfo.toString();
    }
}
