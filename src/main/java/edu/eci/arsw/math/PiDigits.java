package edu.eci.arsw.math;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private static ArrayList<BaileyBorweinPlouffe> threads = new ArrayList<BaileyBorweinPlouffe>();
    private static boolean stop = false;
    private static Object lock = new Object();

    /**
     * Returns a range of hexadecimal digits of pi.
     * 
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */

    public static void main(String[] args) {

        System.out.println(getDigits(1, 50000, 5));
        System.out.println(getDigits(1, 50, 0));

    }

    public static byte[] getDigits(int start, int count, int N) {
        final int[] contadorThreads = { 0 };

        if (N != 0) {
            int work4Thread = count / N;
            int add = count % N;
            int starti = start;
            for (int i = 0; i < N; i++) {
                if (i == N - 1) {
                    BaileyBorweinPlouffe thread = new BaileyBorweinPlouffe(starti, work4Thread);
                    threads.add(thread);
                    thread.start();
                    starti = starti + work4Thread + add;

                } else {
                    BaileyBorweinPlouffe thread = new BaileyBorweinPlouffe(starti, work4Thread);
                    threads.add(thread);
                    thread.start();
                    starti = starti + work4Thread;
                }
            }
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (BaileyBorweinPlouffe thread : threads) {
                        thread.stopThread();
                    }
                    for (BaileyBorweinPlouffe thread : threads) {
                        System.out.println("El hilo: " + " Encontro los siguientes numeros: " + contadorThreads[0]);
                    }
                    System.out.println("Presione enter para continuar. ");
                    String read;
                    Scanner scanner = new Scanner(System.in);
                    read = scanner.nextLine();
                    if (read != null) {
                        scanner.close();
                        System.out.println("Continuando Busqueda...");
                        synchronized (threads) {
                            threads.notifyAll();
                        }
                    }
                }
            }, 1);

            byte[] digits = new byte[count];
            int countd = 0;

            for (BaileyBorweinPlouffe thread : threads) {
                try {
                    thread.join();
                    for (byte digit : thread.getDigits()) {
                        digits[countd] = digit;
                        countd++;
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return digits;

        } else {

            if (start < 0) {
                throw new RuntimeException("Invalid Interval");
            }

            if (count < 0) {
                throw new RuntimeException("Invalid Interval");
            }

            byte[] digits = new byte[count];
            double sum = 0;

            for (int i = 0; i < count; i++) {
                if (i % DigitsPerSum == 0) {
                    sum = 4 * sum(1, start)
                            - 2 * sum(4, start)
                            - sum(5, start)
                            - sum(6, start);

                    start += DigitsPerSum;
                }

                sum = 16 * (sum - Math.floor(sum));
                digits[i] = (byte) sum;
                contadorThreads[0]++;
            }

            return digits;
        }

    }

    public static void resumeThread() {
        stop = false;
        synchronized (threads) {
            threads.notifyAll();
        }
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

}
