import java.io.*;

/**
 * Anton Kuzmin
 * CBC Oracle Padding decoder lab.
 * Given a block of initialization vector and a cipher text, decode the message and print it out.
 * You have to change the IV and cipher text strings manually.
 */
public class Main {
    /* Entire cipher text from pcap file:
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
     */
    private static String path = "python /nfs/student/a/akuzmin/IdeaProjects/CS444Lab2/src/client.py ";
    private static String arg1 = "-ip shasta.cs.unm.edu -p 10035 -b ";
    private static String arg2 = " -id 35";
    private static String IV = "746fe7b340a22a6d01d94fa8b95d983d";
    private static String ciphertext = "210ea4e367b5f961cc93407c9b0598a6";
    private static String[] disassembledIV;
    private static String[] rValue = new String[16];
    private static String[] hex = new String[256];
    private static String[] changedIVarray;
    private static int rIndex = 15;
    private static int counter = 1;

    /**
     * Disassemble the string into an array of 2 characters at each index
     */
    private static String[] disassembleString(String s)
    {
        return s.split("(?<=\\G.{2})");
    }

    /**
     * Assemble the array of 2 character strings into 1 string
     */
    private static String assembleString(String[] s)
    {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < s.length; i++) {

            if(s[i].length() == 1)
            {
                strBuilder.append("0");
            }

            strBuilder.append(s[i]);
        }
        return strBuilder.toString();
    }

    /**
     * Disassemble the initial IV into an array and make an array of hex values from 0 to 255
     */
    private static void initialize()
    {
        disassembledIV = disassembleString(IV);
        changedIVarray = disassembleString(IV);

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

    /**
     * XOR two hex strings
     */
    private static String xor(String one, String two)
    {
        int a = Integer.parseInt(one, 16);
        int b = Integer.parseInt(two, 16);
        return Integer.toString(a ^ b, 16);
    }

    /**
     * Connect to the server and send the modified IV
     */
    private static boolean runPython(String newIV)
    {
        String message = path + arg1 + newIV + ciphertext + arg2;
        String s;

        try
        {
            Process p = Runtime.getRuntime().exec(message);
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdError.readLine()) != null)
            {
                if(getRValue(s))
                    return true;
            }
        }
        catch (Exception e) {}
        return false;
    }

    /**
     * Look through the server output for valid padding and grab the byte with that padding
     */
    private static boolean getRValue(String s)
    {
        int len = s.length();

        for(int i = 0; i < len; i++)
        {
            if(s.charAt(i) == 'M' && changedIVarray[rIndex] != disassembledIV[rIndex])
            {
                System.out.println(" - found " + changedIVarray[rIndex]);
                //debug(changedIVarray);
                rValue[rIndex] = xor(changedIVarray[rIndex], Integer.toHexString(counter));
                rIndex--;
                //debug(rValue);
                return true;
            }
        }
        return false;
    }

    /**
     * Loop through all of the hex values for valid padding and send the modified IV to the server
     */
    private static void searchRValues(int current)
    {
        System.out.print("Searching for byte: " + current);
        for(int i = 0; i < 256; i++)
        {
            changedIVarray[current] = hex[i];
            if(runPython(assembleString(changedIVarray)))
                break;
        }
    }

    /**
     * Run the decoding for all 16 bytes and update the previous byte values accordingly
     */
    private static void runDecoder()
    {
        for(int i = 15; i >= 0; i--)
        {
            for(int j = 15; j > (16 - counter); j--)
            {
                changedIVarray[j] = xor(rValue[j], Integer.toHexString(counter));
            }
            searchRValues(i);
            counter++;
            //debug(changedIVarray);
        }
    }

    /**
     * Used for debugging purposes
     * Print out the array of strings
     */
    private static void debug(String[] str)
    {
        for(int i = 0; i < str.length; i++)
        {
            System.out.print(str[i] + " ");
        }
        System.out.println();
    }

    /**
     * Convert the hex byte string to ascii characters
     */
    private static String hexToAscii(String hexStr)
    {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2)
        {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    /**
     * Print the plain text output
     */
    private static void printOutput()
    {
        String[] str = new String[16];
        for(int i = 0; i < 16; i++)
        {
            str[i] = (xor(rValue[i], disassembledIV[i]));
        }

        System.out.println(hexToAscii(assembleString(str)));
    }

    public static void main(String[] args) {
        initialize();
        runDecoder();
        printOutput();
    }
}