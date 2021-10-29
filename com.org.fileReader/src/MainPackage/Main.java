package MainPackage;

import java.io.File;
import java.io.IOException;

import com.org.fileManager.RootDirectory;

public class Main {
	
	public static void main(String args[]) throws IOException 
	{
		RootDirectory root =new RootDirectory("src");
		root.parsedirectory();
		root.displayDetails();	
		
	}

}


