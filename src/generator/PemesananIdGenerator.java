/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generator;

//import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.util.Random;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;


/**
 *
 * @author nyoma
 */
public class PemesananIdGenerator {
    public boolean isId(String Id, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pemesanan WHERE id_pemesanan = ?";
        
        try(PreparedStatement psmt = conn.prepareStatement(sql))
        {
            psmt.setString(1, Id);
            try (ResultSet rs = psmt.executeQuery())
            {
                if(rs.next())
                {
                    int jumlah =  rs.getInt(1);
                    return jumlah > 0;
                }
            }
        }
        return false;
    }
    
    public String generateIdPemesanan()
    {
        Connection conn = null;
        String idFinal = "";
        
        try
        {
            String url = "jdbc:mysql://localhost:4306/studioease";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
            
            Random random = new Random();
            boolean idIsFound;
            
            do
            {
                int random_number = 1_000_000 + random.nextInt(9_000_000);
                String idPemesanan = String.valueOf(random_number);
                
                idIsFound = isId(idPemesanan, conn);
                
                if (!idIsFound)
                {
                    idFinal = idPemesanan;
                }
                
            } while (idIsFound);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            if (conn != null)
            {
                try{
                    conn.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return idFinal;
        
    }
    
}
