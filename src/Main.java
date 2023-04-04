import java.util.*;

public class Main {
    private static final Map<String, Integer> ROMANIAN = new HashMap<>(Map.of(
            "C", 100, "XC", 90, "L", 50,
            "XL", 40, "X", 10, "IX", 9,
            "V", 5, "IV", 4, "I", 1));
    private static final Set<String> ACTIONS = new HashSet<>(Set.of("/", "+", "-", "*"));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputData = scanner.nextLine();
        try {
            System.out.println(calc(inputData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String calc(String input) throws Exception {
        List<String> parsedData = stringParser(input);
        return makeCount(parsedData);
    }

    private static String makeCount(List<String> parsedData) throws Exception {
        int num1, num2;
        boolean roman = false;
        if ("r".equals(parsedData.get(0))) {
            roman = true;
            num1 = fromRomToArab(parsedData.get(2));
            num2 = fromRomToArab(parsedData.get(3));
        } else {
            num1 = Integer.parseInt(parsedData.get(2));
            num2 = Integer.parseInt(parsedData.get(3));
        }
        int result = switch (parsedData.get(1)) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            default -> 0;
        };
        return roman ? fromArabToRom(result) : String.valueOf(result);
    }

    private static String fromArabToRom(int num) throws Exception {
        if (num <= 0) {
            throw new Exception("Roman numerals cannot be negative or zero");
        }
        StringBuilder result = new StringBuilder();
        List<String> strings = ROMANIAN.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey).toList();
        for (String value : strings) {
            while (num >= ROMANIAN.get(value)) {
                result.append(value);
                num -= ROMANIAN.get(value);
            }
        }
        return result.toString();
    }

    private static int fromRomToArab(String num) {
        if (ROMANIAN.containsKey(num)) {
            return ROMANIAN.get(num);
        }
        int result = 0;
        for (char ch : num.toCharArray()) {
            result += ROMANIAN.get(String.valueOf(ch));
        }
        return result;
    }


    private static List<String> stringParser(String input) throws Exception {
        String[] split = input.split("\\s");
        if (split.length != 3) {
            throw new Exception("Input data is not correct!");
        }
        boolean arabic = isArabic(split[0]) && isArabic(split[2]) && ACTIONS.contains(split[1]);
        boolean romanian = isRomanian(split[0]) && isRomanian(split[2]) && ACTIONS.contains(split[1]);
        if (!arabic && !romanian) {
            throw new Exception("Input data is not correct!");
        }
        return new ArrayList<>(List.of(arabic ? "a" : "r", split[1], split[0], split[2]));
    }

    private static boolean isRomanian(String num) {
        return num.matches("I{1,3}|VI{0,3}|I?[VX]");
    }

    private static boolean isArabic(String num) {
        return num.matches("-?\\d0|-?\\d");
    }
}
