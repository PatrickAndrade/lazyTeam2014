package GUI;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Window extends JFrame {

	private TimeWorker time;
	private BordComputer bordComputer;
	private Sensors sensors;
	
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
	
	private JButton resetTimeRunsButton;
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
					
					//restart le lap si stop avant
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
		
		resetTimeRunsButton = new JButton("Reset time runs");
		resetTimeRunsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time.resetTimeRuns();
			}
		});
		resetTimeRunsButton.setBounds(630, 129, 156, 25);
		contentPane.add(resetTimeRunsButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(896, 39, 128, 196);
		contentPane.add(scrollPane);
		
		lapTextArea = new JTextArea();
		scrollPane.setViewportView(lapTextArea);
		lapTextArea.setLineWrap(true);
		lapTextArea.setWrapStyleWord(true);
		lapTextArea.setEditable(false);
		
		vitesseInstantanneeLabel = new JLabel(VITESSE_INSTANTANNEE);
		vitesseInstantanneeLabel.setBounds(12, 13, 190, 16);
		contentPane.add(vitesseInstantanneeLabel);
		
		vitesseMoyenneRAZLabel = new JLabel(VITESSE_MOYENNE_RAZ);
		vitesseMoyenneRAZLabel.setBounds(12, 31, 196, 16);
		contentPane.add(vitesseMoyenneRAZLabel);
		
		vitesseMoyenne0Label = new JLabel(VITESSE_MOYENNE0);
		vitesseMoyenne0Label.setBounds(12, 49, 196, 16);
		contentPane.add(vitesseMoyenne0Label);
		
		kilometrageParcourusRAZLabel = new JLabel(KILOMETRAGE_PARCOURUS_RAZ);
		kilometrageParcourusRAZLabel.setBounds(12, 69, 212, 16);
		contentPane.add(kilometrageParcourusRAZLabel);
		
		kilometrageParcourus0Label = new JLabel(kILOMETRAGE_PARCOURUS0);
		kilometrageParcourus0Label.setBounds(12, 91, 244, 16);
		contentPane.add(kilometrageParcourus0Label);
		
		consommationInstantanneeLabel = new JLabel(CONSOMMATION_INSTANTANNEE);
		consommationInstantanneeLabel.setBounds(12, 116, 244, 16);
		contentPane.add(consommationInstantanneeLabel);
		
		consommationMoyenneRAZLabel = new JLabel(CONSOMMATION_MOYENNE_RAZ);
		consommationMoyenneRAZLabel.setBounds(12, 138, 244, 16);
		contentPane.add(consommationMoyenneRAZLabel);
		
		consommationMoyenne0Label = new JLabel(CONSOMMATION_MOYENNE0);
		consommationMoyenne0Label.setBounds(12, 159, 320, 16);
		contentPane.add(consommationMoyenne0Label);
				
		volumeDEssenceDisponibleLabel = new JLabel(VOLUME_DISPONIBLE);
		volumeDEssenceDisponibleLabel.setBounds(12, 181, 269, 16);
		contentPane.add(volumeDEssenceDisponibleLabel);
		
		autonomieDisponibleLabel = new JLabel(AUTONOMIE_DISPONIBLE);
		autonomieDisponibleLabel.setBounds(12, 201, 230, 16);
		contentPane.add(autonomieDisponibleLabel);
		
		distancePrevuParRapportAUnObjectifLabel = new JLabel(DISTANCE_TO_OBJECTIVE);
		distancePrevuParRapportAUnObjectifLabel.setBounds(12, 219, 391, 16);
		contentPane.add(distancePrevuParRapportAUnObjectifLabel);

		// Code pour lier les composants
		bordComputer = new BordComputer(this);
		time = new TimeWorker(bordComputer, this);
		sensors = new Sensors(bordComputer);
		
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
		autonomieDisponibleLabel.setText(AUTONOMIE_DISPONIBLE + autonomieDisponible);
	}

	public void updateConsommationInstantanee(double mInstantaneousConsumption) {
		consommationInstantanneeLabel.setText(CONSOMMATION_INSTANTANNEE + mInstantaneousConsumption);
	}

	public void updateConsommationMoyenne(double mediumConsumptionRAZ, double mediumConsumptionFrom0) {
		consommationMoyenneRAZLabel.setText(CONSOMMATION_MOYENNE_RAZ + mediumConsumptionRAZ);
		consommationMoyenne0Label.setText(CONSOMMATION_MOYENNE0 + mediumConsumptionFrom0);
	}

	public void updateDistanceToObective(double distanceToObjective) {
		distancePrevuParRapportAUnObjectifLabel.setText(DISTANCE_TO_OBJECTIVE + distanceToObjective);
	}

	public void updateKilometrageParcourus(double distanceCovered, double tripDistanceCovered) {
		kilometrageParcourusRAZLabel.setText(KILOMETRAGE_PARCOURUS_RAZ + tripDistanceCovered);
		kilometrageParcourus0Label.setText(kILOMETRAGE_PARCOURUS0 + distanceCovered);
	}

	public void updateVitesseInstantannee(double instantaneousSpeed) {
		vitesseInstantanneeLabel.setText(VITESSE_INSTANTANNEE + instantaneousSpeed);
	}

	public void updateVitesseMoyenne(double mediumSpeedRAZ, double mediumSpeedFrom0) {
		vitesseMoyenneRAZLabel.setText(VITESSE_MOYENNE_RAZ + mediumSpeedRAZ);
		vitesseMoyenne0Label.setText(VITESSE_MOYENNE0 + mediumSpeedFrom0);
	}

	public void updateVolumeEssenceDisponible(double essenceVolumeDisponible) {
		volumeDEssenceDisponibleLabel.setText(VOLUME_DISPONIBLE + essenceVolumeDisponible);
	}
}
