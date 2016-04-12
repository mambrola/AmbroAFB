/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tabramishvili
 */
public class KFZClient {

    private String serverAddress = "http://localhost:8080/KFZ_Server/api";

    private final Credentials credentials;
    private String token;

    public KFZClient(String username, String password) throws IOException, KFZServerException {
        this(new Credentials(username, password));
    }

    public KFZClient(Credentials credentials) throws IOException, KFZServerException {
        this.credentials = credentials;
        login();
    }

    public String get(String resource) throws IOException, KFZServerException {
        return call(resource, "GET", null);
    }

    public String post(String resource, String data) throws IOException, KFZServerException {
        return call(resource, "POST", data);
    }

    public final void login() throws IOException, KFZServerException {
        String cred = "{'username':'" + credentials.username + "', 'password':'" + credentials.password + "'}";
        token = post("/authentication", cred);
    }

    public final void logout() {
        try {
            post("/authentication/logout", null);
        } catch (IOException | KFZServerException ex) {
            Logger.getLogger(KFZClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String call(String resource, String method, String data) throws IOException, KFZServerException {
        HttpURLConnection con = createConnection(resource);
        con.setRequestMethod(method);
        if (data != null) {
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes("UTF-8"));
        }

        int status = con.getResponseCode();
        if (status == 200) {
            String response = readStream(con.getInputStream());
            con.getInputStream().close();
            con.disconnect();
            return response;
        }
        String error = readStream(con.getErrorStream());
        con.getErrorStream().close();
        con.getInputStream().close();
        con.disconnect();
        throw new KFZServerException(error, status);
    }

    public HttpURLConnection createConnection(String resource) throws IOException {
        URL url = createUrl(resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + token);
        return con;
    }

    public final URL createUrl(String resource) {
        if (resource != null && !resource.startsWith("/")) {
            resource = "/" + resource;
        }
        String url_str = serverAddress + (resource == null ? "" : resource);
        try {
            return new URL(url_str);
        } catch (MalformedURLException ex) {
            Logger.getLogger(KFZClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String readStream(InputStream stream) throws IOException {
        return readStream(stream, "UTF-8");
    }

    public static String readStream(InputStream stream, String encoding) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, encoding))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static class Credentials {

        public final String username;
        public final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class KFZServerException extends Exception {

        private final int status;

        public KFZServerException(String message, int status) {
            super(message);
            this.status = status;
        }

        public int getStatusCode() {
            return status;
        }
    }

}
