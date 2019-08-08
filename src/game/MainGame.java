package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entites.Camera;
import entites.Entity;
import entites.Light;
import entites.Sky;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGame {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("dragon_solid")));
		ModelTexture texture = texturedModel.getTexture();
		texture.setReflectivity(1f);
		texture.setShineDamper(6f);
		
		RawModel treeModel = OBJLoader.loadObjModel("tree_02", loader);
		TexturedModel treeTexturedModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("wood_02")));
		ModelTexture wood1 = treeTexturedModel.getTexture();
		wood1.setReflectivity(0f);
		
		RawModel grassModel = OBJLoader.loadObjModel("grass_patch_01", loader);
		TexturedModel grassTexturedModel = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("billboard_grass_03")));
		ModelTexture grass1 = grassTexturedModel.getTexture();
		grass1.setHasTransparency(true);
		grass1.setReflectivity(0.5f);
		grass1.setShineDamper(70f);
		grass1.setFakeLighting(true);
		
		Entity dragon = new Entity(texturedModel, new Vector3f(0f, 0f, -10f), 0, 0, 0, 0.18f);
		Terrain rockyTerrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("desert_rocky")));
		Terrain grassyTerrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("dirt_01")));
		Light light = new Light(new Vector3f(0,1000,0), new Vector3f(1.0f, 1.0f, 1.0f));
		Camera camera = new Camera(new Vector3f(0.0f, 1.8f, 0.0f), 16);
		
		// GUI
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture controlsHelp = new GuiTexture(loader.loadTexture("controls_01"), new Vector2f(-0.65f, -0.45f), new Vector2f(0.3f, 0.52f));
		guis.add(controlsHelp);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		// grass generator
		List<Entity> grassPatch = new ArrayList<Entity>();
		Random random = new Random();
		float grassHeight = 0.5f;
		for(int i=0; i<12000; i++){
			grassHeight = (random.nextFloat() * 8 / 10 + 1.1f);
			grassPatch.add(new Entity(grassTexturedModel, new Vector3f(random.nextFloat()*100-50, grassHeight/2, -random.nextFloat()*100+50), 
					random.nextFloat()*10-5, random.nextFloat()*360, random.nextFloat()*10-5, grassHeight));
		}
		
		// tree generator
		List<Entity> trees = new ArrayList<Entity>();
		Random random2 = new Random();
		for(int i=0; i<3000; i++){
			trees.add(new Entity(treeTexturedModel, new Vector3f(random2.nextFloat()*500-250, 0, random2.nextFloat()*500-250), 
					random2.nextFloat()*10-5, random2.nextFloat()*360, random2.nextFloat()*10-5, 1f));
		}
		
		// Sky
		Sky clearSky = new Sky(0.53f, 0.81f, 0.92f);
		Sky sandySky = new Sky(0.87f, 0.7f, 0.51f);
		
		boolean rotating = false;
		boolean grassy = true;
		int framesSinceFlip = 0;
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			
			//game logic
			framesSinceFlip++;
			if(Keyboard.isKeyDown(Keyboard.KEY_F)){
				if(framesSinceFlip > 30){
					framesSinceFlip = 0;
					if(grassy){
						grassy = false;
					}else{
						grassy = true;
					}
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
				rotating = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_BACK)){
				rotating = false;
			}
			if (rotating){
				dragon.increaseRotation(0.0f, 1.0f, 0.0f);
			}
			dragon.increasePosition(0.0f, 0.0f, 0.0f);
			camera.move();
			
			//graphics
			renderer.processEntity(dragon);
			if(grassy){
				for(Entity grass:grassPatch){
					renderer.processEntity(grass);
				}
				renderer.processTerrain(grassyTerrain);
				renderer.render(light, camera, clearSky);
			}else{
				for(Entity tree:trees){
					renderer.processEntity(tree);
				}
				renderer.processTerrain(rockyTerrain);
				renderer.render(light, camera, sandySky);
			}
			
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
			
		}
		
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
