import com.sun.org.apache.xpath.internal.operations.Mod;

import java.math.BigInteger;
import java.util.Random;
import java.util.HashMap;

public class ModularArithmetic {

    /* modAdd()
     *
     * ~ Adds two BigInteger values and Mods by N 
     */
    public static BigInteger modadd(BigInteger a, BigInteger b, BigInteger N) {
        BigInteger c = a.add(b).mod(N);
        return c;
    }

    /* modMult()
     *
     * ~ Multiplies two BigInteger values and Mods by N 
     */
    public static BigInteger modmult(BigInteger a, BigInteger b, BigInteger N) {
        BigInteger c = a.multiply(b).mod(N);
        return c;
    }

    /* modExp()
     *
     * ~ Raises two BigInteger values to the power a^b and Mods by N 
     */
    public static BigInteger modexp(BigInteger a, BigInteger b, BigInteger N) {
        BigInteger c = a.modPow(b, N);
        return c;
    }

    /* modDiv()
     *
     * ~ Divides two BigInteger values and Mods by N 
     */
    public static BigInteger moddiv(BigInteger a, BigInteger b, BigInteger N) {
        BigInteger[] gcd = extendedEuclid(N, b);

        BigInteger val = a.multiply(gcd[1].mod(N)).mod(N);
        
        return val;
    }

    /* extendedEuclid()
     *
     * ~ Multiplies two BigInteger values and Mods by N
     */
    public static BigInteger[] extendedEuclid(BigInteger a, BigInteger b) {
        /*
         * res[0] = x
         * res[1] = y
         * res[2] = d
         * ax + by = d = gcd(a, b)
         */
        
        BigInteger[] res = new BigInteger[3];

        if (b.compareTo(BigInteger.ZERO) == 0) {
            res[0] = BigInteger.ONE;
            res[1] = BigInteger.ZERO;
            res[2] = a;
        } else {
            BigInteger[] packet = extendedEuclid(b, a.mod(b));
            res[0] = packet[1];
            res[1] = packet[0].subtract(packet[1].multiply(a.divide(b)));
            res[2] = packet[2];
        }

        return res;
    }

    /* isPrime()
     *
     * ~ Determines whether a BigInteger N is prime, within a certain probability
     */
    public static boolean isPrime(BigInteger N, int k) {
        BigInteger NEGONE = new BigInteger("-1");
        
        for (int i = 0; i < k; i++) {
            Random rand = new Random(Integer.MAX_VALUE);
            BigInteger upperLimit = N.add(NEGONE);

            BigInteger a;

            do {
                a = new BigInteger(upperLimit.bitLength(), rand);
            }
            while (a.compareTo(upperLimit) >= 0);

            if (a.mod(N).equals(BigInteger.ZERO))
                a.add(BigInteger.ONE);

            BigInteger exp = ModularArithmetic.modexp(a, N.subtract(BigInteger.ONE), N);

            if (!exp.equals(BigInteger.ONE))
                return false;

        }
        return true;
    }


    public static BigInteger genPrime(int n) {
        if(n < 2)
            throw new ArithmeticException();
        
        Random rand = new Random();
        
        return new BigInteger(n, 100, rand);
        
    }
}

