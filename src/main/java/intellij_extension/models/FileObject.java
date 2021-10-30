package intellij_extension.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
* filename
* filesize
* linecount
* no of commits 
* last edited date
* setpath 
*/
public class FileObject
{
	private String fileName;
	private long lineCount;
	private long fileSize;
	private int noOfCommits;
	private String filePath;
	private String folderPath;
	private int depth;

	public FileObject(String fileName, String folderPath,int depth)
	{
		this.fileName=fileName;
		this.folderPath=folderPath;
		setFilePath();
		this.depth=depth;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName=fileName;
	}
	public String getFileName()
	{
		return this.fileName;
	}
	
	public String getFolderPath()
	{
		return this.folderPath;
	}
	
	
	public void setFilePath()
	{
		this.filePath=getFolderPath()+"/"+getFileName();	
	}
	
	public String getFilePath()
	{
		return this.filePath;
	}
	
	public void setLineCount(long lineCount)
	{
		this.lineCount=lineCount;
	}
	
	public long getLineCount()
	{
		return this.lineCount;
	}
	
	
	public  void  computeLineCount() 
	{
		long lineCount=0;
		try
		{
			BufferedReader buffer=new BufferedReader(new FileReader(getFilePath()));
			
			while(buffer.readLine()!=null)
			{
				lineCount++;
			}
			buffer.close();
		}
		catch(Exception e)
		{
			System.out.println("Unable to read:"+ getFilePath());
			System.exit(0);
		}
	
		setLineCount(lineCount);
	}
	
	
	public void setFileSize(long length)
	{
		this.fileSize=length;
		
	}
	
	public long getFileSize()
	{
		return this.fileSize;
	}
	
	public  void computeFileSize() throws IOException
	{
		File file=new File(getFilePath());
		setFileSize(file.length());
	}

	

	public void parseFile() throws IOException 
	{
		computeLineCount();
		computeFileSize();
	}
	
	
	public void displayFileDetails()
	{
		String output=getFileName()+",--->FileSize : "+getFileSize()+", LineCount : "+getLineCount();
		System.out.println(String.format("%1$" + (output.length()+getDepth()) + "s", output));
	}

	public int getDepth() {
		
		return this.depth;
	}

	public int computeHeatLevel()
	{
		return (int) (Math.random() * 10); //TODO
	}
}
