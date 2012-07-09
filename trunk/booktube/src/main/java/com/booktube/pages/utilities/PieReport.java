package com.booktube.pages.utilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PiePlot3D;
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
		chart = ChartFactory.createPieChart3D(title,  	// chart title
	            (PieDataset)dataset,                // data
	            true,                   // include legend
	            true,					// include tooltips
	            false);	 				// locale
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
	}

	@Override
	public void processOtherLabels(String[] labels) {
		// Para este reporte no se necesitan otras etiquetas. Solo el titulo
	}

}
