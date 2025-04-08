import java.util.*;
import java.util.stream.*;
import java.util.function.Function;

public class SortValues {
    public static void main(String args[]) {
        List<Integer> myList = Arrays.asList(10,15,8,49,25,98,98,32,15);

        myList.stream()
                .sorted()
                .forEach(System.out::println);

        /* Or can also try below way */
        /* When numbers are given as Array int[] arr = {10,15,8,49,25,98,98,32,15}; */

//        Arrays.stream(arr).boxed().sorted().collect(Collectors.toList())
    }
}
