package fr.lifl.magique.platform.communication;

import java.io.Serializable;

public record PlatformInfo(String hostname, int port) implements Serializable {
    @Override
    public String toString() {
        return "hostname " + hostname + " port " + port;
    }
}
