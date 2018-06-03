package com.kainos.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetRequest {

    private URL connectionURL;

    public GetRequest(String path) throws IOException {
        this.connectionURL = new URL(path);
    }

    public void changePath(String path) throws IOException {
        this.connectionURL = new URL(path);
    }

    public String getNextDataset() throws IOException {
        URLConnection connection = connectionURL.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        return result.toString();
    }

}
