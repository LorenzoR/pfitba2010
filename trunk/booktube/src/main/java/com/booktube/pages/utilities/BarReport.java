package com.booktube.pages.utilities;	
	
import java.awt.Color;

//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartFrame;
//import org.jfree.chart.ChartUtilities;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;


public class BarReport extends Report{
	private String XLabel;
	private String YLabel;
	
	 public BarReport(Dataset dataset, String...labels) {
		super(dataset, labels);		
	}

	@Override
	public void processOtherLabels(String[] labels) {
		// labels[0] lo procesa la superclase, porque siempre es el titulo (comun a todas las subclases)
		XLabel = labels[1];
		YLabel = labels[2];	
	}

	@Override
	public void configureReport() {
		chart = ChartFactory.createBarChart3D(title,
				XLabel, YLabel,
				(DefaultCategoryDataset)dataset, PlotOrientation.VERTICAL, true, true, false);
		//BufferedImage img = ImageIO.read(new File("src/img/cars.jpg"));
		//chart.setBackgroundImage(img);
		CategoryPlot plot = chart.getCategoryPlot();
		//plot.setBackgroundImage(img);
		BarRenderer render = (BarRenderer)plot.getRenderer();
		render.setSeriesPaint(0, Color.darkGray);
		render.setSeriesPaint(1, Color.ORANGE);
		chart.setBackgroundPaint(Color.WHITE);
		
	}

	
}



