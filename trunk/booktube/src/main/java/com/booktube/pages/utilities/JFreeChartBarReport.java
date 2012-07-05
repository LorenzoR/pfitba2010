package com.booktube.pages.utilities;	
	
import java.awt.Color;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartFrame;
//import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;


public class JFreeChartBarReport {
	
	
	 public JFreeChart crearGrafica(DefaultCategoryDataset dataset) {
		 JFreeChart chart = ChartFactory.createBarChart3D("Nivel de ignorancia",
					"Estudiantes", "Cursos jalados",
					dataset, PlotOrientation.VERTICAL, true, true, false);
		//BufferedImage img = ImageIO.read(new File("src/img/cars.jpg"));
		//chart.setBackgroundImage(img);
		CategoryPlot plot = chart.getCategoryPlot();
		//plot.setBackgroundImage(img);
		BarRenderer render = (BarRenderer)plot.getRenderer();
		render.setSeriesPaint(0, Color.darkGray);
		render.setSeriesPaint(1, Color.ORANGE);
		chart.setBackgroundPaint(Color.WHITE);
		
		return chart;
	 }

	
}



