import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
	private String db_url;
	private String db_user;
	private String db_paswd;
	
	private Connection connexion;
	private Statement statement;
	
	public DataBase(Config conf)
	{
		this.db_url = conf.get_property("db_url");
		this.db_user = conf.get_property("db_user");
		this.db_paswd = conf.get_property("db_paswd");
		
		init_db();
	}
	
	public void close() throws SQLException
	{
		connexion.close();
	}
	
	public int insert_into(String request) throws SQLException
	{
		statement = connexion.createStatement();
        int statut = statement.executeUpdate(request);
        statement.close();
        
		return statut;
	}
	
	private void init_db()
	{
	    	 connexion = null;

	         try
	         {
	             System.out.println("Connexion à la base de données ...");
	             Class.forName("com.mysql.jdbc.Driver");
	             connexion = DriverManager.getConnection("jdbc:mysql:" + this.db_url, this.db_user, this.db_paswd);
	             System.out.println("Connexion réussie");
	            
	         }
	         catch(ClassNotFoundException error)
	         {
	             System.out.println("Error:" + error.getMessage()); 
	         }

	         catch(SQLException error)
	         {
	             System.out.println("Error:" + error.getMessage());
	         }
	}
}
