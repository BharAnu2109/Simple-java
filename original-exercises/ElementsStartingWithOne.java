import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class ElementsStartingWithOne {
    public static List<String> findElementsStartingWithOne(int[] numbers) {
        return Arrays.stream(numbers)
                .boxed()
                .map(String::valueOf)
                .filter(str -> str.startsWith("1"))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        int[] input = {1, 2, 11, 21, 31, 42, 56, 23, 16,111,156,123,189,167};
        List<String> result = findElementsStartingWithOne(input);
        System.out.println(result);
    }
}
