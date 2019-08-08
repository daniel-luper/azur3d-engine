package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final boolean devMode = false;
	private static final int FPS_CAP = 120;
	private static final int DEV_WIDTH = 1280;
	private static final int DEV_HEIGHT = 720;
	private static final int PLAY_WIDTH = 1920;
	private static final int PLAY_HEIGHT = 1080;
	
	public static void createDisplay(){
		
		int WIDTH = PLAY_WIDTH;
		int HEIGHT = PLAY_HEIGHT;
		boolean fullscreen = true;
		
		if(devMode){
			WIDTH = DEV_WIDTH;
			HEIGHT = DEV_HEIGHT;
			fullscreen = false;
		}
		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			
			DisplayMode displayMode = new DisplayMode(WIDTH, HEIGHT);
	        DisplayMode[] modes = Display.getAvailableDisplayModes();

	        for (int i = 0; i < modes.length; i++)
	        {
	        	if (modes[i].getWidth() == WIDTH && modes[i].getHeight() == HEIGHT && modes[i].isFullscreenCapable()){
	        			displayMode = modes[i];
	        	}
	        }
			Display.setDisplayMode(displayMode);
			Display.setFullscreen(fullscreen);
			Display.setTitle("Azur Game Engine");
			Display.create(new PixelFormat(), attribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);
		Display.update();
		
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}

	public static boolean isDevMode() {return devMode;}

	public static int getDevWidth() {return DEV_WIDTH;}

	public static int getDevHeight() {return DEV_HEIGHT;}

	public static int getPlayWidth() {return PLAY_WIDTH;}

	public static int getPlayHeight() {return PLAY_HEIGHT;}

}
