import java.util.Arrays;
import java.util.stream.IntStream;
public class ReverseanInteger {
    public static void main(String[] args)
    {
        int[] array = new int[] {8,15,20,5, 1,10,67,45,54, 7, 3, 9, 6,23,90,89,78};

        int[] reversedArray = IntStream.rangeClosed(1, array.length).map(i -> array[array.length - i]).toArray();

        System.out.println(Arrays.toString(reversedArray));
    }
}

