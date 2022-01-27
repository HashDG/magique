package chap3;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PingSkill extends MagiqueDefaultSkill {
    public PingSkill(Agent a){ super(a); }
    
    public void ping() {
        System.out.println("ping");
        // requ�te sur la comp�tence pong d'un agent "anonyme"
        perform("pong"); 
    }  
} // PingSkill
