/**
 * Created by grantmcgovern on 1/13/15.
 */

import java.io.*;
import java.math.BigInteger;
import java.io.IOException;
import java.util.Scanner;
import java.net.URL;

public class RSA {
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
        BigInteger s;
        BigInteger[] packet;
        
        /* Assign p & q -- Ensures they are distinct */
        while(true) {
            if(p.equals(q)) {
                p = ModularArithmetic.genPrime(n);
                q = ModularArithmetic.genPrime(n);
            }
            else
                break;
        }
        
        N = p.multiply(q);
        s = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = ModularArithmetic.genPrime(s.bitLength());
        packet = ModularArithmetic.extendedEuclid(s, e);

        while(!packet[2].equals(BigInteger.ONE) && !(e.compareTo(s) == -1)) {
            e = ModularArithmetic.genPrime(s.bitLength());
            packet = ModularArithmetic.extendedEuclid(s, e);

        }
        //System.out.println(e.modInverse(s));

        d = ModularArithmetic.moddiv(BigInteger.ONE, e, s);
        
        //BigInteger[] val = ModularArithmetic.extendedEuclid(s, e);
        //d = val[1];

        /* In the case d is negative, handle it. */
        if(d.compareTo(BigInteger.ZERO) == -1) d = d.add(s);
        
        System.out.println("Public Key: " + e);

        /* Debug */
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + N);
        System.out.println("s: " + s);
        System.out.println("e: " + e);
        System.out.println("d: " + d);

    }

    
    public RSA(int n, String privateFile, String publicFile) throws IOException { 
        /* Borrows constructor definition from above */
        this(n);
        
        try {
            File privateDirectory = new File(".");
            File publicDirectory = new File(".");
            
            File pri = new File(privateDirectory.getCanonicalFile() + File.separator + privateFile);
            File pub = new File(publicDirectory.getCanonicalFile() + File.separator + publicFile);
            
            if(!pri.exists() || !pub.exists()) {
                pri.createNewFile();
                pub.createNewFile();
            }
            
            FileWriter privateWriter = new FileWriter(pri.getAbsoluteFile());
            FileWriter publicWriter = new FileWriter(pub.getAbsoluteFile());
            
            BufferedWriter privateBufferedWriter = new BufferedWriter(privateWriter);
            BufferedWriter publicBufferedWriter = new BufferedWriter(publicWriter);

            privateBufferedWriter.write(String.valueOf(this.N)+"\n");
            publicBufferedWriter.write(String.valueOf(this.N)+"\n");
            
            privateBufferedWriter.write(String.valueOf(this.d));
            publicBufferedWriter.write(String.valueOf(this.e));
            
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
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* For a given integer c < N, use the private key to return the decrypted message
       c = m^e (mod N)
     */
    public BigInteger encrypt(BigInteger m, BigInteger N, BigInteger e) {
        BigInteger encrypted_message = ModularArithmetic.modexp(m, e, N);
        return encrypted_message;
    }

    /* For an integer c < N, use the private key to return the decrypted message
       m = c^d (mod N)
     */
    public BigInteger decrypt(BigInteger c) {
        //BigInteger l = new BigInteger("2632626703453331370625164169676227306676274812042680265181549782784364195103381960876771897212046308653212370641672788861398031781992474390970885498200736580812304184316620248968732539556744108037824617685198813363633091779750845943725007674980805475865955715271713057943525038638383585986601364875911505416453760755804479696954296937397913480047334351469859735742110423099194196183308106572490045846418859980569319850459193411666408459173440055686473511381539775510608718051315363903987979912166186929128792186839712312738034783371542860880071795347799817450669225939358336636218363468275013700785414884564592973097191319187456249");
        //BigInteger q = new BigInteger("3619131669228653945441135947905405219110746986064840788153530492206904588344706512253421713504358967205702112814602852284267624424823301352321151331460867647836755829943558718509765119008535914181248077582167378983678564807613707976423759469094146481086588269261774745440611934655096472647170808367144452789736894169051956843244284375362161178780827321771920099780147848060221557035815010174232504327293053304318809640652426995960782556817962704412051019261431632365459052733091134964815852029087682390458643505868404343276650031238304582919143040655417332673141603475503121930776470067414505305994657675480618864214489886469263113");
        BigInteger decrypted_message = ModularArithmetic.modexp(c, this.d, this.N);
        return decrypted_message;
    }
    
    /*
     * paddingScheme(string message)
     * 
     * ~ Takes in a message and encodes using the following padding scheme below. As provided by Dr. Pauca.
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
    
    public String depaddingScheme(BigInteger msg) {
        //String encodedMessage = msg.toString();

        String encodedMessage = msg.toString();

        if (encodedMessage.length() % 3 == 1)
        {
            System.out.println("error in decryption");
            System.exit(0);
        }
        else if (encodedMessage.length() % 3 == 2)
        {
            encodedMessage = "0"+encodedMessage;
        }
        
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

}
