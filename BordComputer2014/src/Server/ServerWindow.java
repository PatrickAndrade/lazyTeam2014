package Server;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ServerWindow extends JFrame {

	private JPanel contentPane;
	private JPanel imagePanel;
	private Graph graph;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow frame = new ServerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1016, 551);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel graphPanel = new JPanel();
		graphPanel.setBounds(0, 0, 831, 513);
		contentPane.add(graphPanel);
		
		imagePanel = new JPanel() {
			
			@Override
			public void paint(Graphics g) {
				try {
					g.drawImage(ImageIO.read(new File("image.jpg")), 0, 0, null);
				} catch (IOException e) {
				}
			}
		};
		
		imagePanel.setBounds(829, 0, 178, 198);
		contentPane.add(imagePanel);

		// Code
		graph = new Graph(graphPanel);

		new Thread(new Server(graph)).start();
		new Thread(new Printer(graph)).start();
	}
}

class Server implements Runnable {

	private Graph graph;

	public Server(Graph graph) {
		this.graph = graph;
	}

	@Override
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(6464);
			System.out.println("Server started!");
		} catch (IOException e) {
			System.err.println("Impossible launch server!");
		}

		while (server != null) {
			try {
				Socket socket = server.accept();
				new Thread(new Connection(graph, socket)).start();
			} catch (IOException e) {
			}
		}
	}
}

class Connection implements Runnable {

	private Graph graph;
	private Socket socket;

	public Connection(Graph graph, Socket socket) {
		this.graph = graph;
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println("New connection");
		DataInputStream inputStream = null;
		try {
			inputStream = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));
			
			while (true) {
				double latitude = inputStream.readDouble();
				double longitude = inputStream.readDouble();				
				graph.map.addPoint(socket, latitude, longitude);
			}
		} catch (IOException e) {
		}
		
		if (inputStream != null) {
			try {
				inputStream.close();
				socket.close();
			} catch (IOException e) {
			}
		}
		
		graph.map.removeSocket(socket);
	}
}

class Printer implements Runnable {
	
	private Graph graph;

	public Printer(Graph graph) {
		this.graph = graph;
	}
	
	private void waitOneSeconds() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
		while (true) {
			graph.map.printAllPoints();
			waitOneSeconds();
		}
	}
}
