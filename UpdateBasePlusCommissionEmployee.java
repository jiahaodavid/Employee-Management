package project4;


import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

public class UpdateBasePlusCommissionEmployee extends UpdateSpecificEmployee
{
	private JLabel lblGrossSales; 
	private JLabel lblCommissionRate; 
	private JLabel lblBaseSalary; 
	
	private JTextField txtGrossSales;
	private JTextField txtCommissionRate; 
	private JTextField txtBaseSalary; 

	public UpdateBasePlusCommissionEmployee( String driver, String databaseURL, 
										     String username, String password )
	{
		super( driver, databaseURL, username, password, EmployeeTypesEnum.BASE_PLUS_COMMISSION_EMPLOYEE );
	} 
	
	protected int addTextFields()
	{
		addComponent( lblGrossSales, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtGrossSales, 1, 1, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( lblCommissionRate, 0, 2, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtCommissionRate, 1, 2, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( lblBaseSalary, 0, 3, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtBaseSalary, 1, 3, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		return 3; 
	} 
	protected void initialiseTextFields()
	{
		lblGrossSales = new JLabel( "Gross Sales" ); 
		txtGrossSales = new JTextField( TEXT_FIELD_TEXT_LENGTH ); 
		lblCommissionRate = new JLabel( "Commission Rate" ); 
		txtCommissionRate = new JTextField( TEXT_FIELD_TEXT_LENGTH ); 
		lblBaseSalary = new JLabel( "Base Salary" ); 
		txtBaseSalary = new JTextField( TEXT_FIELD_TEXT_LENGTH );
	} 
	
	
	protected String validateTextFields()
	{
		String errors = "";

		if( textFieldTextTooLong( txtGrossSales.getText() ) )
		{
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblGrossSales.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		if( textFieldTextEmpty( txtGrossSales.getText() ) )
		{
			errors += String.format( "%s is a required field\n",
									 lblGrossSales.getText() );
		}
		if( !validateInteger( txtGrossSales.getText() ) )
		{
			errors += String.format( "%s must be a valid integer\n", lblGrossSales.getText() );
		}
		if( textFieldTextTooLong( txtCommissionRate.getText() ) )
		{
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblCommissionRate.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		if( textFieldTextEmpty( txtCommissionRate.getText() ) )
		{
			errors += String.format( "%s is a required field\n",
									 lblCommissionRate.getText() );
		}
		if( !validateDouble( txtCommissionRate.getText() ) )
		{
			errors += String.format( "%s must be a valid number\n", lblCommissionRate.getText() );
		}
		if( textFieldTextTooLong( txtBaseSalary.getText() ) )
		{
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblBaseSalary.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		if( textFieldTextEmpty( txtBaseSalary.getText() ) )
		{
			errors += String.format( "%s is a required field\n",
									 lblBaseSalary.getText() );
		}
		if( !validateDouble( txtBaseSalary.getText() ) )
		{
			errors += String.format( "%s must be a valid number\n", lblBaseSalary.getText() );
		}
			
		return errors; 
	} 
	protected String createInsertStatement()
	{
		return "insert into " + TABLE_NAME + " (socialSecurityNumber" +
			   ", grossSales" +
			   ", commissionRate" +
			   ", baseSalary" +
			   ", bonus) values ('" + cmbSocialSecurityNumber.getSelectedItem() +
			   "', " + txtGrossSales.getText() +
			   ", " + txtCommissionRate.getText() +
			   ", " + txtBaseSalary.getText() +
			   ", 0)";
	} 
} 