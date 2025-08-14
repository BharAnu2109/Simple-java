import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class Concatenate {
    public static void main(String[] args) {

        List<String> list1 = Arrays.asList("Java", "8");
        List<String> list2 = Arrays.asList("explained", "through", "programs");
        List<String> list3 = Arrays.asList("Java","is","complex");
        List<String> list4 = Arrays.asList("Java","is","complex","but","love");

        Stream<String> concatStream = concat(list1.stream(),concat (list2.stream(),list3.stream()));

        // Concatenated the list1 and list2 by converting them into Stream

        concatStream.forEach(str -> System.out.print(str + " "));

        // Printed the Concatenated Stream

    }
}

