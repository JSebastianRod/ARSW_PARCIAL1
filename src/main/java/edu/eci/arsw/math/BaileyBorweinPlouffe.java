package edu.eci.arsw.math;

public class BaileyBorweinPlouffe extends Thread{
    private int start;
    private int count;
    private byte[] digits;
    
    
    public BaileyBorweinPlouffe(int start, int count) {
        this.start = start;
        this.count = count;

    }


    
    public void run() {
        System.out.println("Thread running " + Thread.currentThread().getName());
        digits = (PiDigits.getDigits(start, count, 0));
        
    }

    public byte[] getDigits() {
        return digits;
    }
    
}
