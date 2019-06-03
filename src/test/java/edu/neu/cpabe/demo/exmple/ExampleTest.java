package edu.neu.cpabe.demo.exmple;

import co.junwei.cpabe.Cpabe;
import org.junit.Test;

public class ExampleTest {


    final static boolean DEBUG = true;

    static String dir = "demo/cpabe";

    static String pubfile = dir + "/pub_key";
    static String mskfile = dir + "/master_key";
    static String prvfile;

    static String inputfile = dir + "/input.pdf";
    static String encfile = dir + "/input.pdf.cpabe";
    static String decfile;


    static String policy = "math:good physics:good chemical:medium biology:bad 2of2 1of3 character:teacher 1of2";


    //static String student1_attr = "chinese:bad math:bad english:medium "
    //        + "physics:bad chemical:medium biology:bad";
    static String student1_attr = "character:teacher";

    static String student2_attr = "chinese:good math:bad english:medium "
            + "physics:bad chemical:good biology:good";


    @Test
    public void Test1_setup_enc() throws Exception {

        Cpabe test = new Cpabe();
        println("//start to setup");
        test.setup(pubfile, mskfile);
        println("//end to setup");

        println("//start to enc");
        test.enc(pubfile, policy, inputfile, encfile);
        println("//end to enc");

    }

    @Test
    public void Test2_keygen_dec_succeed() throws Exception {
        String attr_str;

        attr_str = student1_attr;
        prvfile = dir + "/prv_key1";

        Cpabe test = new Cpabe();

        println("//start to keygen");
        test.keygen(pubfile, prvfile, mskfile, attr_str);
        println("//end to keygen");

        decfile = dir + "/output1.pdf";

        println("//start to dec");
        test.dec(pubfile, prvfile, encfile, decfile);
        println("//end to dec");
    }

    @Test
    public void Test3_keygen_dec_fail() throws Exception {
        String attr_str;

        attr_str = student2_attr;
        prvfile = dir + "/prv_key2";

        Cpabe test = new Cpabe();

        println("//start to keygen");
        test.keygen(pubfile, prvfile, mskfile, attr_str);
        println("//end to keygen");

        decfile = dir + "/output2.pdf";

        println("//start to dec");
        try{
            test.dec(pubfile, prvfile, encfile, decfile);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        println("//end to dec");
    }

    /* connect element of array with blank */
    public static String array2Str(String[] arr) {
        int len = arr.length;
        String str = arr[0];

        for (int i = 1; i < len; i++) {
            str += " ";
            str += arr[i];
        }

        return str;
    }

    private static void println(Object o) {
        if (DEBUG)
            System.out.println(o);
    }
}
