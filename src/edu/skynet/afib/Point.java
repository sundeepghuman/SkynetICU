package edu.skynet.afib;

class Point {
	public float voltage;
	public int time;

	public Point(float voltage, int time) {
		this.voltage = voltage;
		this.time = time;
	}

	@Override
	public String toString() {
		return voltage + " mv @ " + time;
	}
}
