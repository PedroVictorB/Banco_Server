/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import DAO.comandos_SQL;
import Entidades.Clientes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */

public class conServ extends Thread{

    private Socket socket;
    private String[] vetor_de_comando;
    private BufferedReader buffer;
    private InputStream entrada;
    
    public conServ(Socket socket) {
        this.socket = socket;
    }

    public static void main(String args[]) {
        
        try {
            ServerSocket s = new ServerSocket(2222);
            while (true) {
                System.out.println("Esperando alguem se conectar....");
                try {
                    Socket conexao = s.accept();
                    System.out.println(conexao.getInetAddress() + " Conectou!");
                    Thread t = new conServ(conexao);
                    t.start();
                    //new Thread(new conServ(conexao)).start();
                } catch (IOException e) {
                    System.out.println("IOException: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    @Override
    public synchronized void run() {
        
        
        //String Depositar: "1" + " " + conta + " " + valor
        //String Transferir: "2" + " " + conta + " "  + conta para transferir + " " + valor
        //String Sacar: "3" + " " + conta + " "  + valor
        //String Consultar: "4" + "" + conta
        //USAR A ENTIDADE PARA MEXER NO BANCO DE DADOS(OBRIGATÓRIO!).
        
        vetor_de_comando = new String[4];
        comandos_SQL sql = new comandos_SQL();
        Clientes c = new Clientes();
        Clientes ct = new Clientes();
        String msg;
        
        try {
            
            PrintStream saida = new PrintStream(this.socket.getOutputStream());
            
            entrada = socket.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(entrada));

            msg = buffer.readLine();
            vetor_de_comando = msg.split(" ");
            c.setConta(Integer.parseInt(vetor_de_comando[1]));
            System.out.println("msg: " + msg);
            
            if (Integer.parseInt(vetor_de_comando[0]) == 1) {
                c.setConta(Integer.parseInt(vetor_de_comando[1]));
                sql.depositar(c, Integer.parseInt(vetor_de_comando[2]));
                saida.println(1);
            } else if (Integer.parseInt(vetor_de_comando[0]) == 2) {
                c.setConta(Integer.parseInt(vetor_de_comando[1]));
                ct.setConta(Integer.parseInt(vetor_de_comando[2]));
                sql.transferir(c, Integer.parseInt(vetor_de_comando[3]), ct);
                saida.println(1);
            } else if (Integer.parseInt(vetor_de_comando[0]) == 3) {
                c.setConta(Integer.parseInt(vetor_de_comando[1]));
                sql.sacar(c, Integer.parseInt(vetor_de_comando[2]));
                saida.println(1);
            } else if (Integer.parseInt(vetor_de_comando[0]) == 4) {
                c.setConta(Integer.parseInt(vetor_de_comando[1]));
                int valor = sql.consultar(c);
                System.out.println("SAÍ "+valor);
                saida.println(valor);
            } else {
                System.out.println("Entrada com ERRO ---> " + msg);
                
            }
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (SecurityException ex) {
            Logger.getLogger(conServ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
