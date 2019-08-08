package utils;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class ClockTools {
	
	private float curTime = 0.0f;
	private float previousTime = 0.0f;
	private float deltaTime = 0.0f;
	private float fps;
	
	public void updateDelta(){
		curTime = (float) getTime();
		deltaTime = curTime - previousTime;
		previousTime = curTime;
	}
	
	public void updateFPS(){
		fps = 1.0f / deltaTime;
		Display.setTitle("Azur Engine - " + getFPS() + " FPS");
	}
	
	public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
	
	public float getDeltaTime(){
		return deltaTime;
	}
	
	public int getFPS(){
		return (int) fps;
	}
	
}
