package com.booktube.pages.utilities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public abstract class Report implements Serializable {
	private static final long serialVersionUID = 1755561753398970149L;
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
	
	
	public void saveChartAsPDF(JFreeChart chart, String fileName, int width, int height) throws Exception {
	    if (chart != null) {
	        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
	        try {	             
	            writeChartAsPdf(out, width, height);	            
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	        }
	    }//else: input values not available
	}
	
	// Generea el reporte como un pdf y lo escribe en un OutStream
	public void writeChartAsPdf( OutputStream out, int width, int height ) throws Exception{
		 if (chart != null) {
	        try {   
	            //convert chart to PDF with iText:
	            Rectangle pagesize = new Rectangle(width, height); 
	            com.lowagie.text.Document document = new com.lowagie.text.Document(pagesize, 50, 50, 50, 50); 
	            try { 
	                PdfWriter writer = PdfWriter.getInstance(document, out); 
	                document.addAuthor("BookTube"); 
	                document.open(); 
	        
	                PdfContentByte cb = writer.getDirectContent(); 
	                PdfTemplate tp = cb.createTemplate(width, height); 
	                Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper()); 
	        
	                Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height); 
	                chart.draw(g2, r2D, null); 
	                g2.dispose(); 
	                cb.addTemplate(tp, 0, 0); 
	            } finally {
	                document.close(); 
	            }
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	        }
		    }//else: input values not availabel
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
