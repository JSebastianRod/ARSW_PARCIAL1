package edu.eci.arsw.math;

public class BaileyBorweinPlouffe extends Thread{
    private int start;
    private int count;
    private byte[] digits;
    private int N;
    
    
    public BaileyBorweinPlouffe(int start, int count) {
        this.start = start;
        this.count = count;

    }


    
    public void run() {
        System.out.println("Thread running " + Thread.currentThread().getName());
        System.out.println((PiDigits.getDigits(start, count, N)));
        
    }

    public byte[] getDigits() {
        return digits;
    }
    
}
