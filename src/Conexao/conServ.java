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
public class conServ implements Runnable{

    private Socket socket;
    String[] vetor_de_comando;
    String[] vetor_saida;
    BufferedReader buffer;
    private InputStream entrada;
    private boolean pausado = false;

    public conServ(Socket socket) {
        this.socket = socket;
    }
    
    private synchronized void verificaPausa() throws InterruptedException {
		// Esse while é necessário pois threads estão sujeitas a spurious
		// wakeups, ou seja, elas podem acordar mesmo que nenhum notify tenha
		// sido dado.

		// Whiles diferentes podem ser usados para descrever condições
		// diferentes. Você também pode ter mais de uma condição no while
		// associada com um e. Por exemplo, no caso de um produtor/consumidor,
		// poderia ser while (!pausado && !fila.cheia()).

		// Nesse caso só temos uma condição, que é dormir quando pausado.
		while (pausado) {
			wait();
		}
	}
    
    public synchronized void setPausado(boolean pausado) {
		this.pausado = pausado;

		// Caso pausado seja definido como false, acordamos a thread e pedimos
		// para ela verificar sua condição. Nesse caso, sabemos que a thread
		// acordará, mas no caso de uma condição com várias alternativas, nem
		// sempre isso seria verdadeiro.
		if (!this.pausado)
			notifyAll();
	}

    public static void main(String args[]) {

        try {
            ServerSocket s = new ServerSocket(2222);
            while (true) {
                System.out.println("Esperando alguem se conectar....");
                try {
                    Socket conexao = s.accept();
                    System.out.println(conexao.getInetAddress() + " Conectou!");
                    //conServ t = new conServ(conexao);
                    new Thread(new conServ(conexao)).start();
                    
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
            
            verificaPausa();
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
                setPausado(true);
                c.setConta(Integer.parseInt(vetor_de_comando[1]));
                int valor = sql.consultar(c);
                System.out.println("SAÍ"+valor);
                saida.println(valor);
            } else {
                System.out.println("Entrada com ERRO ---> " + msg);
                
            }
            
            setPausado(false);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (InterruptedException ex) {
            Logger.getLogger(conServ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
