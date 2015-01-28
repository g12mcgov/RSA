/*
 * Author: Grant McGovern
 * Date: 1/13/15
 *  
 * Description: This class implements several Modular Arithmetic methods to be used by the 
 *              RSA class.
 * 
 */


import java.math.BigInteger;
import java.util.Random;

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
     * 
     * NOTE: Original definition of this method was BigInteger c = a.modPow(b, N);
     *       However, due to Dr. Pauca's project restrictions, we were forced to
     *       implement our own.
     *       
     *       The below implementation is a mimick of the Modular Exponentiation
     *       algorithm, as discussed on Wikipedia.
     *       
     *       ~ Link: http://en.wikipedia.org/wiki/Modular_exponentiation
     *
     * ATTENTION: Me and Gaurav Sheni worked together on figuring out this method (mainly 
     *            translating it from Wikipedia to suit our needs.
     *
     */
    public static BigInteger modexp(BigInteger a, BigInteger b, BigInteger N) {
        BigInteger c = BigInteger.ONE; 
        a = a.mod(N);

        for (int i = 0; i < b.bitLength(); i++) {
            /* Checks to see if designated bit of this BigInteger (b) is set. */
            if (b.testBit(i))
                c = c.multiply(a).mod(N);
            
            a = a.multiply(a).mod(N);
        }
            
        return c;
    }

    /* modDiv()
     *
     * ~ Divides two BigInteger values and Mods by N 
     */
    public static BigInteger moddiv(BigInteger a, BigInteger b, BigInteger N) {
        /* Retrieves a packet containing the GCD, and the two coefficients (x, y) such that the following equation is satisfied */
        BigInteger[] gcd = extendedEuclid(N, b);
        
        /* gcd[1] is equal to the y-value, otherwise known as the inverse */
        BigInteger c = a.multiply(gcd[1].mod(N)).mod(N);

        return c;
    }

    /* extendedEuclid()
     *
     * ~ Multiplies two BigInteger values and Mods by N
     */
    public static BigInteger[] extendedEuclid(BigInteger a, BigInteger b) {
        BigInteger[] container;
        
        /*
         * container[0] = x
         * container[1] = y
         * container[2] = d  <-- This is the GCD 
         * ax + by = d = gcd(a, b)
         */
         container = new BigInteger[3];
        
        /* Following the algorithm defined in the textbook... */
        if (b.compareTo(BigInteger.ZERO) == 0) {
            container[0] = BigInteger.ONE;
            container[1] = BigInteger.ZERO;
            container[2] = a;
        }
        else {
            /* Recursively call extendedEuclid(), as described in the book */
            BigInteger[] packet = extendedEuclid(b, a.mod(b));
            container[0] = packet[1];
            container[1] = packet[0].subtract(packet[1].multiply(a.divide(b)));
            container[2] = packet[2];
        }

        return container;
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

            BigInteger c;
            
            /* 
             * Constantly generates a random Integer of N-1 bit length, until it finds one that suits the spec
             */
            do {
                c = new BigInteger(upperLimit.bitLength(), rand);
            }
            while (c.compareTo(upperLimit) >= 0);
            
            /* If for whatever reason our range begins at 0, add 1 to it */
            if (c.mod(N).equals(BigInteger.ZERO)) c.add(BigInteger.ONE);

            BigInteger exp = ModularArithmetic.modexp(c, N.subtract(BigInteger.ONE), N);

            if (!exp.equals(BigInteger.ONE)) return false;

        }
        
        return true;
    }

    /* genPrime()
     *
     * ~ Generates a prime number, based on the number of bits passed as input
     */
    public static BigInteger genPrime(int n) {
        if(n < 2)
            throw new ArithmeticException();
        
        Random rand = new Random();
        
        /* Constructs a BigInteger with the specified bits, where the second argument (100) is the certainty value */
        return new BigInteger(n, 100, rand);
        
    }
}

