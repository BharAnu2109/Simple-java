import java.util.Arrays;
import java.util.Comparator;
public class SecondHighestNumber {
    public static Integer findSecondHighest(int[] numbers) {
        return Arrays.stream(numbers)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .orElse(null);
    }

    public static void main(String[] args) {
        int[] input = {5, 9, 2, 13, 42, 56, 24, 36,52};
        Integer secondHighest = findSecondHighest(input);
        System.out.println(secondHighest);
    }
}
