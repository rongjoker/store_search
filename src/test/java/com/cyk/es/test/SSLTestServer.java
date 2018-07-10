package com.cyk.es.test;/**
 * Created by zhangshipeng on 2/26/2018.
 */

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

/**
 * ssl-server
 *
 * @author Administrator
 * @ClassName: SSLTestServer
 * @date 2018-02-26 11:05 AM
 **/
public class SSLTestServer extends Thread{

    private static String SERVER_KEY_STORE = "d:/data/cert";
    private static String SERVER_KEY_STORE_PASSWORD = "110119120";

    private Socket socket;

    public SSLTestServer(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String data = reader.readLine();
            writer.println(data);
            writer.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", SERVER_KEY_STORE);
        SSLContext context = SSLContext.getInstance("TLS");

        KeyStore ks = KeyStore.getInstance("jceks");
        ks.load(new FileInputStream(SERVER_KEY_STORE), null);
        KeyManagerFactory kf = KeyManagerFactory.getInstance("SunX509");
        kf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());

        context.init(kf.getKeyManagers(), null, null);

        ServerSocketFactory factory = context.getServerSocketFactory();
        ServerSocket _socket = factory.createServerSocket(8443);
        ((SSLServerSocket) _socket).setNeedClientAuth(false);

        while (true) {
            System.out.println("-------------2333 ssl server is starting---------------");
            new SSLTestServer(_socket.accept()).start();
        }
    }



}
