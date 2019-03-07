package project4;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

public class UpdateSalariedEmployee extends UpdateSpecificEmployee
{
	private JLabel lblWeeklySalary; 
	private JTextField txtWeeklySalary; 

	public UpdateSalariedEmployee( String driver, String databaseURL, 
								   String username, String password )
	{
		super( driver, databaseURL, username, password, EmployeeTypesEnum.SALARIED_EMPLOYEE );
	}

	protected int addTextFields()
	{
		addComponent( lblWeeklySalary, 0, 1, 1, 1, 0, 0, new Insets( 5, 15, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		addComponent( txtWeeklySalary, 1, 1, 1, 1, 0, 0, new Insets( 5, 0, 5, 15 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
		return 1; 
	} 
	
	protected void initialiseTextFields()
	{
		lblWeeklySalary = new JLabel( "Weekly Salary" ); 
		txtWeeklySalary = new JTextField( TEXT_FIELD_TEXT_LENGTH ); 
	}
	
	protected String validateTextFields()
	{
		String errors = ""; 

		if( textFieldTextTooLong( txtWeeklySalary.getText() ) )
		{
			errors += String.format( "%s is too long and should contain %d or less characters\n",
									 lblWeeklySalary.getText(),
									 TEXT_FIELD_TEXT_LENGTH );
		}
		if( textFieldTextEmpty( txtWeeklySalary.getText() ) )
		{
			errors += String.format( "%s is a required field\n",
									 lblWeeklySalary.getText() );
		}
		if( !validateDouble( txtWeeklySalary.getText() ) )
		{
			errors += String.format( "%s must be a valid number\n", lblWeeklySalary.getText() );
		}
			
		return errors; 
	} 
	
	protected String createInsertStatement()
	{
		return "insert into " + TABLE_NAME + " (socialSecurityNumber" +
			   ", weeklySalary" +
			   ", bonus) values ('" + cmbSocialSecurityNumber.getSelectedItem() +
			   "', " + txtWeeklySalary.getText() +
			   ", 0)";
	}
}