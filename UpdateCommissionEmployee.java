package project4;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

public class UpdateCommissionEmployee extends UpdateSpecificEmployee
{
	private JLabel lblGrossSales; 
	private JLabel lblCommissionRate; 
	
	private JTextField txtGrossSales; 
	private JTextField txtCommissionRate; 

	public UpdateCommissionEmployee( String driver, String databaseURL, 
								     String username, String password )
	{
		super( driver, databaseURL, username, password, EmployeeTypesEnum.COMMISSION_EMPLOYEE );
	} 
	protected int addTextFields()
	{
		addComponent( lblGrossSales, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtGrossSales, 1, 1, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( lblCommissionRate, 0, 2, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtCommissionRate, 1, 2, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		return 2; 
	} 
	
	protected void initialiseTextFields()
	{
		lblGrossSales = new JLabel( "Gross Sales" );
		txtGrossSales = new JTextField( TEXT_FIELD_TEXT_LENGTH ); 
		lblCommissionRate = new JLabel( "Commission Rate" ); 
		txtCommissionRate = new JTextField( TEXT_FIELD_TEXT_LENGTH );
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
			
		return errors; 
	} 
	protected String createInsertStatement()
	{
		return "insert into " + TABLE_NAME + " (socialSecurityNumber" +
			   ", grossSales" +
			   ", commissionRate" +
			   ", bonus) values ('" + cmbSocialSecurityNumber.getSelectedItem() +
			   "', " + txtGrossSales.getText() +
			   ", " + txtCommissionRate.getText() +
			   ", 0)";
	} 
} 