package edu.neu.cpabe.demo.encrypt;

import co.junwei.bswabe.*;
import co.junwei.cpabe.AESCoder;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class CpabeEncryptUtilImpl implements EncryptUtil {

    private static Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public String encrypt(String input,String policy) {
        try {
            byte[] pub_byte = IOUtils.toByteArray(getClass().getResourceAsStream("/keys/pub_key"));
            BswabePub pub = SerializeUtils.unserializeBswabePub(pub_byte);
            BswabeCphKey keyCph = Bswabe.enc(pub, policy);
            BswabeCph cph = keyCph.cph;
            Element m = keyCph.key;
            System.err.println("m = " + m.toString());
            if (cph == null) {
                System.out.println("Error happed in enc");
                System.exit(0);
            }

            byte[] cphBuf = SerializeUtils.bswabeCphSerialize(cph);
            byte[] plt = input.getBytes();
            byte[] aesBuf = AESCoder.encrypt(m.toBytes(), plt);
            return encoder.encodeToString(writeCpabeFile(cphBuf, aesBuf));
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] writeCpabeFile( byte[] cphBuf, byte[] aesBuf) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);

        int i;
        for(i = 3; i >= 0; --i) {
            os.write((aesBuf.length & 255 << 8 * i) >> 8 * i);
        }

        os.write(aesBuf);

        for(i = 3; i >= 0; --i) {
            os.write((cphBuf.length & 255 << 8 * i) >> 8 * i);
        }

        os.write(cphBuf);
        os.close();
        return os.toByteArray();
    }
}
