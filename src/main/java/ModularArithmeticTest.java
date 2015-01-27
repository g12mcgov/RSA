import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class ModularArithmeticTest {


    @Test
    public void testExtendedEuclid() throws Exception {
        BigInteger a, b;
        BigInteger[] out;
        a = new BigInteger("7");
        b = new BigInteger("3");
        out = ModularArithmetic.extendedEuclid(a, b);
        boolean isTestPassed = (out[0].compareTo(BigInteger.ONE) == 0 &&
                out[1].compareTo(new BigInteger("-2")) == 0 &&
                out[2].compareTo(BigInteger.ONE) == 0);
        assertEquals(true, isTestPassed);
    }

    @Test
    public void testExtendedEuclid_large() throws Exception {
        BigInteger a, b;
        BigInteger[] out;
        a = new BigInteger("952545454");
        b = new BigInteger("11");
        out = ModularArithmetic.extendedEuclid(a, b);
        boolean isTestPassed = (out[0].compareTo(new BigInteger("4")) == 0 &&
                out[1].compareTo(new BigInteger("-346380165")) == 0 &&
                out[2].compareTo(BigInteger.ONE) == 0);
        assertEquals(true, isTestPassed);
    }

    @Test
    public void testModadd() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("12");
        b = new BigInteger("8");
        N = new BigInteger("15");
        c = ModularArithmetic.modadd(a, b, N);
        assertEquals(new BigInteger("5"), c);
    }

    @Test
    public void testModadd_large() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("952545454");
        b = new BigInteger("834352542");
        N = new BigInteger("5643436545");
        c = ModularArithmetic.modadd(a, b, N);
        assertEquals(new BigInteger("1786897996"), c);
    }

    @Test
    public void testModmult() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("12");
        b = new BigInteger("8");
        N = new BigInteger("15");
        c = ModularArithmetic.modmult(a, b, N);
        assertEquals(new BigInteger("6"), c);
    }

    @Test
    public void testModmult_large() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("952545454");
        b = new BigInteger("834352542");
        N = new BigInteger("5643436545");
        c = ModularArithmetic.modmult(a, b, N);
        assertEquals(new BigInteger("2731065003"), c);
    }

    @Test
    public void testModdiv() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("12");
        b = new BigInteger("8");
        N = new BigInteger("15");
        c = ModularArithmetic.moddiv(a, b, N);
        assertEquals(new BigInteger("9"), c);
    }

    @Test
    public void testModdiv_large() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("952545454");
        b = new BigInteger("834352543");
        N = new BigInteger("5643436545");
        c = ModularArithmetic.moddiv(a, b, N);
        assertEquals(new BigInteger("1745297233"), c);
    }

    @Test
    public void testModexp() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("12");
        b = new BigInteger("8");
        N = new BigInteger("15");
        c = ModularArithmetic.modexp(b, a, N);
        assertEquals(new BigInteger("1"), c);
    }

    @Test
    public void testModexp_large() throws Exception {
        BigInteger a, b, c, N;
        a = new BigInteger("952545454");
        b = new BigInteger("834352542");
        N = new BigInteger("5643436545");
        c = ModularArithmetic.modexp(a, b, N);
        assertEquals(new BigInteger("4626426241"), c);
    }

    @Test
    public void testIsPrime() throws Exception {
        BigInteger N;
        N = new BigInteger("23");
        int k = 4;
        boolean isTestPassed = ModularArithmetic.isPrime(N, k);
        N = new BigInteger("25");
        isTestPassed = isTestPassed && !ModularArithmetic.isPrime(N, k);
        N = new BigInteger("97");
        isTestPassed = isTestPassed && ModularArithmetic.isPrime(N, k);
        assertEquals(true, isTestPassed);
    }

    @Test
    public void testIsPrime_large() throws Exception {
        BigInteger N;
        N = new BigInteger("285044579391958034764329004428399141293");
        int k = 128;
        boolean isTestPassed = ModularArithmetic.isPrime(N, k);
        assertEquals(true, isTestPassed);
    }

    @Test
    public void testGenPrime() throws Exception {
        int n = 7;
        BigInteger N;
        BigInteger[] primes = new BigInteger[13];
        primes[0] = new BigInteger("67"); primes[1] = new BigInteger("71");
        primes[2] = new BigInteger("73"); primes[3] = new BigInteger("79");
        primes[4] = new BigInteger("83"); primes[5] = new BigInteger("89");
        primes[6] = new BigInteger("97"); primes[7] = new BigInteger("101");
        primes[8] = new BigInteger("103"); primes[9] = new BigInteger("107");
        primes[10] = new BigInteger("109"); primes[11] = new BigInteger("113");
        primes[12] = new BigInteger("127");
        N = ModularArithmetic.genPrime(n);
        boolean isTestPassed = false;
        for (int i = 0 ; i < 13; i++) {
            isTestPassed = isTestPassed || N.compareTo(primes[i]) == 0;
        }
        assertEquals(true, isTestPassed);
    }
}
