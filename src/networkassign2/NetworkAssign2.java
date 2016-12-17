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
            
            // Logging the Encryption Calculation.
            int index = 0;
            for(int i=0;i<p.size();i+=subSize)
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

            for(int i=0; i<encryptedMsgList.length; i++)
            {
                System.out.print(encryptedMsgList[i]+ "    ");
            }
            System.out.println("");
            System.out.println("");


            System.out.print("----------------------");



            // Decryption
            int decryptedMsgList [] = decrypt(encryptedMsgList,wInverse, n);

            // convert the decryption to binary based on the private key S.
            String [] toBinary = makeMeBinary(decryptedMsgList, s, subSize);


            System.out.println("");
            System.out.println("Decryption:");
            System.out.println("----------------------");

            // Logging the decryption calculations
            for(int i=0; i< H.length;i++)
            {
                System.out.println(H[i]+" * "+wInverse+" = "+ (H[i]+wInverse) + " % "+n+" = "+decryptedMsgList[i] + " = " +toBinary[i]);
            }

            System.out.println("");
            System.out.println("");

            // Logging the plain text decrypted. 
            System.out.println("The Decrypted Plaintext");

            for(int i=0; i<toBinary.length; i++)
            {
                System.out.print(toBinary[i]+ "    ");
            }
            System.out.println("");
            System.out.println("");
            System.out.print("----------------------\n");
        
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
        int subSize = p.size()/H.length;
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

  
    
    
    public static String[] makeMeBinary(int decryptedMsg[], ArrayList<Integer> s, int subSize) 
    {

        double count = Math.pow(2, subSize);
        String bin[] = generateBinary((int) count);
        int [] sumValues = getSumValues(bin, (int) count, subSize, s);
        String [] binValues = getBinVaues(bin, (int) count, subSize, s);
        String [] plainTextResult = new String[s.size()];
        for (int i = 0; i < decryptedMsg.length; i++) 
        {
          
            for(int j=0; j<sumValues.length; j++ )
            {
                   
                if(decryptedMsg[i] == sumValues[j])
                {
                    plainTextResult[i]=binValues[j];
                }
            }
        }
      
        StringBuilder text = new StringBuilder("0000");
            for (int i = 0; i < decryptedMsg.length; i++) 
            {
          
                for(int j=0; j<s.size(); j++ )
                {
                    if(decryptedMsg[i] == s.get(j))
                    {
                        text.setCharAt(j,'1');
                        plainTextResult[i]=text.toString();
                    
                    }
                }
            }
        return plainTextResult;
    }

    
    public static String[] generateBinary(int count) 
    {
        String bin[] = new String[count];
        for (int i = 0; i < count; i++) 
        {
            bin[i] = String.format("%04d",Integer.valueOf(Integer.toBinaryString(i)) );
        }
        return bin;
    }
    
    public static int[] getSumValues(String[] bin, int count , int subSize , ArrayList<Integer>s)
    {
        int sum;
        int result[] = new int[ count];
        int value = 0;
        for(int i = 0; i < bin.length; i++) 
        {
            sum = 0;
            if (bin[i].length() == subSize) {
                for (int j = 0; j < bin[i].length(); j++) 
                {
                    if (bin[i].charAt(j) == '1') 
                    {
                        sum = sum + s.get(j);
                    }
                }
                result[value] = sum;
                value++;
            }
        }
        return result;
    }
    
    
      public static String[] getBinVaues(String[] bin, int count , int subSize , ArrayList<Integer> s)
      {
        String result[] = new String[count];
        int value = 0;
        for (int i = 0; i < bin.length; i++) 
        {
            if (bin[i].length() == subSize) 
            {
                result[value] = bin[i];
                value++;
            }
        }
        return result;
    } 
}
