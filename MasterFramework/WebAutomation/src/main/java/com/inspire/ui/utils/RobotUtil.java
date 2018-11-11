package com.inspire.ui.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * @author sachi
 *
 */
public class RobotUtil {
	Robot robot;

	public RobotUtil() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void pressControl() {
		robot.keyPress(KeyEvent.VK_CONTROL);
	}

	public void pressLeftKey() {
		robot.keyPress(KeyEvent.KEY_LOCATION_LEFT);
	}

	public void pressRightKey() {
		robot.keyPress(KeyEvent.KEY_LOCATION_RIGHT);
	}

	public void pressBackSpace() {
		robot.keyPress(KeyEvent.VK_BACK_SPACE);
	}

	public void pressShiftKey() {
		robot.keyPress(KeyEvent.VK_SHIFT);
	}

	public void pressBackSlash() {
		robot.keyPress(KeyEvent.VK_BACK_SLASH);
	}

	public void pressSpace() {
		robot.keyPress(KeyEvent.VK_SPACE);
	}

	public void pressCancel() {
		robot.keyPress(KeyEvent.VK_CANCEL);
	}

	public void pressCopy() {
		robot.keyPress(KeyEvent.VK_COPY);
	}

	public void pressPaste() {
		robot.keyPress(KeyEvent.VK_PASTE);
	}

	public void pressTab() {
		robot.keyPress(KeyEvent.VK_TAB);
	}

	public void pressPageUp() {
		robot.keyPress(KeyEvent.VK_PAGE_UP);
	}

	public void pressPageDown() {
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
	}

	public void pressClear() {
		robot.keyPress(KeyEvent.VK_CLEAR);
	}

	public int keyPressed() {
		return KeyEvent.KEY_PRESSED;
	}

}
