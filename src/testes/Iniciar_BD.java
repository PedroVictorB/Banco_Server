/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import Conexao.conBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Pedro
 */
public class Iniciar_BD {
    public Connection connection;
    
    public Iniciar_BD(){
        this.connection = new conBD().getConnection();
        // lembrar de ter um banco de dados com o nome "banco"
        String sql = "create table clientes(conta INT NOT NULL AUTO_INCREMENT,saldo INT,primary key (conta));";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
    
    
}
