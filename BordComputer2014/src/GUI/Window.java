package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JButton;

import Temps.TimeWorker;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Window extends JFrame {
	
	private TimeWorker time;

	private JPanel contentPane;

	private JLabel timeRunsLabel;

	private JButton startPauseChronometerButton;

	private JButton stopChronometerButton;

	private JLabel chronometerLabel;

	private JButton lapButton;

	private JLabel timeLabel;
	
	private final String START = "Start";
	private final String PAUSE = "Pause";

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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		timeLabel = new JLabel("Time: ");
		timeLabel.setBounds(22, 24, 244, 16);
		contentPane.add(timeLabel);
		
		startPauseChronometerButton = new JButton("Start");
		startPauseChronometerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Start/Pause Button
				if (startPauseChronometerButton.getText().equals(START)) {
					startPauseChronometerButton.setText(PAUSE);
					time.startChronometer();
				} else if (startPauseChronometerButton.getText().equals(PAUSE)) {
					startPauseChronometerButton.setText(START);
					time.pauseChronometer();
				}
			}
		});
		startPauseChronometerButton.setBounds(24, 102, 71, 25);
		contentPane.add(startPauseChronometerButton);
		
		stopChronometerButton = new JButton("Stop");
		stopChronometerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				time.stopChronometer();
				startPauseChronometerButton.setText(START);
			}
		});
		stopChronometerButton.setBounds(107, 102, 71, 25);
		contentPane.add(stopChronometerButton);
		
		chronometerLabel = new JLabel("Chronometer: ");
		chronometerLabel.setBounds(22, 42, 190, 16);
		contentPane.add(chronometerLabel);
		
		lapButton = new JButton("Lap");
		lapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time.lapChronometer();
			}
		});
		lapButton.setBounds(190, 102, 71, 25);
		contentPane.add(lapButton);
		
		timeRunsLabel = new JLabel("Time runs: ");
		timeRunsLabel.setBounds(22, 60, 135, 16);
		contentPane.add(timeRunsLabel);
		
		//Code pour lier les composants
		time = new TimeWorker(this);
		new Thread(time).start();
		
		updateTime();
	}

	public void updateTime() {
		timeLabel.setText("Time: " + time.getTime());
		chronometerLabel.setText("Chronometer: " + time.getChronometer());
		timeRunsLabel.setText("Time runs : " + time.getTimeRuns());
	}
}
