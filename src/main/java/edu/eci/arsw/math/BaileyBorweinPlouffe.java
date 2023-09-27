package edu.eci.arsw.math;

public class BaileyBorweinPlouffe extends Thread{
    private int start;
    private int count;
    private byte[] digits;
    private static Object lock = new Object();
    
    
    public BaileyBorweinPlouffe(int start, int count) {
        this.start = start;
        this.count = count;

    }

    public void run() {
        digits = (PiDigits.getDigits(start, count, 0));
        
    }

    public byte[] getDigits() {
        return digits;
    }
    
    public static void stopThread() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
}
