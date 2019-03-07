package project4;

public enum DefinedQueriesEnum
{
	SALES_EMPLOYEES ( "Sales Department Employees",
					  "select * from employees where departmentName = 'SALES'" ),
	HOURLY_EMPLOYEES_30( "Hourly Employees Working Over 30 Hours",
						 "select * from employees e, hourlyEmployees he " +
						 "where e.socialSecurityNumber = he.socialSecurityNumber " +
						 "and he.hours > 30" ),
	COMMISSION_EMPLOYEES_RATE_DESC( "Commission Employees in Descending Order of Commission Rate",
								    "select * from employees e, commissionEmployees ce " +
								    "where e.socialSecurityNumber = ce.socialSecurityNumber " +
								    "order by commissionRate desc" );
	
	private final String queryDescription; 
	private final String query; 
	
	// begin constructor
	DefinedQueriesEnum( String description, String query )
	{
		queryDescription = description; 
		this.query = query;
	} 
	
	
	public String toString()
	{
		return queryDescription; 
	} 
	
	public String getQuery()
	{
	   return query; 
	}
} 