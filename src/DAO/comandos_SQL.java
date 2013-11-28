/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Conexao.conBD;
import java.sql.Connection;

/**
 *
 * @author Pedro
 */
public class comandos_SQL {
    public Connection connection;
    public comandos_SQL(){
        this.connection = new conBD().getConnection();
    }
    
    private void depositar(){
        
    }
    
    private void sacar(){
        
    }
    
    private void transferir(){
        
    }
    
    private void consultar(){
        
    }
}
