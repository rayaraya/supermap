/*
    Copyright 2017 SUPMUP

    This file is part of Supermap.

    Supermap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Supermap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Supermap. If not, see <http://www.gnu.org/licenses/>.
*/

package top.supcar.server.parse;

import java.net.*;
import java.io.*;

/**
 * Class for establishing access to distant server resources and communication <br>
 * It sends a request to server and gets file with OSM data being written in another file <br>
 * @author rayaraya
 */

public class URLReader {
//OTHER METHODS

    /**
     * Creates file with OSM data <br>
     * @param surl is the URL object <br>
     * @param filepath is the path to the file being created <br>
     * @throws Exception in case of API faults <br>
     */
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