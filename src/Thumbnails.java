import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;


public class Thumbnails {
	
	public static void main(String[] args) throws InterruptedException {
        // Prints "Hello, World" to the terminal window.
        System.out.println("TEST");
        
        try {
        	        	
        	Robot robot = new Robot();
        	
        	TimeUnit.SECONDS.sleep(3);
        	
        	
        	String trackName = "Oso -= !@ -= Pig!";
        	
        	typeString(robot,trackName);
        	
        	TimeUnit.SECONDS.sleep(3);
        	
        	backSpace(robot, trackName.length());
        	
        	
        	
        	
        	
        	System.out.println("\n --- DONE ---");
        }
        catch(AWTException e) {
        	e.printStackTrace();
        }
    }
	
	
	
	
	public static void typeString(Robot robot, String str) {
		
		System.out.println(str);
		
		for(int i=0; i<str.length(); i++) {
			char character = str.charAt(i);
			typeChar(robot,character);
		}
		
		
		
		robot.keyPress(KeyEvent.VK_ENTER);
		
		
	}
	
	public static void typeChar(Robot robot, char chr) {		

		System.out.println("Testing char: " + chr);
		
		//Lowercase, uppercase, and digits
		
		if(Character.isLowerCase(chr)) {
			char chrUpper = Character.toUpperCase(chr);
			robot.keyPress(chrUpper);
			robot.keyRelease(chrUpper);
		}
		else if(Character.isUpperCase(chr)) {
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(chr);
			robot.keyRelease(chr);
			robot.keyRelease(KeyEvent.VK_SHIFT);			
		}
		else if(Character.isDigit(chr)) {
			robot.keyPress(chr);
			robot.keyRelease(chr);
		}
		
		//NON-SHIFT required special characters	
		
		else if(chr==' ') {
			robot.keyPress(KeyEvent.VK_SPACE);
			robot.keyRelease(KeyEvent.VK_SPACE);}
		else if(chr=='-') {
			robot.keyPress(KeyEvent.VK_MINUS);
			robot.keyRelease(KeyEvent.VK_MINUS);}
		else if(chr=='=') {
			robot.keyPress(KeyEvent.VK_EQUALS);
			robot.keyRelease(KeyEvent.VK_EQUALS);}
		
		
		
		//SHIFT required special characters
		
		else if(chr=='!'||chr=='@') {
			
			robot.keyPress(KeyEvent.VK_SHIFT);
			
			if(chr=='!') {
				robot.keyPress(KeyEvent.VK_1);
				robot.keyRelease(KeyEvent.VK_1);
			}
			else if(chr=='@') {
				robot.keyPress(KeyEvent.VK_2);
				robot.keyRelease(KeyEvent.VK_2);
			}
			
			robot.keyRelease(KeyEvent.VK_SHIFT);}	
	}
	
	public static void backSpace(Robot robot, int num) {
		for (int i=0; i<num; i++) {
			robot.keyPress(KeyEvent.VK_BACK_SPACE);
			robot.keyRelease(KeyEvent.VK_BACK_SPACE);	
		}
	}
	
}