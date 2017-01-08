import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import org.joda.time.DateTime;

public class Sensor {
	private String sensor_name;
	private DateTime date;
	private int value;
	private DataBase db;
	
	public Sensor(DateTime date, int value, DataBase db, String sensor_name)
	{
		this.date = date;
		this.value = value;
		this.db = db;
		this.sensor_name = sensor_name;
	}
	
	public void insert_to_db()
	{	
		String request = "INSERT INTO " + this.sensor_name + " (date_time, valeur) VALUES ('" + this.date.toString("yyyy-MM-dd HH:mm:ss") + "'," + this.value + ");";

		try {
			this.db.insert_into(request);
		} catch (SQLException e) {
			System.out.println("Erreur lors de l'insertion des valeurs du capteur " + this.sensor_name);
			e.printStackTrace();
		}
	}
	
}
