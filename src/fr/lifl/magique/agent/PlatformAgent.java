/**
 * PlatformAgent.java
 * <p>
 * <p>
 * Created: Wed Mar 01 11:46:40 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.agent;

import fr.lifl.magique.Agent;
import fr.lifl.magique.AtomicAgent;
import fr.lifl.magique.platform.Platform;
import fr.lifl.magique.platform.classloader.BytecodeClassLoader;
import fr.lifl.magique.platform.classloader.ClassArchive;

import static fr.lifl.magique.platform.Platform.PLATFORMMAGIQUEAGENTNAME;

public class PlatformAgent extends AtomicAgent {

    private final BytecodeClassLoader myLoader;
    private final Platform platform;

    public PlatformAgent(Platform platform) {
        super(PLATFORMMAGIQUEAGENTNAME);
        this.platform = platform;
        myLoader = (BytecodeClassLoader) this.getClass().getClassLoader();
    }

    public void addClassArchive(ClassArchive classArchive) {
        myLoader.addClassArchive(classArchive);
    }

    public void giveClassArchive(String className, String to) {
        ClassArchive classArchive = null;

        try {
            myLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            System.out.println(getName() + " : giveClassArchive : " + className + " not found");
        }
        if (myLoader.knownClassArchive(className.replace('/', '.'))) {
            classArchive = myLoader.getClassArchive(className.replace('/', '.'));
        }
        String otherName = PLATFORMMAGIQUEAGENTNAME + "@" + fr.lifl.magique.util.Name.noShortName(to);
        if (!getAgenda().containsKey(otherName)) {
            connectTo(otherName);
        }
        askNow(otherName, "addClassArchive", classArchive);
    }

    public Class getClass(String className) {
        Class myClass = null;
        try {
            myClass = myLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            System.out.println("agent class " + className + " not found");
            e.printStackTrace();
        }
        return myClass;
    }

    public void addURL(java.net.URL url) {
        myLoader.addURL(url);
    }

    public void killPlatform() {
        platform.stop();
    }

    /**
     * create and add an agent on this platform
     *
     * @param agentName
     *           the shortName of the agent to be created
     * @return the full name of the created agent
     */
    public String createAgent(String agentName) {
        Agent.setVerboseLevel(2);
        Agent a = getPlatform().createAgent(agentName);
        getPlatform().addAgent(a);
        return a.getName();
    }

    /**
     * create and add an agent on a remote platform
     *
     * @param agentName
     *           the shortName of the agent to be created
     * @param platformName
     *           the remote platform
     * @return the full name of the created agent
     */
    public String createDistantAgent(String agentName, String platformName) {
        connectTo(PLATFORMMAGIQUEAGENTNAME + "@" + platformName);
        return (String) askNow(PLATFORMMAGIQUEAGENTNAME + "@" + platformName, "createAgent", agentName);
    }

    /**
     * create and add an agent on a remote platform and connect it to a boss
     *
     * @param agentName
     *           the shortName of the agent to be created
     * @param platformName
     *           the remote platform
     * @param boss
     *           the boss to connect to
     * @return the full name of the created agent
     */
    public String createDistantAgentAndConnectToBoss(String agentName, String platformName, String boss) {
        connectTo(PLATFORMMAGIQUEAGENTNAME + "@" + platformName);
        String agName = (String) askNow(PLATFORMMAGIQUEAGENTNAME + "@" + platformName, "createAgent", agentName);
        perform(PLATFORMMAGIQUEAGENTNAME + "@" + platformName, "connectAgentToBoss", agName, boss);
        return agName;
    }

    public void connectAgentTo(String agentName, String otherAgentName) {
        perform(agentName, "connectTo", otherAgentName);
    }

    public void connectAgentToBoss(String agentName, String bossName) {
        perform(agentName, "connectToBoss", bossName);
    }
} // PlatformAgent
