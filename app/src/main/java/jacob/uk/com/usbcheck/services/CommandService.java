package jacob.uk.com.usbcheck.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandService {
    public String executeCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = br.readLine()) != null) {
            return line;
        }

        return line;
    }
}
