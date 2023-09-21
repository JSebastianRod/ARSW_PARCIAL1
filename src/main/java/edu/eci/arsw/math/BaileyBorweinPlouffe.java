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
        System.out.println(bytesToHex(PiDigits.getDigits(start, count)));
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }
    
    public byte[] getDigits() {
        return digits;
    }
    
}
