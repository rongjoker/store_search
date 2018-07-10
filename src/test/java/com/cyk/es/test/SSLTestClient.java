package com.cyk.es.test;/**
 * Created by zhangshipeng on 2/26/2018.
 */

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ssl-client
 *
 * @author Administrator
 * @ClassName: SSLTestClient
 * @date 2018-02-26 11:17 AM
 **/
public class SSLTestClient {

    private static String CLIENT_KEY_STORE = "d:/data/client_ks";

    private Socket clientWithoutCert() throws Exception {
        SocketFactory sf = SSLSocketFactory.getDefault();
        Socket s = sf.createSocket("localhost", 8443);
        return s;
    }


    public static void main(String[] args) throws Exception {
        // Set the key store to use for validating the server cert.
        System.setProperty("javax.net.ssl.trustStore", CLIENT_KEY_STORE);

        System.setProperty("javax.net.debug", "ssl,handshake");

        SSLTestClient client = new SSLTestClient();
        Socket s = client.clientWithoutCert();

        PrintWriter writer = new PrintWriter(s.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(s
                .getInputStream()));
        writer.println("Hello World!");
        writer.flush();
        System.out.println(reader.readLine());
        s.close();
    }






}
