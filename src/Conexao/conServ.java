/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import DAO.comandos_SQL;
import Entidades.Clientes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Pedro
 */
public class conServ extends Thread {

    private Socket socket;
    String[] vetor_de_comando;
    
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
                    System.out.println(" Conectou!");
                    Thread t = new conServ(conexao);
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
        //String Depositar: "1" + " " + conta + " " + valor
        //String Transferir: "2" + " " + conta + " "  + conta para transferir + " " + valor
        //String Sacar: "3" + " " + conta + " "  + valor
        //String Consultar: "4" + "" + conta
        //USAR O DAO PARA MEXER NO BANCO DE DADOS(OBRIGATÓRIO!).
        
        vetor_de_comando = new String[4];
        
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            vetor_de_comando = entrada.readLine().split(" ");
            if(Integer.parseInt(vetor_de_comando[1]) == 1){
                Clientes c = new Clientes();
                c.setConta(Integer.parseInt(vetor_de_comando[2]));
                comandos_SQL sql = new comandos_SQL();
                sql.depositar(c, Integer.parseInt(vetor_de_comando[3]));
                
            }else if(Integer.parseInt(vetor_de_comando[1]) == 2){
                Clientes c = new Clientes();
                c.setConta(Integer.parseInt(vetor_de_comando[2]));
                
            }else if(Integer.parseInt(vetor_de_comando[1]) == 3){
                Clientes c = new Clientes();
                c.setConta(Integer.parseInt(vetor_de_comando[2]));
                
            }else if(Integer.parseInt(vetor_de_comando[1]) == 4){
                Clientes c = new Clientes();
                c.setConta(Integer.parseInt(vetor_de_comando[2]));
                
            }else{
                System.out.println("Entrada com ERRO ---> "+entrada.readLine());
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
