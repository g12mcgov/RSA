import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by grantmcgovern on 1/24/15.
 */

public class Test {
    public static void main(String[] args) throws IOException {
        //RSA rsa = new RSA(5);
        //RSA rsa = new RSA(1048, "keys.txt");
        //RSA rsa2 = new RSA("keys.txt");
        RSA rsa = new RSA(1048, "privateKey.txt", "publicKey.txt");
        //RSA rsa2 = new RSA("privateKey.txt");
        
        /* Apply a simple padding scheme to encode message */
        String message = "grant is very cool";
        System.out.println("Original Message: " + message);
        
        BigInteger encodedMessage = rsa.paddingScheme(message);
        System.out.println("Encoded Message: " + encodedMessage);

        String decoded = rsa.depaddingScheme(encodedMessage);
        System.out.println("Decoded Message (Test): " + decoded);
        
        BigInteger publicKey = rsa.e;
        BigInteger N = rsa.N;
        
        BigInteger encryptedMessage = rsa.encrypt(encodedMessage, N, publicKey);
        System.out.println("Encrypted Message: " + encryptedMessage);
        
        //encryptedMessage = new BigInteger("2216052557000060414707694116456812908723780528639431401289200053722601821380167881819710702819028469301688124655132594555811830220268319411272338017416205774486937302700697332906544526406117038256948687996522123547804859608816558506448143534927161945029420300402571856267623870968553701829525907561460222779143661213008158243671496254114848281322119769883355413549683818305902515867414401515243655599495455952529798467849688304126241200558008543115239932801429013016802212471357480462460380014728448186064254237207172748318267748207398655373950710117702537239255064255468300635079034090285965604491525241011939773273842807077237853");
        BigInteger decryptedMessage = rsa.decrypt(encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);
        
        String decodedMessage = rsa.depaddingScheme(decryptedMessage);
        System.out.println("Decoded Message: " + decodedMessage);

    }
}
