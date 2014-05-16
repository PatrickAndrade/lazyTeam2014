package GUI;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Computer.BordComputer;
import Sensor.Sensors;
import Time.TimeWorker;

/**
 * This is the main class that show the GUI and link each component
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private TimeWorker time;
	private BordComputer bordComputer;
	private Sensors sensors;
	private Graph graph;
	private Socket socket = null;
	private DataOutputStream outputStream = null;

	private final String START = "Start";
	private final String PAUSE = "Pause";
	private final String CONSOMMATION_INSTANTANNEE = "Consommation instantannee: ";
	private final String VITESSE_INSTANTANNEE = "Vitesse instantannee: ";
	private final String VITESSE_MOYENNE0 = "Vitesse moyenne (de 0): ";
	private final String VITESSE_MOYENNE_RAZ = "Vitesse moyenne (RAZ): ";
	private final String DISTANCE_TO_OBJECTIVE = "Distance pr\u00E9vu par rapport \u00E0 un objectif: ";
	private final String KILOMETRAGE_PARCOURUS_RAZ = "Kilometrage parcourus (RAZ): ";
	private final String KILOMETRAGE_PARCOURUS0 = "Kilometrage parcourus (de 0): ";
	private final String CONSOMMATION_MOYENNE0 = "Consommation moyenne (de 0): ";
	private final String CONSOMMATION_MOYENNE_RAZ = "Consommation moyenne (RAZ): ";
	private final String VOLUME_DISPONIBLE = "Volume d'essence disponible: ";
	private final String AUTONOMIE_DISPONIBLE = "Autonomie disponible: ";
	private final String TIME = "Date: ";
	private final String CHRONOMETER = "Chronometre: ";
	private final String TIME_RUNS = "Time runs: ";

	private JPanel contentPane;

	private JButton startPauseChronometerButton;

	private JButton stopChronometerButton;

	private JButton lapButton;

	private JButton resetButton;
	private JScrollPane scrollPane;
	private JPanel graphPanel;
	private DefaultTableModel lapModelTable;
	private Vector<String> lapTableName;

	private DefaultTableModel carDataModel;
	private String[] carDataColumnName = { "Name", "Value" };
	private String[][] carDataInitialisation = { { VITESSE_INSTANTANNEE, "" },
			{ VITESSE_MOYENNE_RAZ, "" }, { VITESSE_MOYENNE0, "" },
			{ KILOMETRAGE_PARCOURUS_RAZ, "" }, { KILOMETRAGE_PARCOURUS0, "" },
			{ CONSOMMATION_INSTANTANNEE, "" },
			{ CONSOMMATION_MOYENNE_RAZ, "" }, { CONSOMMATION_MOYENNE0, "" },
			{ VOLUME_DISPONIBLE, "" }, { AUTONOMIE_DISPONIBLE, "" },
			{ DISTANCE_TO_OBJECTIVE, "" } };

	private DefaultTableModel timeDataModel;
	private String[] timeColumnName = { "Name", "Value" };
	private String[][] timeDataInitialisation = { { TIME, "" },
			{ CHRONOMETER, "" }, { TIME_RUNS, "" } };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
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
	public Window() {
		setTitle("Projet - Electronique III");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 903, 752);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		startPauseChronometerButton = new JButton("Start");
		startPauseChronometerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Start/Pause Button
				if (startPauseChronometerButton.getText().equals(START)) {
					startPauseChronometerButton.setText(PAUSE);
					time.startChronometer();

					// restart le lap si stop avant
					if (time.getLastLap().equals("")) {
						lapModelTable.getDataVector().removeAllElements();
					}
				} else if (startPauseChronometerButton.getText().equals(PAUSE)) {
					startPauseChronometerButton.setText(START);
					time.pauseChronometer();
				}
			}
		});
		startPauseChronometerButton.setBounds(725, 356, 154, 25);
		contentPane.add(startPauseChronometerButton);

		stopChronometerButton = new JButton("Stop");
		stopChronometerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				time.stopChronometer();
				startPauseChronometerButton.setText(START);
			}
		});
		stopChronometerButton.setBounds(725, 394, 156, 25);
		contentPane.add(stopChronometerButton);

		lapButton = new JButton("Lap");
		lapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (time.lapChronometer()) {
					Vector<String> lap = new Vector<String>();
					lap.add(time.getLastLap());
					lapModelTable.getDataVector().add(lap);
					lapModelTable.fireTableDataChanged();
				}
			}
		});

		/*
		 * time.lapChronometer(); lapTextArea.append("\n" + time.getLastLap());
		 */
		lapButton.setBounds(725, 432, 156, 25);
		contentPane.add(lapButton);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time.resetTimeRuns();
				bordComputer.reset();
			}
		});
		resetButton.setBounds(725, 470, 156, 25);
		contentPane.add(resetButton);

		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(725, 97, 154, 208);
		contentPane.add(scrollPane);

		lapModelTable = new DefaultTableModel();
		lapTableName = new Vector<String>();
		lapTableName.add("Lap");
		lapModelTable.setColumnIdentifiers(lapTableName);
		lapTable = new JTable(lapModelTable);
		lapTable.setEnabled(false);
		scrollPane.setViewportView(lapTable);

		graphPanel = new JPanel();
		graphPanel.setBounds(12, 248, 701, 448);
		contentPane.add(graphPanel);

		// Code pour lier les composants
		bordComputer = new BordComputer(this);
		time = new TimeWorker(bordComputer, this);
		sensors = new Sensors(bordComputer);
		graph = new Graph(graphPanel);

		JButton btnNextGraph = new JButton("Next Graph");
		btnNextGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graph.showNextGraph();
			}
		});
		btnNextGraph.setBounds(725, 318, 154, 25);
		contentPane.add(btnNextGraph);

		JScrollPane carDataScrollPane = new JScrollPane();
		carDataScrollPane.setBounds(12, 13, 470, 199);
		contentPane.add(carDataScrollPane);

		carDataModel = new DefaultTableModel(carDataInitialisation,
				carDataColumnName);
		carDataTable = new JTable(carDataModel);
		carDataTable.setEnabled(false);
		carDataScrollPane.setViewportView(carDataTable);

		timeScrollPane = new JScrollPane();
		timeScrollPane.setEnabled(false);
		timeScrollPane.setBounds(494, 13, 338, 71);
		contentPane.add(timeScrollPane);

		timeDataModel = new DefaultTableModel(timeDataInitialisation,
				timeColumnName);
		timeTable = new JTable(timeDataModel);
		timeTable.setEnabled(false);
		timeScrollPane.setViewportView(timeTable);

		JPanel imagePanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				try {
					g.drawImage(ImageIO.read(new File("image.jpg")), 0, 0, null);
				} catch (IOException e) {
				}
			}
		};
		imagePanel.setBounds(725, 508, 156, 184);
		contentPane.add(imagePanel);

		tryConnect();

		new Thread(time).start();
		new Thread(bordComputer).start();
		new Thread(sensors).start();

		updateTime();
	}

	public synchronized void updateTime() {

		timeDataModel.setValueAt(time.getTime(), 0, 1);
		timeDataModel.setValueAt(time.getChronometer(), 1, 1);
		timeDataModel.setValueAt(time.getTimeRuns(), 2, 1);
		timeDataModel.fireTableDataChanged();
	}

	public void updateAutonomieDisponible(double autonomieDisponible) {
		String data = roundAtTwoDecimals(autonomieDisponible / 1000) + " km";
		updateData(data, 9);
	}

	public void updateConsommationInstantanee(double instantaneousConsumption) {
		String data = roundAtTwoDecimals(instantaneousConsumption * 100 * 1000)
				+ " l/100km";
		updateData(data, 5);
	}

	public void updateConsommationMoyenne(double mediumConsumptionRAZ,
			double mediumConsumptionFrom0) {
		String data = roundAtTwoDecimals(mediumConsumptionRAZ * 100 * 1000)
				+ " l/100km";
		updateData(data, 6);

		data = roundAtTwoDecimals(mediumConsumptionFrom0 * 100 * 1000)
				+ " l/100km";
		updateData(data, 7);
	}

	public void updateDistanceToObective(double distanceToObjective) {
		String data = roundAtTwoDecimals(distanceToObjective / 1000.0) + " km";
		updateData(data, 10);
	}

	public void updateKilometrageParcourus(double distanceCovered,
			double tripDistanceCovered, int secondsSinceLaunch) {
		graph.vitesse.add(roundAtTwoDecimals(secondsSinceLaunch / 3600.0),
				roundAtTwoDecimals(distanceCovered / 1000));

		String data = roundAtTwoDecimals(tripDistanceCovered / 1000) + " km";
		updateData(data, 3);

		data = roundAtTwoDecimals(distanceCovered / 1000) + " km";
		updateData(data, 4);
	}

	public void updateVitesseInstantannee(double instantaneousSpeed) {
		String data = roundAtTwoDecimals(instantaneousSpeed * 3.6) + " km/h";
		updateData(data, 0);
	}

	public void updateVitesseMoyenne(double mediumSpeedRAZ,
			double mediumSpeedFrom0) {
		String data = roundAtTwoDecimals(mediumSpeedRAZ * 3.6) + " km/h";
		updateData(data, 1);

		data = roundAtTwoDecimals(mediumSpeedFrom0 * 3.6) + " km/h";
		updateData(data, 2);
	}

	public void updateVolumeEssenceDisponible(double essenceVolumeDisponible,
			int secondsSinceLaunch) {
		String data = roundAtTwoDecimals(essenceVolumeDisponible) + " litre";
		updateData(data, 8);
	}

	private void updateData(String data, int row) {
		carDataModel.setValueAt(data, row, 1);
		carDataModel.fireTableDataChanged();
	}

	private double roundAtTwoDecimals(double number) {
		int r = (int) Math.round(number * 100);
		return r / 100.0;
	}

	public void positionMap(double latitude, double longitude) {
		graph.map.clear();
		graph.map.addPoint(latitude, longitude);
		sendToServerPosition(latitude, longitude);
	}

	private final int MAX_COUNTER_TRY_CONNECT = 30;
	private int counterTryConnect = 0;
	private JTable lapTable;
	private JTable carDataTable;
	private JScrollPane timeScrollPane;
	private JTable timeTable;

	public void tryConnect() {
		try {
			socket = new Socket("127.0.0.1", 6464);
			outputStream = new DataOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));
			System.out.println("Connected");
		} catch (IOException e1) {
			System.err.println("Can't connect to server!");
		}
	}

	public void sendToServerPosition(double latitude, double longitude) {

		if (socket != null) {
			try {
				outputStream.writeDouble(latitude);
				outputStream.writeDouble(longitude);
				outputStream.flush();
			} catch (IOException e) {
				System.err.println("Disconnected");
				try {
					outputStream.close();
					socket.close();
				} catch (IOException e1) {
				}

				outputStream = null;
				socket = null;
				counterTryConnect = 0;
			}

		} else if (counterTryConnect > MAX_COUNTER_TRY_CONNECT) {
			tryConnect();
			counterTryConnect = 0;
		} else {
			counterTryConnect++;
		}
	}
}
