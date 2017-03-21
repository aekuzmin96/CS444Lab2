
public class Main {

    private static String xor(String one, String two)
    {
        int a = Integer.parseInt(one, 16);
        int b = Integer.parseInt(two, 16);
        return Integer.toString(a ^ b, 16);
    }

    public static void main(String[] args)
    {

    }
}
