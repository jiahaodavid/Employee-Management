package project4;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JButton;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class UpdateSpecificEmployee extends JFrame
{
	protected JLabel lblSocialSecurityNumber; 
	
	protected JComboBox< String > cmbSocialSecurityNumber; 

	private JTable tblData; 
	private JPanel tablePanel; 
	private JButton btnSave; 
	
	protected ResultSetTableModel mainResultModel; 
	protected ResultSetTableModel ssnResultModel;
	
	protected GridBagLayout gridBagLayout;
	protected GridBagConstraints gridBagConstraints;
	
	protected final String DRIVER; 
	protected final String DATABASE_URL; 
	protected final String USERNAME;
	protected final String PASSWORD; 
	protected final EmployeeTypesEnum EMPLOYEE_TYPE; 
	protected final String TABLE_NAME; 
	protected final String MAIN_QUERY; 
	protected final String SOCIAL_SECURITY_NUMBER_QUERY; 
	protected final int TEXT_FIELD_TEXT_LENGTH = 30;
	private int numberOfTextFields = 0;

	public UpdateSpecificEmployee( String driver, String databaseURL, 
								   String username, String password,
								   EmployeeTypesEnum employeeType )
	{
		super( employeeType.toString() ); 
		
		EMPLOYEE_TYPE = employeeType; 
		DRIVER = driver; 
		DATABASE_URL = databaseURL; 
		USERNAME = username; 
		PASSWORD = password; 
		TABLE_NAME = EMPLOYEE_TYPE.getTableName(); 
		MAIN_QUERY = "select * from " + TABLE_NAME; 
	
		SOCIAL_SECURITY_NUMBER_QUERY = "select socialSecurityNumber " +
						 "from employees " +
						 "where employeeType = '" + EMPLOYEE_TYPE.getEmployeesTableReference() + "'";
		
		try 
		{
			mainResultModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME, PASSWORD, MAIN_QUERY, EMPLOYEE_TYPE );
			
			ssnResultModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME, PASSWORD,
								SOCIAL_SECURITY_NUMBER_QUERY );
			
			lblSocialSecurityNumber = new JLabel( "Social Security Number" ); 
		
			cmbSocialSecurityNumber = new JComboBox< String >(); 
			populateSSNComboBox(); 
		
			btnSave = new JButton( "Save" ); 
			btnSave.addActionListener( 
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						String errors = validateTextFields();
						if( errors.equals( "" ) ) 
						{
							try 
							{
								executeStatement( createInsertStatement() ); 
							}
							catch( SQLException sqlException ) 
							{
								JOptionPane.showMessageDialog(
									null, 
									String.format( "Could not add %s to database\n%s",
                                                                        EMPLOYEE_TYPE.toString(),
									sqlException.getMessage() ), 
									String.format( "Error Adding %s To Database",
										EMPLOYEE_TYPE.toString() ), 
									JOptionPane.ERROR_MESSAGE ); 
								sqlException.printStackTrace(); 
							} 
							catch ( ClassNotFoundException classNotFoundException ) 
							{
								JOptionPane.showMessageDialog(
									null, 
									classNotFoundException.getMessage(),
									"Error", 
									JOptionPane.ERROR_MESSAGE );
								classNotFoundException.printStackTrace();
							}
						}
						else 
						{
							JOptionPane.showMessageDialog( null, 
										"Cannot insert record. Please fix the following errors:\n\n" + errors, 
										"Cannot Insert Record", 
                                                                                JOptionPane.ERROR_MESSAGE ); 
						} 
					} 
				} 
			); 
		
			tblData = new JTable( mainResultModel ); 
			tblData.setGridColor( Color.BLACK ); 
			tablePanel = new JPanel();
			tablePanel.setLayout( new BorderLayout() ); 
			
			tablePanel.add( new JScrollPane( tblData ), BorderLayout.CENTER );
		
			initialiseTextFields(); 
			
			gridBagLayout = new GridBagLayout(); 
			gridBagConstraints = new GridBagConstraints(); 
			setLayout( gridBagLayout );
		
			addComponent( lblSocialSecurityNumber, 0, 0, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( cmbSocialSecurityNumber, 1, 0, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.EAST, gridBagConstraints.NONE );
		
			numberOfTextFields = addTextFields();
			

			addComponent( btnSave, 0, numberOfTextFields + 1, 2, 1, 0, 0, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.NONE );
			addComponent( tablePanel, 0, numberOfTextFields + 2, 3, 1, 1, 1, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.BOTH );
			
			addWindowListener(
				new WindowAdapter() 
				{
				
					public void windowClosing( WindowEvent event )
					{
						if( mainResultModel != null ) 
							mainResultModel.disconnectFromDatabase(); 
							
						if( mainResultModel != null ) 
							mainResultModel.disconnectFromDatabase(); 
					}

					public void windowActivated( WindowEvent event )
					{
						try 
						{
							if( mainResultModel != null ) 
								mainResultModel.setQuery( MAIN_QUERY ); 
						}
						catch( SQLException sqlException )
						{
							JOptionPane.showMessageDialog(
								null,
								sqlException.getMessage(), 
								"Table Model Error", 
								JOptionPane.ERROR_MESSAGE );
							sqlException.printStackTrace(); 
							
							setVisible( false ); 
							dispose(); 
						} 
					} 
				} 
			); 
		
			pack(); 
			setVisible( true ); 
		} 
		catch ( SQLException sqlException ) 
		{
			JOptionPane.showMessageDialog(
				null, 
				sqlException.getMessage(), 
				"Table Model Error", 
				JOptionPane.ERROR_MESSAGE ); 
			sqlException.printStackTrace(); 
			setVisible( false ); 
			dispose(); 
		} 
		catch ( ClassNotFoundException classNotFoundException )
		{
			JOptionPane.showMessageDialog(
				null, 
				String.format( "Could not find database driver %s\n%s", 
					DRIVER,
					classNotFoundException.getMessage() ),
				"Driver Not Found", 
				JOptionPane.ERROR_MESSAGE ); 
			classNotFoundException.printStackTrace();
			
			setVisible( false ); 
			dispose(); 
		} 
	}
	
	private void populateSSNComboBox()
	{
		cmbSocialSecurityNumber.removeAllItems(); 
		for( int i = 0; i < ssnResultModel.getRowCount(); i++ )
		{
			
			cmbSocialSecurityNumber.addItem( ( String ) ssnResultModel.getValueAt( i, 0 ) );
		}
		
		cmbSocialSecurityNumber.setPreferredSize( new Dimension( 300, ( int ) cmbSocialSecurityNumber.getPreferredSize().getHeight() ) );
	} 
	
	private void executeStatement( String sqlStatement ) throws SQLException, ClassNotFoundException
	{
		Connection connection = null; 
		Statement statement = null; 
		
		try 
		{
			Class.forName( DRIVER ); 
			
			
			connection = DriverManager.getConnection( DATABASE_URL, USERNAME, PASSWORD );
			
			statement = connection.createStatement(); 
			statement.executeUpdate( sqlStatement ); 
			mainResultModel.setQuery( MAIN_QUERY ); 
		} 
		catch( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
			
			
			throw new SQLException( String.format( "Error executing SQL statement\n%s\n%s", sqlException.getMessage(), sqlStatement ) );
		} 
		catch( ClassNotFoundException classNotFoundException ) 
		{
			classNotFoundException.printStackTrace(); 
	
			throw new ClassNotFoundException( String.format( "Could not find database driver %s\n%s", DRIVER, classNotFoundException.getMessage() ) );
		} 
		finally 
		{
			try 
			{
				statement.close(); 
				connection.close(); 
			} 
			catch( Exception exception ) 
			{
				exception.printStackTrace(); 
			}
		} 
	} 
	
	protected boolean validateDouble( String text )
	{
		try 
		{
			Double.parseDouble( text ); 
			return true;
		} 
		catch( NumberFormatException numberFormatException )
		{
			numberFormatException.printStackTrace(); 
		} 
		
	
		return false;
	}
	
	
	protected boolean validateInteger( String text )
	{
		try
		{
			Integer.parseInt( text ); 
			return true; 
		} 
		catch( NumberFormatException numberFormatException ) 
		{
			
			numberFormatException.printStackTrace(); 
		} 
		
		
		return false;
	} //
	
	
	protected boolean textFieldTextTooLong( String text )
	{
		return ( text.length() > TEXT_FIELD_TEXT_LENGTH ) ? true : false;
	}
	protected boolean textFieldTextEmpty( String text )
	{
		return ( text.length() < 1 ) ? true : false;
	} 
	protected void addComponent( Component component,
							     int column, int row,
							     int width, int height,
							     int weightx, int weighty,
							     Insets insets, int anchor, int fill )
	{
		gridBagConstraints.gridx = column; 
		gridBagConstraints.gridy = row; 
		gridBagConstraints.gridwidth = width;
		gridBagConstraints.gridheight = height; 
		gridBagConstraints.weightx = weightx; 
		gridBagConstraints.weighty = weighty; 
		gridBagConstraints.insets = insets; 
		gridBagConstraints.anchor = anchor; 
		gridBagConstraints.fill = fill; 
		gridBagLayout.setConstraints( component, gridBagConstraints ); 
		add( component ); 
	} 
	
	
	protected abstract void initialiseTextFields();
	
	
	protected abstract int addTextFields();
	
	protected abstract String validateTextFields();

	protected abstract String createInsertStatement();
} 