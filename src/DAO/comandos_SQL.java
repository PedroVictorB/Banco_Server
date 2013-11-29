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
        
        try {
            stmt2 = connection.prepareStatement(sql2);
            rs = stmt2.executeQuery();

            int total = (int) rs.getObject("saldo") - valor;
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

    public void sacar() {
    }

    public void transferir() {
    }

    public int consultar(Clientes c) {
        
        String sql = "SELECT saldo FROM clientes WHERE conta = ?";
        PreparedStatement stmt;
        ResultSet rs;
        
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, c.getConta());
            rs = stmt.executeQuery();
            stmt.close();
            return (int)rs.getObject("saldo");
        } catch (SQLException ex) {
            Logger.getLogger(comandos_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
