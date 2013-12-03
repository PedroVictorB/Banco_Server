/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Conexao.conBD;
import Entidades.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class comandos_SQL {

    public Connection connection;

    public comandos_SQL() {
        this.connection = new conBD().getConnection();
    }

    public void depositar(Clientes c, int valor) {

        String sql = "UPDATE  `banco`.`clientes` SET  `saldo` =  ? WHERE  `clientes`.`conta` =?;";
        String sql2 = "SELECT saldo FROM clientes WHERE conta = ?";
        PreparedStatement stmt, stmt2;
        ResultSet rs;
        int total;
        
        try {
            stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, c.getConta());
            rs = stmt2.executeQuery();
            rs.next();
            total = (int) rs.getObject("saldo");
            stmt2.close();
            
            total = total + valor;
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, total);
            stmt.setInt(2, c.getConta());
            stmt.execute();
            stmt.close();
            

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void sacar(Clientes c, int valor) {
        String sql = "UPDATE  `banco`.`clientes` SET  `saldo` =  ? WHERE  `clientes`.`conta` =?;";
        String sql2 = "SELECT saldo FROM clientes WHERE conta = ?";
        PreparedStatement stmt, stmt2;
        ResultSet rs;
        int total;
        try {
            stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, c.getConta());
            rs = stmt2.executeQuery();
            rs.next();
            total = (int) rs.getObject("saldo") - valor;
            
            if (total >= 0) {
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, total);
                stmt.setInt(2, c.getConta());
                stmt.execute();
                stmt.close();
                stmt2.close();
            } else {
                System.out.println("Saldo Insuficiente");
                stmt2.close();
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void transferir(Clientes c, int valor, Clientes ct) {
        System.out.println("Cheguei" + " conta1:"+c.getConta() + " conta2:"+ct.getConta() + " valor:"+valor);
        String sql = "UPDATE  `banco`.`clientes` SET  `saldo` =  ? WHERE  `clientes`.`conta` =?;";
        String sql2 = "SELECT saldo FROM clientes WHERE conta = ?";
        
        PreparedStatement stmt, stmt2, stmt3;
        ResultSet rs;
        int saldo, total1, total2;
        try {
            stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, c.getConta());
            rs = stmt2.executeQuery();
            rs.next();
            saldo = (int) rs.getObject("saldo");
            stmt2.close();
            
            total1 = saldo - valor;
            total2 = saldo + valor;
            System.out.println(total1 + " " + total2);
            if (total1 >= 0) {
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, total1);
                stmt.setInt(2, c.getConta());
                stmt.execute();
                stmt.close();
                
                stmt3 = connection.prepareStatement(sql);
                stmt3.setInt(1, total2);
                stmt3.setInt(2, ct.getConta());
                stmt3.execute();
                stmt3.close();
                
            } else {
                System.out.println("Saldo Insuficiente");
                stmt2.close();
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public int consultar(Clientes c) {

        String sql = "SELECT saldo FROM clientes WHERE conta = ?";
        PreparedStatement stmt;
        ResultSet rs;
        int resultado;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, c.getConta());
            rs = stmt.executeQuery();
            rs.next();
            resultado = (int) rs.getObject("saldo");
            stmt.close();
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(comandos_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
