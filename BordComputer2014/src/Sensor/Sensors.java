package Sensor;

import java.util.concurrent.ArrayBlockingQueue;

import Computer.BordComputer;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Sensors implements Runnable {
	
	enum Type {
		Hall, Injection, Volume
	}

	private ArrayBlockingQueue<DataSensor> dataSensorQueue;
	private BordComputer bordComputer;

	private double injection = 1.0;
	
	public Sensors(BordComputer bordComputer) {
		this.bordComputer = bordComputer;
		dataSensorQueue = new ArrayBlockingQueue<DataSensor>(100);
	}
	
	private void start() {
		new Thread(new HallSensor()).start();
		new Thread(new InjectionSensor()).start();
		new Thread(new VolumeSensor()).start();
	}
	
	private synchronized void waitTime() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}
	
	private synchronized void waitUntilDataSensorQueueIsNotEmpty() {
		try {
			wait();
		} catch (InterruptedException e) {
		}
	}
	
	public synchronized void notifyDataSensorQueueIsNotEmpty() {
		notify();
	}

	@Override
	public void run() {
		start();
		
		while(true) {
			while (dataSensorQueue.isEmpty()) {
				waitUntilDataSensorQueueIsNotEmpty();
			}
			
			DataSensor data = dataSensorQueue.poll();
			
			switch (data.type) {
			case Hall:
				bordComputer.computeInstantaneousSpeed(data.value);
				break;
			case Injection:
				bordComputer.computeInstantaneousConsumption(data.value);
				break;
			case Volume:
				bordComputer.computeEssenceVolumeDisponible(data.value);
				break;
			default:
				break;
			}
		}
	}
	
	private class HallSensor implements Runnable {

		private double hallEffect = 0.0;

		public void run() {
			while (true) {
				dataSensorQueue.add(new DataSensor(Type.Hall, hallEffect));
				notifyDataSensorQueueIsNotEmpty();
				hallEffect++;
				waitTime();
			}
		}
	}

	private class InjectionSensor implements Runnable {

		public void run() {
			while (true) {
				dataSensorQueue.add(new DataSensor(Type.Injection, injection));
				notifyDataSensorQueueIsNotEmpty();
				waitTime();
			}
		}
	}

	private class VolumeSensor implements Runnable {

		private double volume = 45.0;

		public void run() {
			while (true) {
				dataSensorQueue.add(new DataSensor(Type.Volume, volume));
				notifyDataSensorQueueIsNotEmpty();
				
				if (volume - injection >= 0) {
					volume -= injection;
				}
			}
		}
	}
	
	private static class DataSensor {
		
		private Type type;
		private double value;
		
		public DataSensor(Type type, double value) {
			this.type = type;
			this.value = value;
		}
	}
}
