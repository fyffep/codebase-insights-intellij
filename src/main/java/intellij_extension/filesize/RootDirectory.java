package intellij_extension.filesize;

import intellij_extension.models.Commit;
import intellij_extension.models.FileObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Holds multiple folder/package
 */
	
public class RootDirectory {
	
	private String baseDir;
	private HashMap<String,Folder> folderMap;
	private HashMap<String, FileObject> fileMap;

	private long folderCount, fileCount;
	private int depth;
	
	//default constructor 		
	public RootDirectory()
	{
		this.baseDir="/src";
		folderMap =new HashMap<String,Folder>();
		fileMap=new HashMap<String,FileObject>();
		folderCount=0;
		fileCount=0;
		depth=0;
	}
	
	//constructor
	public RootDirectory(String baseDir)
	{
		this.baseDir=baseDir;
		folderMap =new HashMap<String,Folder>();
		fileMap=new HashMap<String,FileObject>();
		folderCount=0;
		fileCount=0;
		depth=0;
	}
	
	
	//set base directory path
	public  void setBaseDir(String sourcename)
	{
		 this.baseDir=sourcename;
	}
	
	//get base directory path
	public String getBaseDir()
	{
		return this.baseDir;	
	}
	
	
	// reads the base directory information and sets the folder and file objects
	public void parsedirectory() throws IOException
	{
		File directory=new File(getBaseDir());
		System.out.println("Parsing files from root dir `"+directory.getAbsolutePath()+"`");
		
		long folderCount=0;
		
		
		for( String folderObject : directory.list())
		{
			File verifyFolder=new File(getBaseDir()+"/"+folderObject);
			
			// folder search 
			if(verifyFolder.isDirectory())
			{
				
				Folder folder=new Folder(folderObject,getBaseDir(),getDepth()+1);
				folder.parseFolder();
				folderMap.put(folderObject, folder);
				folderCount++;
			
			}
			
			//file search
			else if (verifyFolder.isFile())
			{
				FileObject file=new FileObject(folderObject,getBaseDir(),getDepth()+1);
				file.parseFile();
				fileMap.put(folderObject,file);
				fileCount++;
			}
		}
		
		setFolderCount(folderCount);
		setFileCount(fileCount);
			
	}
	
	
	
	public  void setFileCount(long fileCount) 
	{
		this.fileCount=fileCount;
		
	}
	public long  getFileCount()
	{
		return this.fileCount;
	}

	

	public  void setFolderCount(long folderCount) 
	{
		this.folderCount=folderCount;	
	}
	
	public long  getFolderCount()
	{
		return this.folderCount;
	}
	
	// to store the depth of a folder with respect to root. Used for display purpose
	public int getDepth() {
		
		return this.depth;
	}
	

	public void displayDetails()
	{
		System.out.println(getBaseDir()+" Folder Count : "+getFolderCount());
		
		// display all folders within the base directory
		for( String foldername : folderMap.keySet())
		{
			Folder folder= folderMap.get(foldername);
			folder.displayDetails();
		}
		
		// display all files within the base directory 
		for( String filename : fileMap.keySet())
		{
			FileObject file=fileMap.get(filename);
			file.displayFileDetails();	
		}
		
	}

	/**
	 * Changes the HashMap passed to it so that FileObjects inside of it
	 * are either added (if they do not exist) or given more data.
	 * In this case, the FileObjects should be given file size data.
	 * @param existingFileMetricMap this holds all file data for one commit in the project
	 * @return the input map but with all FileObjects in the project given file size counts.
	 */
	public HashMap<String, FileObject> editFileMetricMap(HashMap<String, FileObject> existingFileMetricMap)
	{
		//Place the FileObjects from this directory into the map
		for(String filename : fileMap.keySet())
		{
			//Merge the existing data (if it exists) with the newly computed data
			FileObject existingData = existingFileMetricMap.get(filename); //what was passed in as a param
			FileObject fileSizeData = fileMap.get(filename); //what this class computed
			if (existingData == null)
				existingData = fileSizeData;
			existingData.setFileSize(fileSizeData.getFileSize());
			existingData.setLineCount(fileSizeData.getLineCount());

			existingFileMetricMap.put(filename, existingData);
			System.out.println("Mapped file "+filename);
		}

		//Recur on the sub-directories
		for( String folderPath : folderMap.keySet())
		{
			Folder folder = folderMap.get(folderPath);
			folder.editFileMetricMap(existingFileMetricMap);
		}

		return existingFileMetricMap;
	}

	public static void main(String[] args) {
		//Compute file size
		//TODO We may need to have the user select the project root
		RootDirectory rootDirectory =new RootDirectory("C:\\Users\\Pete\\Desktop\\team3-project\\src\\main");
		try {
			rootDirectory.parsedirectory();
			rootDirectory.displayDetails();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Add the file size data to the map
		Commit activeCommit = new Commit();
		HashMap<String, FileObject> fileMetricMap = activeCommit.getFileMetricMap();
		rootDirectory.editFileMetricMap(fileMetricMap);
		System.out.println("Complete");
	}

}
