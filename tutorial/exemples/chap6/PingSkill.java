package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PingSkill extends MagiqueDefaultSkill {
    public PingSkill(Agent a){ super(a); }
    
    public void ping() {
        System.out.println("ping");
        perform("pong"); // requ�te sur la comp�tence pong d'un agent "anonyme"
    }  
} // PingSkill
