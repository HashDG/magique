package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PongSkill extends MagiqueDefaultSkill {
    public PongSkill(Agent a){ super(a); }
    
    public void pong() {
        System.out.println("pong");
        perform("ping"); // requ�te sur la comp�tence ping d'un agent "anonyme"
    }  
} // PongSkill
