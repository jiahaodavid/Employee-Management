package project4;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel
{
	private Connection connection; 
	private Statement statement; 
	private ResultSet resultSet; 
	private ResultSetMetaData metaData;
	private int numberOfRows; 
	private boolean connectedToDatabase = false; 
	private final EmployeeTypesEnum EMPLOYEE_TYPE; 
	
	public ResultSetTableModel( String driver, String url, String username,
								String password, String query, EmployeeTypesEnum employeeType )
		throws SQLException, ClassNotFoundException
	{	
		EMPLOYEE_TYPE = employeeType; 
		
		try 
		{
			Class.forName( driver ); 
			
			connection = DriverManager.getConnection( url, username, password );
		
			statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			connectedToDatabase = true; 
		} 
		catch( SQLException sqlException )
		{
			throw new SQLException( "Error connecting to database: " + sqlException.getMessage() );
		} 
		
		try 
		{
			setQuery( query ); 
		}
		catch( SQLException sqlException )
		{
			throw sqlException; 
		} 
		
	} 
	public ResultSetTableModel( String driver, String url, String username,
								String password, String query )
		throws SQLException, ClassNotFoundException
	{
		this( driver, url, username, password, query, null );
	} 
	
	public void setQuery( String query ) throws SQLException, IllegalStateException 
	{
		if ( !connectedToDatabase ) 
        	throw new IllegalStateException( "Not Connected to Database" ); // throw an IllegalStateException
        
        try
        {
			resultSet = statement.executeQuery( query ); 
			metaData = resultSet.getMetaData(); 
			resultSet.last();
			numberOfRows = resultSet.getRow(); 
			fireTableStructureChanged(); 
		} 
		catch( SQLException sqlException ) 
		{
			throw new SQLException( String.format( "Error setting query\n%s\n%s", sqlException.getMessage(), query ) );
		}
	} 
	public Class getColumnClass( int column ) throws IllegalStateException
	{
		if ( !connectedToDatabase ) 
        	throw new IllegalStateException( "Not Connected to Database" );

		try
		{
			String className = metaData.getColumnClassName( column + 1 );
			return Class.forName( className );
		} 
		catch ( Exception exception ) 
		{
			exception.printStackTrace(); 
		} 
		return Object.class;
	} 
	public int getRowCount() throws IllegalStateException
	{
		if ( !connectedToDatabase ) 
        	throw new IllegalStateException( "Not Connected to Database" ); 

		return numberOfRows;
	} 
	
	public int getColumnCount() throws IllegalStateException
	{
		if ( !connectedToDatabase )
        	throw new IllegalStateException( "Not Connected to Database" ); 
        	
		try
		{
			return metaData.getColumnCount();
		} 
		catch ( SQLException sqlException )
		{
			sqlException.printStackTrace(); 
		} 
		
		return 0; 
	} 
	
	public String getColumnName( int column ) throws IllegalStateException
	{
		if ( !connectedToDatabase ) 
        	throw new IllegalStateException( "Not Connected to Database" ); 
		try 
		{
			if( EMPLOYEE_TYPE != null )
			{
				return EMPLOYEE_TYPE.getColumnHeader( metaData.getColumnName( column + 1 ) );
			}
			else
				return metaData.getColumnName( column + 1 ); // return the column name
		} 
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		}
		return "";
	}
	public Object getValueAt( int row, int column ) throws IllegalStateException
	{
		if ( !connectedToDatabase )
        	throw new IllegalStateException( "Not Connected to Database" ); 
        	
		try
		{
			resultSet.absolute( row + 1 ); 
			return resultSet.getObject( column + 1 ); 
		} 
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		}

		return "";
	}
	public void disconnectFromDatabase()
	{
		if( connectedToDatabase ) 
		{
			try 
			{                                            
				resultSet.close();              
				statement.close(); 
				connection.close(); 
			} 
			catch ( SQLException sqlException )
			{                                            
				sqlException.printStackTrace(); 
			} 
			finally 
			{
				connectedToDatabase = false; 
			} 
		} 
	} 
} 