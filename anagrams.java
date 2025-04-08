import java.util.stream.Collectors;
import java.util.stream.Stream;

public class anagrams {
    public static void main(String[] args)
    {
        String s1 = "madam";
        String s2 = "MadaM";

        s1 = Stream.of(s1.split("")).map(String::toUpperCase).sorted().collect(Collectors.joining());

        s2 = Stream.of(s2.split("")).map(String::toUpperCase).sorted().collect(Collectors.joining());

        if (s1.equals(s2))
        {
            System.out.println("Two strings are anagrams");
        }
        else
        {
            System.out.println("Two strings are not anagrams");
        }
    }
}
