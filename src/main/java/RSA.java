/*
 * Author: Grant McGovern
 * Date: 1/13/15
 *  
 * Description: This class implements several RSA constructors, along with an encryption() and decryption() method
 * 
 */

import java.io.*;
import java.math.BigInteger;
import java.io.IOException;
import java.util.Scanner;

public class RSA {
    /* N value */
    public static BigInteger N;
    
    /* Private Key */
    private static BigInteger d;
    /* Public Key */
    public static BigInteger e;

    /*  Generates a public (N, e) and private (N, d) RSA key pair, where N, e, d
        are numbers of approximately n bits in length. the private key is stored
        as a private field of the class and the public key is printed to standard
        output.
     */
    public RSA(int n) {
        /* Initialize BigInteger Variables */
        BigInteger p = ModularArithmetic.genPrime(n);
        BigInteger q = ModularArithmetic.genPrime(n);
        
        /* If p & q are the same, generate new ones */
        while(p.equals(q)) {
            p = ModularArithmetic.genPrime(n);
            q = ModularArithmetic.genPrime(n);
        }
        
        BigInteger s;
        BigInteger[] packet;
        
        
        /* Set our value of N = pq */
        N = p.multiply(q);
        /* Set our value of s = (p-1)(q-1) */
        s = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        /* Set e = some random prime BigInteger where e is 1 < e < s */
        e = ModularArithmetic.genPrime(s.bitLength() - 2);
        
        packet = ModularArithmetic.extendedEuclid(s, e);

        /* 
         * Here we check to see if the GCD = 1. If it doesn't and e is greater than s,
         * recompute both the GCD and e.
         */
        while(!packet[2].equals(BigInteger.ONE) && !(e.compareTo(s) == -1)) {
            e = ModularArithmetic.genPrime(s.bitLength() - 2);
            packet = ModularArithmetic.extendedEuclid(s, e);
        }

        /* Obtain a value for d */
        d = ModularArithmetic.moddiv(BigInteger.ONE, e, s);

        /* In the case d is negative, handle it. */
        if(d.compareTo(BigInteger.ZERO) == -1) d = d.add(s);
        
        /* Outputs the Public Key */
        System.out.println("Public Key: " + e);

        /* Debug */
        printValues(p, q, N, s, e, d);

    }

    /* 
     *  RSA(int, String, String)
     * 
     *  ~ This method constructs an RSA object, given a specific input, n. 
     *    It also generates and reads out the private and public keys to the
     *    filenames passed in. It expects the privateFile to be the first arg
     *    and the publicFile to be the second arg.
     */
    public RSA(int n, String privateFile, String publicFile) throws IOException { 
        /* Borrows constructor definition from above */
        this(n);
        
        /* Wrapped in a try/catch block to handle and report any I/O errors */
        try {
            /* Simple File IO, opening it, etc... */
            File privateDirectory = new File(".");
            File publicDirectory = new File(".");
            
            File pri = new File(privateDirectory.getCanonicalFile() + File.separator + privateFile);
            File pub = new File(publicDirectory.getCanonicalFile() + File.separator + publicFile);
            
            /* If our file doesn't exist, create it */
            if(!pri.exists() || !pub.exists()) {
                pri.createNewFile();
                pub.createNewFile();
            }
            
            FileWriter privateWriter = new FileWriter(pri.getAbsoluteFile());
            FileWriter publicWriter = new FileWriter(pub.getAbsoluteFile());
            
            BufferedWriter privateBufferedWriter = new BufferedWriter(privateWriter);
            BufferedWriter publicBufferedWriter = new BufferedWriter(publicWriter);
            
            /* Write out the N value for both keys */
            privateBufferedWriter.write(String.valueOf(this.N)+"\n");
            publicBufferedWriter.write(String.valueOf(this.N)+"\n");
            
            /* Write out privateKey, then publicKey */
            privateBufferedWriter.write(String.valueOf(this.d));
            publicBufferedWriter.write(String.valueOf(this.e));
            
            /* Lastly, close our files */
            privateBufferedWriter.close();
            publicBufferedWriter.close();
            
        } catch (IOException err) {
            err.printStackTrace();
        }
    }


    /* Reads in the private key stored in the file "filename" */
    public RSA(String filename) throws IOException {
        File directory = new File(".");
        File file = new File(directory.getCanonicalPath() + File.separator + filename);

        try {

            Scanner sc = new Scanner(file);

            /* Pulls off the first value from the file, which is N */
            this.N = sc.nextBigInteger();
            System.out.println("Read in N: " + this.N);
            
            /* Pulls off the second value from the file, which is the private key */
            BigInteger privateKey = sc.nextBigInteger();
            
            System.out.println("Read in Private Key: " + privateKey);
            this.d = privateKey;

            sc.close();
        }
        catch (FileNotFoundException err) {
            err.printStackTrace();
        }
    }

    /* 
     * For a given integer c < N, use the private key to return the decrypted message
     * c = m^e (mod N)
     */
    public BigInteger encrypt(BigInteger m, BigInteger N, BigInteger e) {
        BigInteger encrypted_message = ModularArithmetic.modexp(m, e, N);
        return encrypted_message;
    }

    /* 
     * For an integer c < N, use the private key to return the decrypted message
     * m = c^d (mod N)
     */
    public BigInteger decrypt(BigInteger c) {
        BigInteger decrypted_message = ModularArithmetic.modexp(c, this.d, this.N);
        return decrypted_message;
    }
    
    /*
     * paddingScheme(string message)
     * 
     * ~ Takes in a message and encodes using the following padding scheme below.
     * 
     * via @Dr.Pauca 
     */
    public BigInteger paddingScheme(String message) {
        int c;
        String intMessage = "";  // corresponding integer message to encrypt

        for(int i = 0; i < message.length(); i++)
        {
            c = message.charAt(i);
            intMessage = intMessage + String.format("%1$03d", c);
        }
        
        return new BigInteger(intMessage);
    }

    /*
     * depaddingScheme(string message)
     * 
     * ~ Takes in a message and decodes using the following depadding scheme below.
     * 
     * via @Dr. Pauca
     */
    public String depaddingScheme(BigInteger msg) {
        //String encodedMessage = msg.toString();

        String encodedMessage = msg.toString();

        if (encodedMessage.length() % 3 == 1)
        {
            System.out.println("error in decryption");
            System.exit(0);
        }
        else if (encodedMessage.length() % 3 == 2)
            encodedMessage = "0"+encodedMessage;
        
        String decryptedMessage = "";
        int a = 0;
        int b = 0;
        for (int i = 0; i < encodedMessage.length()/3; i++)
        {
            a = encodedMessage.charAt(3*i);   // decimal of charAt(3*i)
            // e.g. 48 for '1', 52 for '4'
            a = (a-48)*100;
            b = encodedMessage.charAt(3*i+1);
            a += (b-48)*10;
            b = encodedMessage.charAt(3*i+2);
            a += (b-48);
            decryptedMessage += (char)a;
        }
        
        return decryptedMessage;
    }
    
    /* 
     * A simple helper method to display values of p, q, N, s, e, & d.
     * (mainly to be used via debugging)
     */
    public void printValues(BigInteger p, BigInteger q, BigInteger N, BigInteger s, BigInteger e, BigInteger d) {
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + N);
        System.out.println("s: " + s);
        System.out.println("e: " + e);
        System.out.println("d: " + d);
    }

}
