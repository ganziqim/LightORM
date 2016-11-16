package Main;

/**
 * Created by admin on 2016/11/7.
 */
public class Test {
    static int[] bits;

    public static void main(String[] args) {
        bitString(3);
    }

    public static void bitString(int n) {
        bits = new int[n];
        _bitString(n);
    }

    public static void _bitString(int n) {
        if(n == 0)
            display(bits);
        else{
            bits[n - 1] = 0;
            _bitString(n - 1);
            bits[n - 1] = 1;
            _bitString(n - 1);
        }

    }

    public static void display(int[] array){
      /*
        for(int a : array)
            System.out.print(a + " ");
        System.out.println();
       */
      for(int i = array.length - 1; i >= 0; i--)
          System.out.print(array[i] + " ");
        System.out.println();
    }
}
