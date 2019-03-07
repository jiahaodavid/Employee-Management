package project4;

// import required classes
import java.util.HashMap;
import java.util.Map;

// begin class
public enum EmployeeTypesEnum
{

	GENERIC_EMPLOYEE  ( "employees", "Generic Employee", "employee",
						 new String[] { "socialSecurityNumber",
										"firstName",
										"lastName",
										"birthday",
										"employeeType",
										"departmentName" },
						 new String[] { "Social Security Number",
										"First Name",
										"Last Name",
										"Birthday",
										"Employee Type",
										"Department Name" }
					   ),
	
	SALARIED_EMPLOYEE  ( "salariedEmployees", "Salaried Employee", "salariedEmployee",
						 new String[] { "socialsecurityNumber",
										"weeklySalary",
										"bonus" },
						 new String[] { "Social Security Number",
										"Weekly Salary",
										"Bonus" }
					   ),
	
	COMMISSION_EMPLOYEE ( "commissionEmployees" , "Commission Employee", "commissionEmployee",
						  new String[] { "socialSecurityNumber",
										 "grossSales",
										 "commissionRate",
										 "bonus" },
						  new String[] { "Social Security Number",
										 "Gross Sales",
										 "Commission Rate",
										 "Bonus" }
						),
	
	BASE_PLUS_COMMISSION_EMPLOYEE ( "basePlusCommissionEmployees", "Base Plus Commission Employee", "basePlusCommissionEmployee",
									new String[] { "socialSecurityNumber",
												   "grossSales",
												   "commissionRate",
												   "baseSalary",
												   "bonus" },
									new String[] { "Social Security Number",
												   "Gross Sales",
												   "Commission Rate",
												   "Base Salary",
												   "Bonus" }
								  ),
	
	HOURLY_EMPLOYEE ( "hourlyEmployees", "Hourly Employee", "hourlyEmployee",
					  new String[] { "socialSecurityNumber",
									 "hours",
									 "wage",
									 "bonus" },
					  new String[] { "Social Security Number",
									 "Hours",
									 "Wage",
									 "Bonus" }
					);
	
	private final String tableName; 
	private final String description; 
	private final String employeesTableReference; 
	private final Map< String, String > columnNamesMap; 
	
	
	EmployeeTypesEnum( String tableName, String description, String reference,
					   String[] columnNames, String[] columnHeaders )
	{
		this.tableName = tableName; 
		this.description = description; 
		employeesTableReference = reference; 
		columnNamesMap = new HashMap< String, String >(); 
		for( int i = 0; i < columnNames.length; i++ )
			columnNamesMap.put( columnNames[ i ], columnHeaders[ i ] ); 
	} 

	public String getColumnHeader( String columnName )
	{
		String columnHeader = columnNamesMap.get( columnName ); 
		if( columnHeader == null ) 
			columnHeader = columnName; 
		
		return columnHeader; 
	} 
	
	
	public String getTableName()
	{
		return tableName; 
	} 
	
	
	public String getEmployeesTableReference()
	{
		return employeesTableReference; 
	} 

	
	public String toString()
	{
		return description; 
	} 

}