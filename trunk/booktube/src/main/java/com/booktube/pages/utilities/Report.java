package com.booktube.pages.utilities;



import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;


import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
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
		Logger.getLogger("Report.saveReportAsPNG()").info("Entre en el metodo para salver imagen");
		ChartUtilities.saveChartAsPNG(new File(filename), chart, width, height);
	}
	
	
	public void saveChartAsPDF(JFreeChart chart, String fileName, int width, int height, String details) throws Exception {
	    if (chart != null) {
	        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
	        try {	             
	            writeChartAsPdf(out, width, height, details);	            
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	        }
	    }//else: input values not available
	}
	
	// Generea el reporte como un pdf y lo escribe en un OutStream
	public void writeChartAsPdf( OutputStream out, int width, int height, String details ) throws Exception{
		 if (chart != null) {
	        try {   
	            //convert chart to PDF with iText:
	            Rectangle pagesize = new Rectangle(width, height); 
	            com.lowagie.text.Document document = new com.lowagie.text.Document(pagesize, 50, 50, 30, 10); //left, right,top,bottom
	            try { 
	                PdfWriter writer = PdfWriter.getInstance(document, out); 
	                document.addAuthor("BookTube"); 
	                document.open(); 
	        
	                // Para agregar el grafico al pdf, uso un PdfContentByte
	                PdfContentByte cb = writer.getDirectContent(); 
	                PdfTemplate tp = cb.createTemplate(width-100, height-100); 
	                Graphics2D g2 = tp.createGraphics(width-100, height-100, new DefaultFontMapper()); 
	        
	                Rectangle2D r2D = new Rectangle2D.Double(0, 0, width-100, height-100); 
	                chart.draw(g2, r2D, null); 
	                g2.dispose(); 
	                cb.addTemplate(tp, 50, 80);
	                
	                // Para agregar texto después del pdf, uso otro PdfContentByte	                
//	                String details = "Edad entre 20 y 50 años, Genero Masculino, Año de registración 2012";
	                PdfTemplate detailsTemplate = cb.createTemplate(width-100, 100);
	                
	                BaseFont bf = BaseFont.createFont();
	                detailsTemplate.setFontAndSize(bf, 14);
	                
	                detailsTemplate.beginText();
	                detailsTemplate.moveText(120, 45);
	                detailsTemplate.showText("Criterios usados para este Reporte");
	                
	                detailsTemplate.setFontAndSize(bf, 10);
	                detailsTemplate.moveText(-80, -30);
	                detailsTemplate.newlineShowText(details);
	                detailsTemplate.endText();
	                
	                cb.addTemplate(detailsTemplate, 50, 20);	                
	                
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
