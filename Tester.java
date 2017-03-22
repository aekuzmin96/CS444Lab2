/**
 * Created by akuzmin on 3/22/17.
 */
public class Tester {
    private static int[] b = new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

    private static void foo()
    {
        int counter = 1;
        for (int i = 15; i >= 0; i--)
        {
            for (int j = 15; j > (16 - counter); j--)
            {
                System.out.println(b[j] + " " + b[j] + " " + counter);
            }
            counter++;
            System.out.println("-");
        }
    }

    public static void main(String[] args)
    {
        foo();
    }
}
