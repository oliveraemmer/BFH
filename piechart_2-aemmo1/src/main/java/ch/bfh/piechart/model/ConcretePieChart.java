/*
 * Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
 */
package ch.bfh.piechart.model;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.matrix.GraphicOps;
import ch.bfh.matrix.Matrix;
import ch.bfh.piechart.datalayer.SalesValue;

/**
 * Represents a pie chart.
 */
public class ConcretePieChart implements PieChart {

	private final List<ConcretePieChart.ConcreteSlice> slices = new ArrayList<>();
	private double x = 0;
	private double y = 0;
	private double r = 0;

	class ConcreteSlice implements PieChartSlice {

		private Matrix coords;
		private Boolean moved = false;
		private double angle = 0;

		ConcreteSlice(double startAngle, double endAngle) {
			angle = (startAngle + endAngle) / 2;
			Matrix start = GraphicOps.rotate(GraphicOps.UNIT_Y_VECTOR, startAngle);
			Matrix end = GraphicOps.rotate(GraphicOps.UNIT_Y_VECTOR, endAngle);
			coords = new Matrix(new double[][]{{0.0, start.get(0, 0), end.get(0, 0)},
					{0.0, start.get(1, 0), end.get(1, 0)}, {1.0, 1.0, 1.0}});
		}

		@Override
		public Matrix getCoords() {
			if (!moved) {
				Matrix transformation = GraphicOps.translate(ConcretePieChart.this.x, ConcretePieChart.this.y)
						.multiply(GraphicOps.scale(ConcretePieChart.this.r));
				return transformation.multiply(coords);
			} else {
				Matrix transformation = GraphicOps.translate(ConcretePieChart.this.x, ConcretePieChart.this.y)
						.multiply(GraphicOps.scale(ConcretePieChart.this.r));
				Matrix rotatedDetach = GraphicOps.rotate(DETACH_VECTOR, angle);
				Matrix movedCoords = GraphicOps.translate(coords, rotatedDetach);
				return transformation.multiply(movedCoords);
			}
		}

		@Override
		public void move() {
			if (!moved) {
				moved = true;
			} else {
				moved = false;
			}
		}
	}

	/**
	 * Creates a pie chart based on percentages in the list of SalesValue objects.
	 *
	 * @param values sales value objects
	 */
	public ConcretePieChart(List<SalesValue> values) {
		double startAngle = 2.0 * Math.PI;
		for (int i = 0; i < values.size(); i++) {
			double endAngle = startAngle - (values.get(i).getPercentage() / 100) * 2.0 * Math.PI;
			slices.add(new ConcreteSlice(startAngle, endAngle));
			startAngle = endAngle;
		}
	}

	/**
	 * Returns the number of slices in this pie chart.
	 *
	 * @return number of slices
	 */
	@Override
	public int getNbOfSlices() {
		return slices.size();
	}

	/**
	 * Returns the indexed slice of the pie chart.
	 *
	 * @param index slice index
	 * @return slice object
	 */
	@Override
	public PieChartSlice getSlice(int index) {
		return slices.get(index);
	}

	/**
	 * Sets the center position and the radius for the chart.
	 *
	 * @param x the x-value of the center position
	 * @param y the y-value of the center position
	 * @param r the radius for the chart
	 */
	@Override
	public void setPosAndRadius(double valX, double valY, double valR) {
		this.x = valX;
		this.y = valY;
		this.r = valR;
	}

	// ---------------------------------
	// implement observable pattern

	private final List<PieChartObserver> observers = new ArrayList<>();

	/**
	 * To be called when observers need to be notified that one or all slices need
	 * to be updated.
	 *
	 * @param slice the slice to be updated or null if all slices need to be updated
	 */
	@SuppressWarnings("unused")
	private void notifyObservers(PieChartSlice slice) {
		observers.forEach(observer -> observer.update(slice));
	}

	/**
	 * Adds an observer to the list of observers.
	 *
	 * @param observer reference to the observer.
	 */
	@Override
	public void addObserver(PieChartObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	/**
	 *
	 * @param index the index of the slice clicked
	 */
	@Override
	public void onClick(int index) {
		PieChartSlice slice = getSlice(index);
		slice.move();
		notifyObservers(slice);
	}
}
