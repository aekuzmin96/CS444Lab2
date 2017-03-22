import java.io.*;

public class Main {
    /*
     4b4d3239764863686245437379465942
     adbeb300136c6305bf21eb69dc71e7c0
     0b9c6f19dd789479e884d29da45c0bea
     c49d4a5701c2a903e9ce0d59228ea4d0
     f003a065a9ac9e04f3330ef903be54dc
     d7307dbc20d96bc3a3e3b45c6af1447f
     fe86cdeba431347e6f27a88015a69dc0
     9565754cdc87df46697fbd98184a07e5
     b807a08ac6f3561650c6df665c4a7758
     746fe7b340a22a6d01d94fa8b95d983d
     210ea4e367b5f961cc93407c9b0598a6

     python client.py -ip shasta.cs.unm.edu -p 10035 -b 'IV+CT' -id 35
     */

    private static String path = "python /nfs/student/a/akuzmin/IdeaProjects/CS444Lab2/src/client.py ";
    private static String arg1 = "-ip shasta.cs.unm.edu -p 10035 -b '";
    private static String arg2 = "' -id 35";
    private static String IV = "4b4d3239764863686245437379465942";
    private static String ciphertext = "adbeb300136c6305bf21eb69dc71e7c0";
    private static String[] rValue = new String[16];
    private static String[] hex = new String[256];
    private static int[] offset;

    private static void initializeProgram()
    {
        buildHexValues();
        offset = new int[] {16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    }

    private static void buildHexValues()
    {
        for(int i = 0; i < 256; i++)
        {
            if(i < 16)
            {
                hex[i] = "0" + Integer.toHexString(i);
            }
            else
            {
                hex[i] = Integer.toHexString(i);
            }
        }
    }

    private static String xor(String one, String two)
    {
        int a = Integer.parseInt(one, 16);
        int b = Integer.parseInt(two, 16);
        return Integer.toString(a ^ b, 16);
    }

    private static void runPython()
    {
        String message = path + arg1 + IV + ciphertext + arg2;
        System.out.println(message);
        System.out.println(IV.length() + ciphertext.length());
        String s;

        try
        {
            Process p = Runtime.getRuntime().exec(message);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Error:");
            while ((s = stdError.readLine()) != null)
            {
                System.out.println(s);
            }
        }
        catch (Exception e) {}
    }

    public static void main(String[] args) {
        initializeProgram();
        runPython();
    }
}
