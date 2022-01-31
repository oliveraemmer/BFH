/*
	* Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
	*/
package ch.bfh.piechart.ui;

import ch.bfh.piechart.datalayer.SalesValue;
import ch.bfh.piechart.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
	* Controller for the JavaFX representation of the pie chart.
	*/
public class ChartController implements PieChartObserver {

	@FXML
	Pane pane;
	@FXML
	Label errorMsg;

	private PieChart pieChart;

	private CircleSector[] sectors;

	private PieChartProvider pcp;

	/**
		* Creates the controller. Gets the pie chart model from the provider class and
		* creates its visual representation.
		*/
	public ChartController() {

		Platform.runLater(() -> {

			try {
				pcp = new PieChartProvider();

				try {
					pieChart = pcp.getPieChart();
				} catch (Exception e) {
					e.printStackTrace();
				}
				pieChart.addObserver(this);

				sectors = new CircleSector[pieChart.getNbOfSlices()];
				System.out.println("NbOfSlices: " + pieChart.getNbOfSlices());
				for (int i = 0; i < pieChart.getNbOfSlices(); i++) {
					// For each slice in the chart, create a CircleSector instance and add it
					// to the collection of circle sectors.
					sectors[i] = new CircleSector();
					sectors[i].update(pieChart.getSlice(i).getCoords());
					// TODO: register the piechart.onClick() method as event handler at each circle sector
					int finalI = i;
					sectors[i].addEventHandler(MouseEvent.MOUSE_CLICKED,
							new EventHandler<MouseEvent>() {
								public void handle(MouseEvent e) {
									pieChart.onClick(finalI);
								}
							});
					// add the circle sector to the children of the pane
					pane.getChildren().add(sectors[i]);
					pane.widthProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observableValue,
											Number number, Number t1) {
							setPosAndRadius();
						}
					});
					pane.heightProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observableValue,
											Number number, Number t1) {
							setPosAndRadius();
						}
					});
				}
				setPosAndRadius();
			} catch (Exception ex) {
				errorMsg.setText(ex.getMessage());
				ex.printStackTrace();
			}
		});
	}

	/**
		* Calculate position and radius for the pie chart depending on the window size.
		*/
	void setPosAndRadius() {
		// CHECKSTYLE:OFF MagicNumber
		if (pieChart != null) {
			double x = pane.getWidth() * 0.5;
			double y = pane.getHeight() * 0.5;
			double r = ((x > y) ? y : x) * 0.8;
			// Adjusts the center and the radius of the pie chart
			pieChart.setPosAndRadius(x, y, r);
			update();
		}
	}

	/**
		* Called by the JavaFX framework. Sets listeners to be informed when the window
		* size changes.
		*/
	public void initialize() {

		ChangeListener<Number> paneSizeListener = (observable, oldValue, newValue) -> {
			setPosAndRadius();
		};

		pane.widthProperty().addListener(paneSizeListener);
		pane.heightProperty().addListener(paneSizeListener);
	}

	/**
		* Update the visual representation of one or all slices.
		*
		* @param slice The slice to be updated or null if all slices need to be
		*              updated.
		*/
	@Override
	public void update(PieChartSlice slice) {
		// else if null is given then update all circle sectors.
		for (int i = 0; i < pieChart.getNbOfSlices(); i++) {
			if (pieChart.getSlice(i) == slice) {
				sectors[i].update(slice.getCoords());
			}
		}
	}
	public void update() {
		// else if null is given then update all circle sectors.
		for (int i = 0; i < pieChart.getNbOfSlices(); i++) {
			sectors[i].update(pieChart.getSlice(i).getCoords());
		}
	}
}
