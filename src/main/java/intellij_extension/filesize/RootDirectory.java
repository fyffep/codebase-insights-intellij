package intellij_extension.filesize;

import intellij_extension.models.FileObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Holds multiple folder/package
 */
	
public class RootDirectory {
	
	private String baseDir;
	private HashMap<String,Folder> folderList;
	private HashMap<String, FileObject> fileList;

	private long folderCount, fileCount;
	private int depth;
	
	//default constructor 		
	public RootDirectory()
	{
		this.baseDir="/src";
		folderList=new HashMap<String,Folder>();
		fileList=new HashMap<String,FileObject>();
		folderCount=0;
		fileCount=0;
		depth=0;
	}
	
	//constructor
	public RootDirectory(String baseDir)
	{
		this.baseDir=baseDir;
		folderList=new HashMap<String,Folder>();
		fileList=new HashMap<String,FileObject>();
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
	public  void parsedirectory() throws IOException
	{
		File directory=new File(getBaseDir());
		
		long folderCount=0;
		
		
		for( String folderObject : directory.list())
		{
			File verifyFolder=new File(getBaseDir()+"/"+folderObject);
			
			// folder search 
			if(verifyFolder.isDirectory())
			{
				
				Folder folder=new Folder(folderObject,getBaseDir(),getDepth()+1);
				folder.parseFolder();
				folderList.put(folderObject, folder);
				folderCount++;
			
			}
			
			//file search
			else if (verifyFolder.isFile())
			{
				FileObject file=new FileObject(folderObject,getBaseDir(),getDepth()+1);
				file.parseFile();
				fileList.put(folderObject,file);
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
		for( String foldername : folderList.keySet())
		{
			Folder folder=folderList.get(foldername);
			folder.displayDetails();
			
		}
		
		// display all files within the base directory 
		for( String filename : fileList.keySet())
		{
			FileObject file=fileList.get(filename);
			file.displayFileDetails();	
		}
		
	}
	
}
