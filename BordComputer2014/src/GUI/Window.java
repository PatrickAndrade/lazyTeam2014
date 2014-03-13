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
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

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
	private final String kILOMETRAGE_PARCOURUS0 = "Kilometrage parcourus (de 0): ";
	private final String CONSOMMATION_MOYENNE0 = "Consommation moyenne (de 0): ";
	private final String CONSOMMATION_MOYENNE_RAZ = "Consommation moyenne (RAZ): ";
	private final String VOLUME_DISPONIBLE = "Volume d'essence disponible: ";
	private final String AUTONOMIE_DISPONIBLE = "Autonomie disponible: ";

	private JPanel contentPane;

	private JLabel timeRunsLabel;

	private JButton startPauseChronometerButton;

	private JButton stopChronometerButton;

	private JLabel chronometerLabel;

	private JButton lapButton;

	private JLabel timeLabel;

	private JLabel lapsLabel;

	private JTextArea lapTextArea;

	private JButton resetButton;
	private JScrollPane scrollPane;

	private JLabel distancePrevuParRapportAUnObjectifLabel;

	private JLabel autonomieDisponibleLabel;

	private JLabel volumeDEssenceDisponibleLabel;

	private JLabel consommationMoyenne0Label;

	private JLabel consommationMoyenneRAZLabel;

	private JLabel consommationInstantanneeLabel;

	private JLabel kilometrageParcourus0Label;

	private JLabel kilometrageParcourusRAZLabel;

	private JLabel vitesseMoyenne0Label;

	private JLabel vitesseMoyenneRAZLabel;

	private JLabel vitesseInstantanneeLabel;
	private JPanel graphPanel;
	private JPanel imagePanel;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1054, 701);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		timeLabel = new JLabel("Time: ");
		timeLabel.setBounds(630, 13, 244, 16);
		contentPane.add(timeLabel);

		startPauseChronometerButton = new JButton("Start");
		startPauseChronometerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Start/Pause Button
				if (startPauseChronometerButton.getText().equals(START)) {
					startPauseChronometerButton.setText(PAUSE);
					time.startChronometer();

					// restart le lap si stop avant
					if (time.getLastLap().equals("")) {
						lapTextArea.setText("");
					}
				} else if (startPauseChronometerButton.getText().equals(PAUSE)) {
					startPauseChronometerButton.setText(START);
					time.pauseChronometer();
				}
			}
		});
		startPauseChronometerButton.setBounds(632, 91, 71, 25);
		contentPane.add(startPauseChronometerButton);

		stopChronometerButton = new JButton("Stop");
		stopChronometerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				time.stopChronometer();
				startPauseChronometerButton.setText(START);
			}
		});
		stopChronometerButton.setBounds(715, 91, 71, 25);
		contentPane.add(stopChronometerButton);

		chronometerLabel = new JLabel("Chronometer: ");
		chronometerLabel.setBounds(630, 31, 190, 16);
		contentPane.add(chronometerLabel);

		lapButton = new JButton("Lap");
		lapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time.lapChronometer();
				lapTextArea.append("\n" + time.getLastLap());
			}
		});
		lapButton.setBounds(798, 91, 71, 25);
		contentPane.add(lapButton);

		timeRunsLabel = new JLabel("Time runs: ");
		timeRunsLabel.setBounds(630, 49, 135, 16);
		contentPane.add(timeRunsLabel);

		lapsLabel = new JLabel("Laps:");
		lapsLabel.setBounds(895, 13, 56, 16);
		contentPane.add(lapsLabel);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time.resetTimeRuns();
				bordComputer.reset();
			}
		});
		resetButton.setBounds(630, 129, 156, 25);
		contentPane.add(resetButton);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(896, 39, 128, 196);
		contentPane.add(scrollPane);

		lapTextArea = new JTextArea();
		scrollPane.setViewportView(lapTextArea);
		lapTextArea.setLineWrap(true);
		lapTextArea.setWrapStyleWord(true);
		lapTextArea.setEditable(false);

		vitesseInstantanneeLabel = new JLabel(VITESSE_INSTANTANNEE);
		vitesseInstantanneeLabel.setBounds(12, 13, 352, 16);
		contentPane.add(vitesseInstantanneeLabel);

		vitesseMoyenneRAZLabel = new JLabel(VITESSE_MOYENNE_RAZ);
		vitesseMoyenneRAZLabel.setBounds(12, 31, 391, 16);
		contentPane.add(vitesseMoyenneRAZLabel);

		vitesseMoyenne0Label = new JLabel(VITESSE_MOYENNE0);
		vitesseMoyenne0Label.setBounds(12, 49, 406, 16);
		contentPane.add(vitesseMoyenne0Label);

		kilometrageParcourusRAZLabel = new JLabel(KILOMETRAGE_PARCOURUS_RAZ);
		kilometrageParcourusRAZLabel.setBounds(12, 69, 364, 16);
		contentPane.add(kilometrageParcourusRAZLabel);

		kilometrageParcourus0Label = new JLabel(kILOMETRAGE_PARCOURUS0);
		kilometrageParcourus0Label.setBounds(12, 91, 422, 16);
		contentPane.add(kilometrageParcourus0Label);

		consommationInstantanneeLabel = new JLabel(CONSOMMATION_INSTANTANNEE);
		consommationInstantanneeLabel.setBounds(12, 116, 391, 16);
		contentPane.add(consommationInstantanneeLabel);

		consommationMoyenneRAZLabel = new JLabel(CONSOMMATION_MOYENNE_RAZ);
		consommationMoyenneRAZLabel.setBounds(12, 138, 378, 16);
		contentPane.add(consommationMoyenneRAZLabel);

		consommationMoyenne0Label = new JLabel(CONSOMMATION_MOYENNE0);
		consommationMoyenne0Label.setBounds(12, 159, 352, 16);
		contentPane.add(consommationMoyenne0Label);

		volumeDEssenceDisponibleLabel = new JLabel(VOLUME_DISPONIBLE);
		volumeDEssenceDisponibleLabel.setBounds(12, 181, 352, 16);
		contentPane.add(volumeDEssenceDisponibleLabel);

		autonomieDisponibleLabel = new JLabel(AUTONOMIE_DISPONIBLE);
		autonomieDisponibleLabel.setBounds(12, 201, 334, 16);
		contentPane.add(autonomieDisponibleLabel);

		distancePrevuParRapportAUnObjectifLabel = new JLabel(
				DISTANCE_TO_OBJECTIVE);
		distancePrevuParRapportAUnObjectifLabel.setBounds(12, 219, 391, 16);
		contentPane.add(distancePrevuParRapportAUnObjectifLabel);
		
		graphPanel = new JPanel();
		graphPanel.setBounds(12, 262, 1012, 379);
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
		btnNextGraph.setBounds(463, 224, 97, 25);
		contentPane.add(btnNextGraph);
		
		imagePanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				try {
					g.drawImage(ImageIO.read(new File("image.jpg")), 0, 0, null);
				} catch (IOException e) {
				}
			}
		};
		
		imagePanel.setBounds(440, 13, 178, 198);
		contentPane.add(imagePanel);
		
		tryConnect();

		new Thread(time).start();
		new Thread(bordComputer).start();
		new Thread(sensors).start();

		updateTime();
	}

	public synchronized void updateTime() {
		timeLabel.setText("Time: " + time.getTime());
		chronometerLabel.setText("Chronometer: " + time.getChronometer());
		timeRunsLabel.setText("Time runs : " + time.getTimeRuns());
	}

	public void updateAutonomieDisponible(double autonomieDisponible) {
		autonomieDisponibleLabel.setText(AUTONOMIE_DISPONIBLE
				+ roundAtTwoDecimals(autonomieDisponible / 1000) + " km");
	}

	public void updateConsommationInstantanee(double instantaneousConsumption) {
		consommationInstantanneeLabel.setText(CONSOMMATION_INSTANTANNEE
				+ roundAtTwoDecimals(instantaneousConsumption * 100 * 1000)
				+ " l/100km");
	}

	public void updateConsommationMoyenne(double mediumConsumptionRAZ,
			double mediumConsumptionFrom0) {
		consommationMoyenneRAZLabel.setText(CONSOMMATION_MOYENNE_RAZ
				+ roundAtTwoDecimals(mediumConsumptionRAZ * 100 * 1000)
				+ " l/100km");
		
		consommationMoyenne0Label.setText(CONSOMMATION_MOYENNE0
				+ roundAtTwoDecimals(mediumConsumptionFrom0 * 100 * 1000)
				+ " l/100km");
	}

	public void updateDistanceToObective(double distanceToObjective) {
		distancePrevuParRapportAUnObjectifLabel.setText(DISTANCE_TO_OBJECTIVE
				+ roundAtTwoDecimals(distanceToObjective / 1000.0) + " km");
	}

	public void updateKilometrageParcourus(double distanceCovered,
			double tripDistanceCovered, int secondsSinceLaunch) {
		kilometrageParcourusRAZLabel.setText(KILOMETRAGE_PARCOURUS_RAZ
				+ roundAtTwoDecimals(tripDistanceCovered / 1000) + " km");
		kilometrageParcourus0Label.setText(kILOMETRAGE_PARCOURUS0
				+ roundAtTwoDecimals(distanceCovered / 1000) + " km");
		graph.vitesse.add(roundAtTwoDecimals(secondsSinceLaunch / 3600.0), roundAtTwoDecimals(distanceCovered / 1000));
	}

	public void updateVitesseInstantannee(double instantaneousSpeed) {
		vitesseInstantanneeLabel.setText(VITESSE_INSTANTANNEE
				+ roundAtTwoDecimals(instantaneousSpeed * 3.6) + " km/h");
	}

	public void updateVitesseMoyenne(double mediumSpeedRAZ,
			double mediumSpeedFrom0) {
		vitesseMoyenneRAZLabel.setText(VITESSE_MOYENNE_RAZ
				+ roundAtTwoDecimals(mediumSpeedRAZ * 3.6) + " km/h");
		vitesseMoyenne0Label.setText(VITESSE_MOYENNE0
				+ roundAtTwoDecimals(mediumSpeedFrom0 * 3.6) + " km/h");
	}

	public void updateVolumeEssenceDisponible(double essenceVolumeDisponible, int secondsSinceLaunch) {
		volumeDEssenceDisponibleLabel.setText(VOLUME_DISPONIBLE
				+ roundAtTwoDecimals(essenceVolumeDisponible) + " litre");
		graph.consommation.add(secondsSinceLaunch  / 3600.0, roundAtTwoDecimals(essenceVolumeDisponible));
	}

	private double roundAtTwoDecimals(double number) {
		int r = (int) Math.round(number * 100);
		return r / 100.0;
	}

	public void positionMap(double latitude, double longitude) {
		graph.map.clear();
		graph.map.addPoint(latitude, longitude);
		//sendToServerPosition(latitude, longitude);
	}
	
	private final int MAX_COUNTER_TRY_CONNECT = 30;
	private int counterTryConnect = 0;
	
	public void tryConnect() {
		try {
			socket = new Socket("127.0.0.1", 6464);
			outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
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
