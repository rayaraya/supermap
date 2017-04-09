package top.supcar.server.parse;

import java.net.*;
import java.io.*;

public class URLReader {
    public void readWrite(String surl, String filepath) throws Exception {
        System.out.println(filepath);
        URL url = new URL(surl);
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        FileWriter writer = new FileWriter(filepath, false);
        while ((inputLine = in.readLine()) != null) {
            writer.write(inputLine);
            writer.write("\n");
        }
        writer.flush();
        writer.close();
        in.close();
    }
}

