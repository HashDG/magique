package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PongSkill2 extends MagiqueDefaultSkill {
   public PongSkill2(Agent a){ super(a); }
    
   public void pong() {
      System.out.println("pong");
      // requ�te sur la comp�tence ping d'un agent "anonyme"
      perform("ping"); 
    }  
} // PongSkill
