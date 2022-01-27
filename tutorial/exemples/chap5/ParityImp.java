package chap5;
import fr.lifl.magique.*;

public class ParityImp extends AbstractMagiqueMain{
    // args[0] = super host name 
    public void theRealMain(String[] args) {
    if (args.length == 2) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
    }  
              // cr�ation du superviseur "math�matique"
    Agent supermath = createAgent("supermath");  
              // connexion � son superviseur
    supermath.connectToBoss("super@"+args[0]);
              // cr�ation de l'agent pour la parit�
    Agent parity = createAgent("parity");
             // enseignement de la comp�tence de parit�
    parity.addSkill(new chap5.ParitySkill());
             // connexion � son superviseur
    parity.connectToBoss("supermath");
  }
}
