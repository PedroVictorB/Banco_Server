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

/**
 *
 * @author Pedro
 */
public class comandos_SQL {
    public Connection connection;
    public comandos_SQL(){
        this.connection = new conBD().getConnection();
    }
    
    public void depositar(Clientes c, int valor){
        
        String sql = "UPDATE  `banco`.`clientes` SET  `saldo` =  ? WHERE  `clientes`.`conta` =?;";
        String sql2 = "SELECT saldo FROM clientes WHERE conta = ?";
        
        try {
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            ResultSet rs = stmt2.executeQuery();
            c.setSaldo((int)rs.getObject("saldo"));
            
            int total  = (int)rs.getObject("saldo") - valor;
            if(total >= 0){
                c.setSaldo(total);
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, total);
                stmt.setInt(2, c.getConta());
            }else{
                System.out.println("Saldo Insuficiente");
            }
            
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
    
    public void sacar(){
        
    }
    
    public void transferir(){
        
    }
    
    public void consultar(){
        
    }
}
