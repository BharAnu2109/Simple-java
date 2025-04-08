import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class findCountOfChars {
    public static void main(String args[]) {
        String s = "string data to count each character";

        Map<String, Long> mapObject = Arrays.stream(s.split(""))
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Printing the map to see the results
        mapObject.forEach((character, count) ->
                System.out.println(character + ": " + count));
    }
}
