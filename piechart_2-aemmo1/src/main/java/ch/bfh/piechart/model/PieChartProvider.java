/*
 * Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
 */
package ch.bfh.piechart.model;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.piechart.datalayer.ConnectionManager;
import ch.bfh.piechart.datalayer.SalesValue;
import ch.bfh.piechart.datalayer.SalesValueLoader;
import ch.bfh.piechart.datalayer.SalesValueRepository;

// TODO Complete import statements


/**
 * Service class providing sales value information as a pie chart. Upon loading
 * this class, then 1) all sales values are loaded from the database, 2) there
 * relative percentage values are computed, and 3) the updated sales value
 * objects are persisted.
 */
public class PieChartProvider {

	private static boolean done = false;
	public static final String FILENAME = "salesvalues.txt";

	private static SalesValueRepository repo;
	private static List<SalesValue> sv = new ArrayList<>();

	/**
	 * Loads all sales values, computes there relative percentage values, and stores
	 * the updated sales values in the database.
	 */
	// CHECKSTYLE:OFF EmptyBlock
	static {
		/*
		 * 1. It makes a connection to the database using the ConnectionManager;
		 * 2. It reads all sales values available;
		 * 3. It computes the relative percentage for the sales values;
		 * 4. It updates the sales values in the database.
		 * Note: If any kind of exception is thrown in the above 4 steps then
		 * catch it in a RuntimeExpetion and throw it from within the static block.
		 */

		Connection connection = ConnectionManager.getConnection(true);
		SalesValueLoader.loadSalesValues();
		repo = new SalesValueRepository(connection);
		try {
			sv = repo.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Calcualte total value
		double total = 0;
		for (SalesValue value : sv) {
			total += value.getValue();
		}
		for (SalesValue value : sv) {
			double percentage = 100 / total * value.getValue();
			value.setPercentage(percentage);
		}
	}

	/**
	 * Creates a pie chart based on the list of sales value tuples.
	 *
	 * @return a pie chart
	 * @throws Exception if sales values cannot be obtained
	 */
	public PieChart getPieChart() throws Exception {
		PieChart pc = new ConcretePieChart(sv);
		return pc;
	}

	/**
	 * Returns the sales values for this pie chart repository. That is, the
	 * corresponding pie chart is based on the list of these sales values.
	 *
	 * @return a list of sales values the corresponding pie chart is based on
	 * @throws Exception if sales values cannot be obtained
	 */
	public List<SalesValue> getPieChartSalesValues() throws Exception {
		return sv;
	}
}
