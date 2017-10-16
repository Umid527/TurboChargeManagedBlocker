package org.learn.blocker;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

/**
 * @author umahmudov on 09-Oct-17.
 * @project ManagedBlocked
 */

//    demo1: test100_000_000() time = 42332
//    demo2: test100_000_000() time = 20991
//    demo3: test100_000_000() time = 12555
//    demo4: test100_000_000() time = 9973
public class Fibonacci {
    public BigInteger f(int n) {
        Map<Integer, BigInteger> cache = new ConcurrentHashMap<>();
        cache.put(0, BigInteger.ZERO);
        cache.put(1, BigInteger.ONE);
        return f(n, cache);
    }

    public BigInteger f(int n, Map<Integer, BigInteger> cache) {
        BigInteger result = cache.get(n);
        if (result == null) {


            int half = (n + 1) / 2;

            RecursiveTask<BigInteger> f0_task = new RecursiveTask<BigInteger>() {
                @Override
                protected BigInteger compute() {
                    return f(half - 1, cache);
                }
            };
            f0_task.fork();

            BigInteger f1 = f(half, cache);
            BigInteger f0 = f0_task.join();

            long time = n > 10_000 ? System.currentTimeMillis() : 0;
            try {

                if (n % 2 == 1) {
                    result = f0.multiply(f0).add(f1.multiply(f1));
                } else {
                    result = f0.shiftLeft(1).add(f1).multiply(f1);
                }
                cache.put(n, result);
            } finally {
                time = n > 10_000 ? System.currentTimeMillis() - time : 0;
                if (time > 50) System.out.printf("f(%d) took %d%n", n, time);
            }
        }
        return result;
//        return f(n - 1).add(f(n - 2));
    }
}