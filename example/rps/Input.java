
/**
 * Une classe utilitaire pour la saisie de cha�nes ou d'entiers sur l'entr�e standard.
 *
 *
 * Created: Mon Oct 07 15:53:21 2002
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
public class Input {

    /** permet la saisie d'une cha�ne sur l'entr�e standard
     * @return la cha�ne saisie
     */
    public static String readString() throws java.io.IOException {	
	return new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).readLine();
    }

    /** permet la saisie d'un entier sur l'entr�e standard
     * @return l'entier saisi
     */
    public static int readInt() throws java.io.IOException {
	int n;
	try {
	    n=Integer.parseInt(readString());
	} catch(NumberFormatException e) {
	    throw new java.io.IOException();
	}
	return n;
    }
    /** permet la saisie d'un char sur l'entr�e standard
     * @return le char saisi (les uatres caract�res sont ignor�s et "perdus")
     */
    public static char readChar() throws java.io.IOException {
	return (char) new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).read();
    }

}// Input
