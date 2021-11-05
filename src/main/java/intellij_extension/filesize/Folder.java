package intellij_extension.filesize;

import intellij_extension.models.FileObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * holds files
 *folder name
 * file count
 *path 
 *folder size
 */

public class Folder {
	
	private String foldername;
	private HashMap<String, FileObject> fileMap;
	private HashMap<String,Folder> folderMap;
	private String folderpath;
	private long fileCount,folderCount;
	private int depth;

	//default constructor
	public Folder ()
	{
		this.foldername="";
		fileMap =new HashMap<String,FileObject>();
		folderMap =new HashMap<String,Folder>();

		fileCount=0;
		folderCount=0;
		depth=0;
		
	}
	
	//constructor
	public Folder(String foldername, String basedir,int depth) 
	{
		
		this.foldername=foldername;
		fileMap =new HashMap<String,FileObject>();
		setFolderPath(basedir);
		fileCount=0;
		folderMap =new HashMap<String,Folder>();
		folderCount=0;
		this.depth=depth;
	}

	//set folder name
	public void setFolder(String foldername)
	{
		this.foldername=foldername;
	}
	
	//get folder name
	public String getFolder() 
	{
		return this.foldername;
	}
	
	//set folder path
	public void setFolderPath(String basedir)
	{
		this.folderpath=basedir+"/"+getFolder();
	}
	
	
	//get the folder path
	public String getFolderPath()
	{
		return this.folderpath;
	}
	

	
	public void parseFolder() throws IOException 
	{
		File directory=new File(getFolderPath());
		long fileCount=0;
		for( String folderObject : directory.list())
		{
			File verifyobj=new File(getFolderPath()+"/"+folderObject);
			if (verifyobj.isFile())
			{
				FileObject file=new FileObject(folderObject,getFolderPath(),getDepth()+1);
				file.parseFile();
				fileMap.put(folderObject,file);
				fileCount++;
			}
			else if(verifyobj.isDirectory())
			{
				Folder folder=new Folder(folderObject,getFolderPath(),getDepth()+1);
				folder.parseFolder();
				folderMap.put(folderObject,folder);
				folderCount++;
			}
				
			
		}
		setFileCount(fileCount);
		setFolderCount(folderCount);
		
	}
	
	
	private int getDepth() 
	{
		
		return this.depth;
	}

	public  void setFileCount(long fileCount) 
	{
		this.fileCount=fileCount;
		
	}
	
	public long getFileCount()
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

	void displayDetails()
	{
		String output=getFolder()+"---> FileCount : "+getFileCount()+" Folder Count : "+getFolderCount();
		System.out.println(String.format("%1$" + (output.length()+getDepth()) + "s", output));
		for( String filename : fileMap.keySet())
		{
			FileObject file= fileMap.get(filename);
			file.displayFileDetails();	
		}
		
		for(String foldername: folderMap.keySet())
		{
			Folder folder= folderMap.get(foldername);
			folder.displayDetails();
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
}
