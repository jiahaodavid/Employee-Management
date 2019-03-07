package project4;


import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.Box;
import java.sql.SQLException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class AddEmployees extends JFrame
{
	private JButton btnGenericEmployee; 
	private JButton btnSalariedEmployee; 
	private JButton btnCommissionEmployee; 
	private JButton btnBasePlusCommissionEmployee; 
	private JButton btnHourlyEmployee; 
	private JButton btnIncreaseBaseSalary; 
	private JButton btnCalculateBonuses; // calculate employee bonuses button
	private JButton btnQueryDatabase; // query database button
	private JTable tblEmployeeTable; 
	private JPanel pnlPanel1; 
	private JPanel pnlPanel2; 
	private JPanel pnlPanel3; 
	private JPanel pnlPanel4; 
	private Box boxAddEmployees; 
	private ResultSetTableModel genericEmployeeModel; 
	
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; 
	private static final String DATABASE_URL = "jdbc:mysql://localhost/david"; 
	private static final String USERNAME = "root"; 
	private static final String PASSWORD = "123456"; 
	
	private static final String BASE_PLUS_COMMISSION_SALARY_INCREASE = "1.1"; 
	private static final String BIRTHDAY_BONUS = "100"; 
	private static final String SALES_BONUS = "100"; 
	private static final String GROSS_SALES_TARGET = "10000"; 
	private static final String GENERIC_EMPLOYEE_QUERY = "select * from employees"; 
	
	private static final String UPDATE_BASE_SALARY = "update basePlusCommissionEmployees " + 
                "set baseSalary = baseSalary * " + BASE_PLUS_COMMISSION_SALARY_INCREASE;
	
	private static final String CALCULATE_COMMISSION_BONUS = "update commissionEmployees " +
								 "set bonus = " + SALES_BONUS + " " +
							"where grossSales > " + GROSS_SALES_TARGET;

	// begin constructor
	public AddEmployees()
	{
		super( "Add Employees" ); 
		
		try 
		{
			
			genericEmployeeModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME,
								PASSWORD, GENERIC_EMPLOYEE_QUERY,
							EmployeeTypesEnum.GENERIC_EMPLOYEE );
															
			btnGenericEmployee = new JButton( "Add Employee" ); 
			
			
			btnGenericEmployee.addActionListener(
				new ActionListener() 
				{
					
					public void actionPerformed( ActionEvent event )
					{
						
						UpdateGenericEmployee genericEmployee =
							new UpdateGenericEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} 
				} 
			); 
		
			btnSalariedEmployee = new JButton( "Add Salaried Employee" ); 
			btnSalariedEmployee.addActionListener(
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						UpdateSalariedEmployee salariedEmployee =
							new UpdateSalariedEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} 
				} 
			); 
		
			btnCommissionEmployee = new JButton( "Add Commission Employee" ); 
			btnCommissionEmployee.addActionListener(
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						UpdateCommissionEmployee commissionEmployee =
							new UpdateCommissionEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} 
				} 
			); 
			btnBasePlusCommissionEmployee = new JButton( "Add Base Plus Commission Employee" );
			btnBasePlusCommissionEmployee.addActionListener(
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						UpdateBasePlusCommissionEmployee basePlusCommissionEmployee =
							new UpdateBasePlusCommissionEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					}
				}
			); 
		
			btnHourlyEmployee = new JButton( "Add Hourly Employee" ); 
			btnHourlyEmployee.addActionListener(
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						UpdateHourlyEmployee genericEmployee =
							new UpdateHourlyEmployee( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					} 
				} 
			); 
			btnIncreaseBaseSalary = new JButton( "Increase 10% Base Salary for Base Plus Commission Employees" );
			btnIncreaseBaseSalary.addActionListener(
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						try 
						{
							executeStatement( UPDATE_BASE_SALARY );
							JOptionPane.showMessageDialog(
								null, 
								"Base salary updated for Base Plus Commission Employees", 
								"Base Salary IS Updated", 
								JOptionPane.INFORMATION_MESSAGE ); 
						} 
						catch( SQLException sqlException ) 
						{
							JOptionPane.showMessageDialog(
								null,
								String.format( "Could not update base salary for Base Plus Commission Employees\n%s",
											   sqlException.getMessage() ), 
								"Base Salary Not Updated", 
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
				}
			); 
		
			btnCalculateBonuses = new JButton( "Calculate Bonuses" ); 
			btnCalculateBonuses.addActionListener(
				new ActionListener()
				{
					public void actionPerformed( ActionEvent event )
					{
						try 
						{
							resetBonuses( EmployeeTypesEnum.SALARIED_EMPLOYEE );
							resetBonuses( EmployeeTypesEnum.COMMISSION_EMPLOYEE );
							resetBonuses( EmployeeTypesEnum.BASE_PLUS_COMMISSION_EMPLOYEE );
							resetBonuses( EmployeeTypesEnum.HOURLY_EMPLOYEE );
	
							executeStatement( CALCULATE_COMMISSION_BONUS );
							
							addBirthdayBonuses( EmployeeTypesEnum.SALARIED_EMPLOYEE );
							addBirthdayBonuses( EmployeeTypesEnum.COMMISSION_EMPLOYEE );
							addBirthdayBonuses( EmployeeTypesEnum.BASE_PLUS_COMMISSION_EMPLOYEE );
							addBirthdayBonuses( EmployeeTypesEnum.HOURLY_EMPLOYEE );
							
							JOptionPane.showMessageDialog(
								null, 
								"Bonuses calculated and applied",
								"Bonuses Calculated", 
								JOptionPane.INFORMATION_MESSAGE ); 
						}
						catch( SQLException sqlException )
						{
							JOptionPane.showMessageDialog(
								null,
								String.format( "Could not calculate employee bonuses\n%s",
									sqlException.getMessage() ),
								"Bonuses Not Applied",
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
				} 
			); 
		
			btnQueryDatabase = new JButton( "Perform the query" ); 
			btnQueryDatabase.addActionListener(
				new ActionListener() 
				{
					public void actionPerformed( ActionEvent event )
					{
						QueryFrame queryFrame = new QueryFrame( DRIVER, DATABASE_URL, USERNAME, PASSWORD );
					}
				} 
			); 
				
			tblEmployeeTable = new JTable( genericEmployeeModel ); 
			tblEmployeeTable.setGridColor( Color.BLACK ); 
		
			pnlPanel1 = new JPanel(); 
			pnlPanel1.add( btnGenericEmployee ); 
		
			pnlPanel2 = new JPanel(); 
			pnlPanel2.add( btnSalariedEmployee ); 
			pnlPanel2.add( btnCommissionEmployee ); 
			pnlPanel2.add( btnBasePlusCommissionEmployee ); 
			pnlPanel2.add( btnHourlyEmployee ); 
		
			pnlPanel3 = new JPanel(); 
			pnlPanel3.setLayout( new BorderLayout() ); 
			
			pnlPanel3.add( new JScrollPane( tblEmployeeTable ), BorderLayout.CENTER );
		
			pnlPanel4 = new JPanel(); 
			pnlPanel4.add( btnIncreaseBaseSalary ); 
			pnlPanel4.add( btnCalculateBonuses ); 
			pnlPanel4.add( btnQueryDatabase ); 
		
			boxAddEmployees = Box.createVerticalBox(); 
			boxAddEmployees.add( pnlPanel1 ); 
			boxAddEmployees.add( pnlPanel2 ); 
			boxAddEmployees.add( pnlPanel3 ); 
			boxAddEmployees.add( pnlPanel4 ); 
	
			add( boxAddEmployees ); 
			pack(); 
			setVisible( true ); 
		
			
			addWindowListener(
				new WindowAdapter() 
				{
					public void windowClosing( WindowEvent event )
					{
						if( genericEmployeeModel != null ) 
							genericEmployeeModel.disconnectFromDatabase(); 
					} 
					public void windowActivated( WindowEvent event )
					{
						try 
						{
							if( genericEmployeeModel != null )
								genericEmployeeModel.setQuery( GENERIC_EMPLOYEE_QUERY ); // set the table model query
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
			genericEmployeeModel.setQuery( GENERIC_EMPLOYEE_QUERY ); 
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
	
	private void resetBonuses( EmployeeTypesEnum employeeType ) throws SQLException, ClassNotFoundException
	{
		try
		{
			executeStatement( "update " + employeeType.getTableName() + " " + "set bonus = 0" );
		} 
		catch( SQLException sqlException ) 
		{
			throw sqlException; 
		} 
		catch( ClassNotFoundException classNotFoundException ) 
		{
			throw classNotFoundException; 
		} 
	}

	private void addBirthdayBonuses( EmployeeTypesEnum employeeType ) throws SQLException, ClassNotFoundException
	{
		Calendar today = Calendar.getInstance(); 
		int currentMonth = today.get( Calendar.MONTH ) + 1; // create a variable to hold the current month
		
		String employeeStatement = "update " + employeeType.getTableName() + " " +
								   "set bonus = bonus + " + BIRTHDAY_BONUS + " " +
								   "where socialSecurityNumber in (" +
								   "select socialSecurityNumber " +
								   "from employees " +
								   "where employeeType = '" + employeeType.getEmployeesTableReference() + "' " +
								   "and MONTH(birthday) = " + currentMonth + ")";
		
		try 
		{
			executeStatement( employeeStatement ); 
		} 
		catch( SQLException sqlException ) 
		{
			throw sqlException;
		} 
		catch( ClassNotFoundException classNotFoundException ) 
		{
			throw classNotFoundException; 
		} 
	} 
}