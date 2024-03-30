package com.nfw;
import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenSizeHelper {
	public int[] ScreenBounds() {
		// Get the screen resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		int positionX = screenWidth / 4;
		int positionY = screenHeight / 4;
		int sizeX = screenWidth / 2;
		int sizeY = screenHeight / 2;
		int[] screenBounds = { positionX, positionY, sizeX, sizeY };
		return screenBounds;
	}

}
