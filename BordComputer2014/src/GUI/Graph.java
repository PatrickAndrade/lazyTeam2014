package GUI;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Graph {
	private JPanel panel;
	private int nextGraph = 2; 
	
	private ChartPanel chartPanel1;
	private ChartPanel chartPanel2;


	private JFreeChart chart1;
	private JFreeChart chart2;
	
	
	private  XYSeriesCollection graph1Collection = new XYSeriesCollection();
	private  XYSeriesCollection graph2Collection = new XYSeriesCollection();

	public XYSeries consommation = new XYSeries("Consommation");
	public XYSeries vitesse = new XYSeries("Vitesse");
	public XYSeries altitude = new XYSeries("altitude");
	public Map map;

	
	public  Graph(JPanel jpanel) {
		panel = jpanel;
		map = new Map();
		
		//add function to different graph:
		graph1Collection.addSeries(consommation);        
		graph2Collection.addSeries(vitesse);
				
		        chart1 = ChartFactory.createXYLineChart(
		            "Consommation",
		            "heure", 
		            "litre", 
		            graph1Collection,
		            PlotOrientation.VERTICAL,
		            true,
		            true,
		            false
		        );
		        
		        chart2 = ChartFactory.createXYLineChart(
			            "Vitesse",
			            "seconde", 
			            "metre", 
			            graph2Collection,
			            PlotOrientation.VERTICAL,
			            true,
			            true,
			            false
			        );
		        
		         
		         
		         chartPanel1 = new ChartPanel(chart1);		    
		         chartPanel2 = new ChartPanel(chart2);		    
	
		         
		         chartPanel1.setMouseZoomable(true);
		         chartPanel2.setMouseZoomable(true);

		         
		         panel.add(map);

	}
	
	public void showNextGraph(){
		panel.remove(chartPanel1);
		panel.remove(chartPanel2);
		panel.remove(map);
		switch(nextGraph){
		case 1: panel.add(map);
				nextGraph++; 
				break;
		case 2: panel.add(chartPanel1);
				nextGraph++; 
				break;
		case 3: panel.add(chartPanel2);
				nextGraph = 1; 
				break;

		}
		panel.validate();
		panel.repaint();
		
	}
	

}
