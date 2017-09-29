package timecounter.presentation.counter;

import timecounter.presentation.Color;

public class CounterColor extends Color {
	
	public CounterColor(float time) {
		super(0, 255.f, 0);
		this.time = time;
		this.startTime = time;
	}
	
	void update(float dt) {
		time -= dt;
		if(time > startTime / 4 ) 
			r += (4 * dt / (3 * startTime)) * 255.f;
		else 
			g -= (4 * dt / startTime) * 255.f;
	}
	
	private float startTime;
	private float time;
}
