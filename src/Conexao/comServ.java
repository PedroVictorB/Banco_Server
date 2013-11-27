/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class comServ extends Thread {

    private ServerSocket server;
    private Socket socket;
    private InputStream entrada;
    private BufferedReader read;
    private String nome;
    private List clientes = new ArrayList();
    private static List teste = new ArrayList();

    public comServ(Socket socket) {
        this.socket = socket;
    }

    public static void main(String args[]) {

        try {
            ServerSocket s = new ServerSocket(2222);
            while (true) {
                System.out.println("Esperando alguem se conectar...");
                try {
                    Socket conexao = s.accept();
                    teste.add(s.getInetAddress().getHostName());
                    PrintStream ps = new PrintStream(conexao.getOutputStream());
                    System.out.println(" Conectou!");
                    Thread t = new comServ(conexao);
                    t.start();
                } catch (IOException e) {
                    System.out.println("IOException: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream saida = new PrintStream(socket.getOutputStream());
            nome = entrada.readLine();
            if (nome == null) {
                return;
            }
            clientes.add(saida);
            String linha = entrada.readLine();
            while (linha != null && !(linha.trim().equals(""))) {
                linha = entrada.readLine();
                System.out.println(linha);
            }
            clientes.remove(saida);
            socket.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
