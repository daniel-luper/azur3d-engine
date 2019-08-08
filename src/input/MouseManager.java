package input;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class MouseManager {
	
	private int speed = 1;
	private int maxLookUp = 85;
	private int maxLookDown = -85;
	
	public MouseManager(){
		Mouse.setGrabbed(true);
	}
	
	public Vector2f look(float yaw, float pitch){
        float mouseDX = Mouse.getDX() * speed * 0.16f;
        float mouseDY = Mouse.getDY() * speed * 0.16f;
            
        if (yaw + mouseDX >= 360) {yaw = yaw + mouseDX - 360;} 
        else if (yaw + mouseDX < 0) {yaw = 360 - yaw + mouseDX;}
        else {yaw += mouseDX;}
        if (pitch - mouseDY >= maxLookDown && pitch - mouseDY <= maxLookUp) {pitch += -mouseDY;}
        else if (pitch - mouseDY < maxLookDown) {pitch = maxLookDown;}
        else if (pitch - mouseDY > maxLookUp) {pitch = maxLookUp;}
        
        return (new Vector2f(yaw, pitch));
    }
		
}
