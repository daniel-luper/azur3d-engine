package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {
	
	public static RawModel loadObjModel(String fileName, Loader loader){
		FileReader fReader = null;
		try {
			fReader = new FileReader(new File("res/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}
		BufferedReader bReader = new BufferedReader(fReader);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] vArray = null;
		float[] tArray = null;
		float[] nArray = null;
		int[] iArray = null;
		try {
			while(true){
				line = bReader.readLine();
				String[] curLine = line.split(" ");
				if(line.startsWith("v ")){
					Vector3f vertex = new Vector3f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")){
					Vector2f texture = new Vector2f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")){
					Vector3f normal = new Vector3f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")){
					tArray = new float[vertices.size()*2];
					nArray = new float[vertices.size()*3];
					break;
				}
			}
			while(line!=null){
				if(!line.startsWith("f ")){
					line = bReader.readLine();
					continue;
				}
				String[] curLine = line.split(" ");
				String[] vertex1 = curLine[1].split("/");
				String[] vertex2 = curLine[2].split("/");
				String[] vertex3 = curLine[3].split("/");
				processVertex(vertex1, indices, vertices, textures, normals, tArray, nArray);
				processVertex(vertex2, indices, vertices, textures, normals, tArray, nArray);
				processVertex(vertex3, indices, vertices, textures, normals, tArray, nArray);
				line  = bReader.readLine();
			}
			bReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vArray = new float[vertices.size() * 3];
		iArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex:vertices){
			vArray[vertexPointer++] = vertex.x;
			vArray[vertexPointer++] = vertex.y;
			vArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++){
			iArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(vArray, tArray, nArray, iArray);
		
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, 
			float[] tArray, float[] nArray){
		int curVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(curVertexPointer);
		Vector2f curTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
		tArray[curVertexPointer * 2] = curTexture.x;
		tArray[curVertexPointer * 2 + 1] = 1 - curTexture.y;
		Vector3f curNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		nArray[curVertexPointer * 3] = curNorm.x;
		nArray[curVertexPointer * 3 + 1] = curNorm.y;
		nArray[curVertexPointer * 3 + 2] = curNorm.z;
	}
	
}
