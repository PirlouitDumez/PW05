//author : Pirlouit DUMEZ

package fr.isen.java2.db.daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		
	    List<Genre> listOfGenres = new ArrayList<>();
	    
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	    	try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("select * from genre")) {
	                while (results.next()) {
	                    Genre Genre = new Genre(results.getInt("idgenre"),
	                                            results.getString("name"));
	                    listOfGenres.add(Genre);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return listOfGenres;
	}

	public Genre getGenre(String name) {
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement(
	        		"SELECT * FROM genre WHERE name = ?")) {
	            statement.setString(1, name);
	            try (ResultSet results = statement.executeQuery()) {
	                if (results.next()) {
	                    return new Genre(results.getInt("idgenre"),
	                                    results.getString("name"));
	                }
	            }
	        }
	    } catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	    return null;
	}

	public static Genre getGenreById(int id) {
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement(
	        		"SELECT * FROM genre WHERE idgenre = ?")) {
	            statement.setInt(1, id);
	            try (ResultSet results = statement.executeQuery()) {
	                if (results.next()) {
	                    return new Genre(results.getInt("idgenre"),
	                                    results.getString("name"));
	                }
	            }
	        }
	    } catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	    return null;
	}

	
	public void addGenre(String name) {
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        String sqlQuery = "insert into genre(name) "+"VALUES(?)";
	        try (PreparedStatement statement = connection.prepareStatement(
	                        sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, name);
	            statement.executeUpdate();	            
	        }
	    }
	    catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	}
}
