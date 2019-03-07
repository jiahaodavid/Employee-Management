package project4;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JButton;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QueryFrame extends JFrame
{	
	private JLabel lblInstructions; 
	private JComboBox< DefinedQueriesEnum > cmbDefinedQueries; 
	private JButton btnDefinedQueries; 
	private JTextArea txtCustomQuery; 
	private JButton btnCustomQuery; 
	private JPanel tablePanel; 
	private JTable tblQueryResults; 
	
	private ResultSetTableModel queryResultsModel;
	
	private GridBagLayout gridBagLayout; 
	private GridBagConstraints gridBagConstraints; 
	
	private final String DRIVER; 
	private final String DATABASE_URL; 
	private final String USERNAME; 
	private final String PASSWORD; 

	public QueryFrame( String driver, String databaseURL, 
					   String username, String password )
	{
		super( "Database Queries" ); 
		DRIVER = driver; 
		DATABASE_URL = databaseURL; 
		USERNAME = username; 
		PASSWORD = password; 
		
		lblInstructions = new JLabel( "Choose a defined query or create a custom query in the text area below" );
		
		cmbDefinedQueries = new JComboBox< DefinedQueriesEnum >( DefinedQueriesEnum.values() );
		
		txtCustomQuery = new JTextArea( 10, 30 ); 
		txtCustomQuery.setLineWrap( true ); 
		txtCustomQuery.setWrapStyleWord( true ); 
		
		try 
		{
			queryResultsModel = new ResultSetTableModel( DRIVER, DATABASE_URL, USERNAME, PASSWORD,
				( ( DefinedQueriesEnum ) cmbDefinedQueries.getSelectedItem() ).getQuery() );
			
			btnDefinedQueries = new JButton( "Execute Defined Query" ); 
			btnDefinedQueries.addActionListener( 
				new ActionListener()
				{	
					public void actionPerformed( ActionEvent event )
					{					
						try 
						{
							queryResultsModel.setQuery( ( ( DefinedQueriesEnum ) cmbDefinedQueries.getSelectedItem() ).getQuery() );
						} 
						catch( SQLException sqlException ) 
						{
							JOptionPane.showMessageDialog(
								null, 
								sqlException.getMessage(),
								"Table Model Error", 
								JOptionPane.ERROR_MESSAGE ); 
							sqlException.printStackTrace(); 
						} 
					} 
				}
			); 
		
			btnCustomQuery = new JButton( "Execute Custom Query" ); 
			btnCustomQuery.addActionListener( 
				new ActionListener()
				{
					public void actionPerformed( ActionEvent event )
					{
						try 
						{
							queryResultsModel.setQuery( txtCustomQuery.getText() );
						} 
						catch( SQLException sqlException ) 
						{
							JOptionPane.showMessageDialog(
								null, 
								String.format( "Not a valid SQL query\n%s\n%s",
									txtCustomQuery.getText(),
									sqlException.getMessage() ), 
								"Query Invalid", 
								JOptionPane.ERROR_MESSAGE ); 
						} 
					} 
				}
			); 
		
			tblQueryResults = new JTable( queryResultsModel ); 
			tblQueryResults.setGridColor( Color.BLACK ); 
			tablePanel = new JPanel(); 
			tablePanel.setLayout( new BorderLayout() ); 
			
			
			tablePanel.add( new JScrollPane( tblQueryResults ), BorderLayout.CENTER );
		
			gridBagLayout = new GridBagLayout(); 
			gridBagConstraints = new GridBagConstraints(); 
			setLayout( gridBagLayout ); 
			
			
			addComponent( lblInstructions, 0, 0, 2, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.CENTER, gridBagConstraints.NONE );
			addComponent( cmbDefinedQueries, 0, 1, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( btnDefinedQueries, 1, 1, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.WEST, gridBagConstraints.NONE );
			addComponent( txtCustomQuery, 0, 2, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.WEST, gridBagConstraints.HORIZONTAL );
			addComponent( btnCustomQuery, 1, 2, 1, 1, 0, 0, new Insets( 5, 5, 5, 5 ), gridBagConstraints.NORTHWEST, gridBagConstraints.HORIZONTAL );
			addComponent( tablePanel, 0, 3, 3, 1, 1, 1, new Insets( 5, 0, 5, 0 ), gridBagConstraints.CENTER, gridBagConstraints.BOTH );
		
			addWindowListener(
				new WindowAdapter() // declare anonymous inner class
				{
					public void windowClosing( WindowEvent event )
					{
						if( queryResultsModel != null )
							queryResultsModel.disconnectFromDatabase(); 
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
	
	private void addComponent( Component component,
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
}