package intellij_extension.models;

import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.utility.commithistory.CommitCountCalculator;
import intellij_extension.utility.filesize.FileSizeCalculator;

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
	private int numberOfCommits = 1;
	private String filePath;
	private String folderPath;
	private int depth;

	private int heatLevel = -1;

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
	
	public void setFileSize(long length)
	{
		this.fileSize=length;
	}
	
	public long getFileSize()
	{
		return this.fileSize;
	}

	public int getDepth() {

		return this.depth;
	}

	public int getNumberOfCommits() {
		return numberOfCommits;
	}

	public void setNumberOfCommits(int numberOfCommits) {
		this.numberOfCommits = numberOfCommits;
	}

	public void displayFileDetails()
	{
		String output=getFileName()+",--->FileSize : "+getFileSize()+", LineCount : "+getLineCount();
		System.out.println(String.format("%1$" + (output.length()+getDepth()) + "s", output));
	}

	/**
	 * Assigns a heat level to this FileObject based on its metrics.
	 * @return a value from 1 to 10, with 10 being the hottest
	 */
	public int computeHeatLevel() //maybe this class (a model) is not the best place for this method
	{
		//Currently, this does not support accumulated heat

		//Compute the heat of each metric
		//File size
		int sizeHeat = HeatCalculationUtility.calculateHeatForFileSize(this);
		//Number of commits
		int numberOfCommitsHeat = HeatCalculationUtility.calculateHeatForNumberOfCommits(this);

		//Average all the metrics
		this.heatLevel = Math.round(
				sizeHeat +
				numberOfCommitsHeat
				//Add more metrics here...
		) / 2;

		return this.heatLevel;
	}
}
