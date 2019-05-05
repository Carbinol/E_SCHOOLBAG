package edu.neu.cpabe.demo.key;


import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/key")
public class KeyManageController {

    private static Base64.Encoder encoder = Base64.getEncoder();

    @GetMapping
    public ResponseEntity<?> getKey() throws IOException {
        byte[] pub_byte = IOUtils.toByteArray(getClass().getResourceAsStream("/keys/pub_key"));
        byte[] master_byte = IOUtils.toByteArray(getClass().getResourceAsStream("/keys/master_key"));
        Map<String, String> keys = new HashMap<>();
        keys.put("pubKey", encoder.encodeToString(pub_byte));
        keys.put("masterKey", encoder.encodeToString(master_byte));
        return ResponseEntity.ok(keys);
    }

}
