package timecounter.presentation.counter;

import timecounter.presentation.AnimationWindow;

public class DecorativeRing {
	private float period;
	private float rMin;
	private float rMax;
	private float x, y;
	private float strokeWeight;
	
	private AnimationWindow window;
	
	private float time;
	
	private final float START_ANGLE = 3 * (float)Math.PI / 2;
	private final float PI = (float)Math.PI;
	
	public DecorativeRing(float x, float y, float rMin, float rMax, float period, float strokeWeight, AnimationWindow window) {
		time = 0;
		this.x = x;
		this.y = y;
		this.period = period;
		this.rMax = rMax;
		this.rMin = rMin;
		this.strokeWeight = strokeWeight;
		this.window = window;
	}
	
	void draw(float parentX, float parentY, float parentAlpha) {
		window.arc(parentX + x, parentY + y, rMin, rMin, START_ANGLE, START_ANGLE + (time / period)*2*PI);
		window.strokeWeight(strokeWeight);
		window.noFill();
		float alpha = 255.f * (float)Math.cos(time/period * PI/2);
		if(alpha > parentAlpha)
			alpha = parentAlpha;
		window.stroke(255.f, 255.f, 255.f, alpha);
		float r = rMin + (rMax - rMin) * (time / period);
		window.arc(parentX + x, parentY + y, r, r, 0, 2 * PI);
		window.noStroke();
	}
	
	void update(float dt) {
		time += dt;
		if(time > period)
			time -= period;
	}
}
