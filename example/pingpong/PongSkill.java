import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PongSkill extends MagiqueDefaultSkill {
   public PongSkill(Agent a){ super(a); }
    
   public void pong(Integer i) {
       //      System.out.println("pong "+i);
     perform("display",new Object[] {"pong "+ i});
      // requ�te sur la comp�tence ping d'un agent "anonyme"
     perform("ping", new Integer(i.intValue()+1)); 
    }  
} // PongSkill
