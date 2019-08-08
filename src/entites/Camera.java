package entites;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utils.MathTools;

import input.MouseManager;

public class Camera {
	
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private float pitch, yaw, roll;
	private Vector2f viewAngle;
	private Vector2f direction = new Vector2f(0, 0);
	private float defaultY;
	private boolean jumping = false;
	private boolean crouching = false;
	private static final float GRAVITY = 0.003f;
	private float velocity = 0.0f;
	private float speed;
	private MouseManager mouse = new MouseManager();
	
	public Camera(Vector3f position, float pitch){
		this.position = position;
		this.pitch = pitch;
		defaultY = position.y;
	}
	
	public void move(){
		// mouse movement
		viewAngle = mouse.look(yaw, pitch);
		yaw = viewAngle.x;
		pitch = viewAngle.y;
		
		// Jump
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			if (!jumping) {
				jumping = true;
				velocity = 0.1f;
			}
		}
		
		// Crouch
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			if (position.y >= 1.1f){
				crouching = true;
				position.y -= 0.05f;
				speed = 0.05f;
			}
		}else{
			if (position.y < defaultY){
				position.y += 0.05f;
			}else{
				crouching = false;
			}
		}
		
		// Jump
		if(jumping){
			updateJump();
		}else if (!crouching){
			position.y = defaultY;
		}
		
		// Sprint
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			speed = 0.15f;
		}else if (!crouching){
			speed = 0.08f;
		}
		
		// Move (forward/backward/strafe)
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			direction = MathTools.createVectorFromAngle(yaw, 0);
			position.z -= direction.x*speed;
			position.x += direction.y*speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){;
			direction = MathTools.createVectorFromAngle(yaw, -Math.PI/2);
			position.z -= direction.x*speed;
			position.x += direction.y*speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			direction = MathTools.createVectorFromAngle(yaw, Math.PI);
			position.z -= direction.x*speed;
			position.x += direction.y*speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			direction = MathTools.createVectorFromAngle(yaw, Math.PI/2);
			position.z -= direction.x*speed;
			position.x += direction.y*speed;
		}
		
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void updateJump() {
		position.y += velocity;
		velocity -= GRAVITY;
		if (position.y <= defaultY) {
			position.y = defaultY;
			jumping = false;
		}
	}

}
