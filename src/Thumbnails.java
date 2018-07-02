import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//thread controlling KILL switch
class killClass extends Thread{
	public boolean RUNNING = true;
	public void run() {
		
		Object[] options = { "KILL" };
		JOptionPane.showOptionDialog(null, "The Fusion Thumbnail Scraper is RUNNING\nClick KILL to kill the program", "FTS",
		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		null, options, options[0]);
		
		RUNNING = false;
		
	}
	public boolean getIsRunning() {
		return RUNNING;
	}
}

public class Thumbnails {
	
	public static void main(String[] args) throws InterruptedException, IOException {
        
		//start KILL switch thread
		killClass killThread = new killClass();
		killThread.start();
		
		
		 
            //System.out.print("Enter the file name with extension : ");

            Scanner input = new Scanner(System.in);

            File file = new File("INPUT.txt");

            //determine number of tracks in the file
            input = new Scanner(file);
	        int trackCount = 0;
	        while (input.hasNextLine()) {
	            	String line = input.nextLine();
	                trackCount++;
	        }
	        input.close();
		 
		 
	            
	        //create arrays to hold track IDs and names
	        String[] IDList = new String[trackCount];
	        String[] trackList = new String[trackCount];
	        
	        int invalidCount = 0;
	        String invalidTracks = "";
	            
	    	try {	
	            //place all Track IDs and Names into their respective arrays
	            int currentID = 0;
	            input = new Scanner(file);
	            while (input.hasNextLine()) {
	                String line = input.nextLine();
	                for(int i=0; i<line.length(); i++) {
	                	if(line.charAt(i)==' ') {
	                		IDList[currentID] = line.substring(0, i);
	                		trackList[currentID] = line.substring(i+1);
	                		System.out.println("ID is " + IDList[currentID] + " and track name is " + trackList[currentID]);
	                		currentID++;
	                		break;
	                	}
	                }
	            }
	            input.close();

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		
		
		
		
	
		
		
		
		
		
		
		
        
		//initiliazing search and image load time values (milliseconds)
        int searchTimeMS = 3000;
        int imageLoadTimeMS = 3000;
        
        try {
        	        	
        	Robot robot = new Robot();
        	
        	robot.delay(5000);
     
        	int tracksProcessed=0;
        	//looping through each track in the tracklist
        	for(int iter=0; iter<trackList.length && killThread.getIsRunning(); iter++) {
        		
        		
        		
        		String trackName = trackList[iter];
            	
        		if(killThread.getIsRunning()) {
        			typeString(robot,trackName);
        		}
            	
            	if(killThread.getIsRunning()) {
            		searchTrack(robot,searchTimeMS);
            	}
            	
            	if(killThread.getIsRunning()) {
            		robot.delay(imageLoadTimeMS);
            	}
            	if(killThread.getIsRunning()) {
            		screenCap(robot,IDList[iter]);
            		
            	
            		if(isCapValid(IDList[iter])){
            			returnToSearch(robot);
            		}
            		else {
            			returnToSearchINVALID(robot);
            			invalidCount++;
            			invalidTracks = invalidTracks + ", '" + trackList[iter] + "'";
            		}
            			
            		
            		backSpace(robot, trackName.length());
            		robot.delay(200);
            		tracksProcessed++;
            	}
            	
            	
            	
        	}
        	
        	killThread.interrupt();
        	
        	if(invalidCount==0) {
        		invalidTracks = "  None";
        	}
        	
        	System.out.println("\n Invalid Tracks: " + invalidTracks);

        	Object[] options = { "OK" };
    		JOptionPane.showOptionDialog(null, "Thumbnail Scraping HALTED \n"
    				+ " ______________________________ \n"
    				+ "Tracks submitted:    " + trackList.length + "\n"
    				+ "Tracks processed:   " + tracksProcessed + "\n"
    				+ " ______________________________ \n"
    				+ "Invalid Tracks: " + invalidCount + " (" + invalidTracks.substring(2)+ ")", "FTS",
    		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
    		null, options, options[0]);
        	
    		
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	System.out.println("\n --- DONE ---");
        }
        catch(AWTException e) {
        	e.printStackTrace();
        }
    }
	
	
	
	//type out each character of track name using robot
	public static void typeString(Robot robot, String str) {
		
		System.out.println(str);
		
		for(int i=0; i<str.length(); i++) {
			char character = str.charAt(i);
			typeChar(robot,character);
		}	
	}
	
	//parse character for correct key press(es)
	public static void typeChar(Robot robot, char chr) {		

		//System.out.println("Testing char: " + chr);
		
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
		else if(chr==',') {
			robot.keyPress(KeyEvent.VK_COMMA);
			robot.keyRelease(KeyEvent.VK_COMMA);}
		else if(chr=='.') {
			robot.keyPress(KeyEvent.VK_PERIOD);
			robot.keyRelease(KeyEvent.VK_PERIOD);}
		else if(chr==';') {
			robot.keyPress(KeyEvent.VK_SEMICOLON);
			robot.keyRelease(KeyEvent.VK_SEMICOLON);}
		else if(chr=='\'') {
			robot.keyPress(KeyEvent.VK_QUOTE);
			robot.keyRelease(KeyEvent.VK_QUOTE);}
		
		
		
		
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
	
	//press backspace a certain number of times in succession
	public static void backSpace(Robot robot, int num) {
		for (int i=0; i<num; i++) {
			robot.keyPress(KeyEvent.VK_BACK_SPACE);
			robot.keyRelease(KeyEvent.VK_BACK_SPACE);	
		}
	}
	
	//Take screenCap at certain portion of screen
	public static void screenCap(Robot robot, String index) {
		try {
			String format = "jpg";
			String fileName = "OUTPUT/" + index + "." + format;
    	
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle captureRect = new Rectangle(180, 410, 430, 270);
			BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
			ImageIO.write(screenFullImage, format, new File(fileName));
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	//Search track, wait a load time, then open track details
	public static void searchTrack(Robot robot, int searchTimeMS) {
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_RIGHT);
		robot.keyRelease(KeyEvent.VK_RIGHT);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(searchTimeMS);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	
	//return to search bar from track details
	public static void returnToSearch(Robot robot) {
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);
		robot.delay(700);
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_LEFT);
		robot.delay(75);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(75);
	}
	
	public static void returnToSearchINVALID(Robot robot){
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_LEFT);
		robot.delay(75);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(75);
	}
	
	public static boolean isCapValid(String ID) throws IOException {
		
		File file2= new File("OUTPUT/"+ID+".jpg");
		BufferedImage image = ImageIO.read(file2);
		  
		// Getting pixel color by position x and y 
		//int clr=  image.getRGB(100,200);
		int[] Points = {image.getRGB(100,100),image.getRGB(300,100),image.getRGB(300,200)};
		int[][] Colors = new int[3][3];
		  
		for(int i=0; i<3; i++) {
			Colors[i][0] = (Points[i] & 0x00ff0000) >> 16;
			Colors[i][1] = (Points[i] & 0x0000ff00) >> 8;
			Colors[i][2] = Points[i] & 0x000000ff;
			
			System.out.println("RGB for Point " + i + ": (" + Colors[i][0] + "," + Colors[i][1] + "," + Colors[i][2] + ")");
		}
		
		if(Colors[0][0]==Colors[1][0]&&Colors[0][0]==Colors[2][0]&&Colors[0][1]==Colors[1][1]&&Colors[0][1]==Colors[2][1]&&Colors[0][2]==Colors[1][2]&&Colors[0][2]==Colors[2][2]) {
			return false;
		}
		  
		return true;
	}
}

