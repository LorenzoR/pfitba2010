package com.booktube.pages.utilities;

import java.awt.Font;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class PieReport extends Report {
	private static final long serialVersionUID = 4543877477746800635L;

	public PieReport(Dataset dataset, String... labels) {
		super(dataset, labels);
	}

	@Override
	public void configureReport() {
		chart = ChartFactory.createPieChart(title, // chart title
				(PieDataset) dataset, // data
				true, // include legend
				true, // include tooltips
				false); // locale

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {2}",
				NumberFormat.getNumberInstance(), NumberFormat
						.getPercentInstance()));
		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		((StandardPieSectionLabelGenerator)plot.getLabelGenerator()).getPercentFormat().setMaximumFractionDigits(2);
	}

	@Override
	public void processOtherLabels(String[] labels) {
		// Para este reporte no se necesitan otras etiquetas. Solo el titulo
	}

}
