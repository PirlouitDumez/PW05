package fr.isen.java2.db.daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import fr.isen.java2.db.entities.Film;

public class FilmDao {

	public List<Film> listFilms() {
		List<Film> listOfFilms = new ArrayList<>();
	    
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	    	try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre")) {
	                while (results.next()) {
	                    Film film = new Film(results.getInt("idfilm"),
	                    					 results.getString("title"),
	                    					 results.getDate("release_date").toLocalDate(),
	                    					 GenreDao.getGenreById(results.getInt("genre_id")),
	                    					 results.getInt("duration"),
	                                         results.getString("director"),
	                    					 results.getString("summary"));
	                    listOfFilms.add(film);
	                }
	            }
	        }
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return listOfFilms;	}

	public List<Film> listFilmsByGenre(String genreName) {
		List<Film> listOfFilms = new ArrayList<>();
	    
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	    	try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = 'Comedy'")) {
	                while (results.next()) {
	                    Film film = new Film(results.getInt("idfilm"),
	                    					 results.getString("title"),
	                    					 results.getDate("release_date").toLocalDate(),
	                    					 GenreDao.getGenreById(results.getInt("genre_id")),
	                    					 results.getInt("duration"),
	                                         results.getString("director"),
	                    					 results.getString("summary"));
	                    listOfFilms.add(film);
	                }
	            }
	        }
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return listOfFilms;	
	}

	public static Film addFilm(Film film) {
		
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        String sqlQuery = "INSERT INTO film(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
	        try (PreparedStatement statement = connection.prepareStatement(
	                        sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, film.getTitle());
	            statement.setDate(2, Date.valueOf(film.getReleaseDate()));
	            statement.setInt(3, film.getGenre().getId());
	            statement.setInt(4, film.getDuration());
	            statement.setString(5, film.getDirector());
	            statement.setString(6, film.getSummary());
	            statement.executeUpdate();
	        }
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	        
	    }
		
		int id = -1;
	    try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	    	String sqlQuery = "SELECT * FROM film where (film.title=? and film.director=?)";	    	
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
		    	statement.setString(1, film.getTitle());
		    	statement.setString(2, film.getDirector());
	            try (ResultSet results = statement.executeQuery()) {
	                while (results.next()) {
	                	id = results.getInt("idfilm");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
		
		film.setId(id);
		return film;
	}
}
