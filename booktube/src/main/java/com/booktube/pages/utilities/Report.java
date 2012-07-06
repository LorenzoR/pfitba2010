package com.booktube.pages.utilities;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

public abstract class Report {
	protected String title;
	protected Dataset dataset;
	protected JFreeChart chart;
	
	//Como mínimo "labels" tiene el titulo del reporte
	public Report(Dataset dataset, String... labels){
		this.dataset = dataset;
		processLabels(labels);		
		configureReport();
	}

	public void saveReportAsPNG( String filename, int width, int height) throws IOException{
		ChartUtilities.saveChartAsPNG(new File(filename), chart, width, height);
	}
	
	// Implementa el patron template method, cada subclase debe implementar processOtherLabels()
	private void processLabels(String[] labels) {
		//El primer label es siempre el titulo del reporte.
		title = labels[0];
		processOtherLabels(labels);
	}
	
	public abstract void processOtherLabels(String[] labels);
	
	public abstract void configureReport();

}
