/*
 * Network Assignment #2 _ Student_Id = 2004-3-325

 */
package networkassign2;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;
import java.util.ArrayList;
 


/**
 *
 * @author mohamedramadan
 */
public class NetworkAssign2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {     
        try 
        {
            // Load the Input configuration file that maintain the inputs.
            File fXmlFile = new File("src/networkassign2/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

	
            // Read the inputs from config.xml
            int w = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("w").item(0).getTextContent());
            int wInverse = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("winverse").item(0).getTextContent());
            int n = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("n").item(0).getTextContent());
            
            String stringElementS =doc.getDocumentElement().getElementsByTagName("s").item(0).getTextContent();
            String [] items = stringElementS.split(",");
            ArrayList<Integer> s = new ArrayList<Integer>();
            for(int i=0; i<items.length;i++)
            {
                s.add(Integer.parseInt(items[i]));
            }
        
        
            String [] plaintext = doc.getDocumentElement().getElementsByTagName("plaintext").item(0).getTextContent().split(",");
            ArrayList<Integer> p = new ArrayList<Integer>();
            for(int i=0; i<plaintext.length;i++)
            {
                p.add(Integer.parseInt(plaintext[i]));
            }       
        
        
        
            // Log the inputs. 
            System.out.println("The Input Set:");
            System.out.print("----------------------");
            System.out.println("");
            System.out.print("S: ");
            for(int i = 0;i<s.size() ;i++)
            {
                System.out.print(s.get(i));
                if(i!=s.size()-1)
                {
                    System.out.print(", ");
                }
            }

            System.out.println("");
            System.out.print("p: ");
            for(int i=0; i<p.size();i++)
            {
                System.out.print(p.get(i));
            }
            System.out.println("");
            System.out.println("W: " + w);
            System.out.println("n: " + n);
            System.out.println("W-1: " + wInverse);
            System.out.print("----------------------\n");



            // Calculating the Public Key H.
            int H[] = generatePublicKeyH(w, n, s);
            
            // Loging the calculation of H.
            System.out.println("Caluclating Public Key:");
            System.out.println("----------------------");
            for(int i=0; i<s.size();i++)
            {
                System.out.println(s.get(i) + " * " + w + " % " + n + " = " + H[i]);
            }
            System.out.println("");
            System.out.print("H =  ");
            for (int i = 0; i < H.length; i++) 
            {
                System.out.print(H[i]);
                if(i!=H.length-1)
                {
                    System.out.print(", ");
                }
            }
            System.out.print("\n----------------------\n");



            // Ecncryption
            ArrayList<Integer> ecryptedMsg = new ArrayList<Integer>();
            int encryptedMsgList [] = encrypt(p,H);

            System.out.println("");
            System.out.println("Encryption:");

            // calculating the subsize of each octit of the plain text based on the length of Knapsack.
            int subSize = p.size()/H.length;
            //System.out.print("heeeeeeeeeeeeeeer"+subSize);
            
            // Logging the Encryption Calculation.
            int index = 0;
            for(int i=0;i<subSize;i++)
            {

                System.out.println("");
                System.out.print("[ ");
                for (int j=i; j<subSize+i; j++)
                {

                    System.out.print(p.get(j));
                    if(i!=p.size()-1)
                    {
                        System.out.print(", ");
                    }

                }
                System.out.print("] * ");
                System.out.print("[ ");
                for(int k=0; k<H.length;k++)
                {
                    System.out.print(H[k]);
                    if(i!=H.length-1)
                    {
                        System.out.print(", ");
                    }
                }
                System.out.print("] = " + encryptedMsgList[index]);
                index ++;
            }

            System.out.println("");
            System.out.println("");



            // Logging the final encrypted msg.
            System.out.println("The Encrypted Message");

            for(int i=0; i<subSize; i++)
            {
                System.out.print(encryptedMsgList[i]+ "    ");
            }
            System.out.println("");
            System.out.println("");


            System.out.print("----------------------");



            // Decryption
            int decryptedMsgList [] = decrypt(encryptedMsgList,wInverse, n);

            
            

            System.out.println("");
            System.out.println("Decryption:");
            System.out.println("----------------------");

            // Logging the decryption calculations
            for(int i=0; i< subSize;i++)
            {
                System.out.println(H[i]+" * "+wInverse+" = "+ (H[i]+wInverse) + " % "+n+" = "+decryptedMsgList[i]);
            }

            System.out.println("");
            System.out.println("");

            // Logging the  decrypted Msg. 
            System.out.println("The Decrypted Message");

            for(int i=0; i<subSize; i++)
            {
                System.out.print(decryptedMsgList[i]+ "    ");
            }
            System.out.println("");
            System.out.println("");
            System.out.print("----------------------\n");
            System.out.println("");
            System.out.println("");

            

            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        
    }
    
    
    
    
    
    
    
    public static int[] generatePublicKeyH(int w, int n, ArrayList<Integer> s) 
    {
        int H[] = new int[s.size()];
        for (int i = 0; i < s.size(); i++) 
        {
            H[i] = s.get(i) * w % n;
        }
        return H;
    }
   
    
    
   
    public static int[] encrypt(ArrayList<Integer> p ,int H[])
    {
    
        int msg[] = new int[H.length];
        int indexOfencrptMsg = 0;
        int value;
        
        /* the trick that solve the issue 
            was to update the value of the subSize which represents the
            number of partitions the plain text should be devided into
            by making it equal to = plain.size / (plain.size / S.length)
        
        */
        int mySubSize = p.size()/H.length;
        int subSize = p.size()/mySubSize;
        for (int i = 0; i < p.size(); i += subSize) 
        {
            value = 0;
            for (int j = 0; j < subSize; j++) 
            {
                value = value + H[j] * p.get(i + j);
            }
            msg[indexOfencrptMsg] = value;
            indexOfencrptMsg++;
        }
        return msg;
    }

    
    public static int[] decrypt(int encrytedMsg[], int wInverse, int n) 
    {
        int decrytedMsg[] = new int[encrytedMsg.length];
        for (int i = 0; i < encrytedMsg.length; i++) 
        {
            decrytedMsg[i] = encrytedMsg[i] * wInverse % n;
        }
        return decrytedMsg;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
