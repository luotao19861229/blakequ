package com.stickycoding.rokon;

import java.util.ArrayList;

import com.stickycoding.rokon.vbo.VBO;

/**
 * VBOManager.java
 * Handles VBOs, much in the same way that TextureManager does. It keeps track of the ones active on the hardware, and reloads them if necessary.
 * 		eg, when coming back from Home button press
 *  VBO������
 * @author Richard
 */

public class VBOManager {
	
	protected static ArrayList<VBO> list = new ArrayList<VBO>();
	
	/**
	 * Adds a VBO to the list. When the list needs to be refreshed, the VBO will be informed that it is no longer on the hardware.
	 * 
	 * @param vbo valid VBO object
	 */
	public static void add(VBO vbo) {
		Debug.print("VBO loaded");
		list.add(vbo);
	}
	
	protected static void removeVBOs() {
		Debug.warning("Removing VBOs");
		for(int i = 0; i < list.size(); i++) {
			((VBO)list.get(i)).setUnloaded();
		}
		list.clear();
	}

}
