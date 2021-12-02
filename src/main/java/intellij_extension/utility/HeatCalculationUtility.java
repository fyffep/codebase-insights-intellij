package intellij_extension.utility;

import com.intellij.vcs.log.Hash;
import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import javafx.scene.paint.Color;

import java.util.*;

public class HeatCalculationUtility
{
    private HeatCalculationUtility() {
        //This is a utility class
    }


    /**
     * Converts the input heat level to a color.
     * Higher heat levels are indicated by higher intensities of red.
     * @param heatLevel a number from 1 to 10
     * @return a hexadecimal String of the form "FFFFFF" representing a color
     */
    public static Color colorOfHeat(int heatLevel) {
        // Get percentage
        float heatPercentage = heatLevel / ((float) Constants.HEAT_MAX);

        // Get color based on percentage 0 = completely BLUE 1 = Completely RED
        Color heatColor = Constants.HEAT_MIN_COLOR.interpolate(Constants.HEAT_MAX_COLOR, heatPercentage);

        return heatColor;
    }



    public static void assignHeatLevelsFileSize(Codebase codebase)
    {
        System.out.println("Calculating heat based on file size...");
        final int REQUIRED_NUM_COMMITS_WITHOUT_CHANGING = 5; //the number of consecutive commits where no increase in a file's size is recorded needed in order to reduce the accumulated heat level.
        final int REQUIRED_SIZE_CHANGE = 200;
        final int SIZE_INCREASE_HEAT_CONSEQUENCE = 2; //how much the heat increases when the file size increases
        final int SIZE_DECREASE_HEAT_CONSEQUENCE = -1; //how much the heat decreases when the file size decreases
        final int SIZE_NO_CHANGE_HEAT_CONSEQUENCE = -1; //how much the heat decreases if the file size stays the same for long enough

        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            //The oldest commits are at the front of the LinkedHashMap
            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();

            HeatObject lastHeatObject = null;
            int numberOfConsecutiveCommitsWithNoSizeIncrease = 0;

            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                HeatObject newerHeatObject = commitToHeatObjectEntry.getValue();
                if (lastHeatObject != null)
                {
                    newerHeatObject.setHeatLevel(lastHeatObject.getHeatLevel()); //use previous heat, then modify

                    //If the file size increased at all, incur 2 heat
                    long oldFileSize = lastHeatObject.getFileSize();
                    long newFileSize = newerHeatObject.getFileSize();
/*if (fileObject.getFilename().equals("HeatMapPane.java"))
{
    System.out.println("New Hash: "+commitToHeatObjectEntry.getKey());
    System.out.println("oldFileSize: "+oldFileSize);
    System.out.println("newFileSize: "+newFileSize);
}*/
                    if (newFileSize > oldFileSize)
                    {
                        newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + SIZE_INCREASE_HEAT_CONSEQUENCE);
                        numberOfConsecutiveCommitsWithNoSizeIncrease = 0;
                    }
                    //File size decrease -> lose 1 heat
                    else if (oldFileSize - newFileSize >= REQUIRED_SIZE_CHANGE)
                    {
                        newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + SIZE_DECREASE_HEAT_CONSEQUENCE);
                        numberOfConsecutiveCommitsWithNoSizeIncrease++;
                    }
                    //File size stayed equal ↓
                    else
                    {
                        numberOfConsecutiveCommitsWithNoSizeIncrease++;

                        //If file went unchanged for long enough, the heat improved
                        if (numberOfConsecutiveCommitsWithNoSizeIncrease >= REQUIRED_NUM_COMMITS_WITHOUT_CHANGING)
                        {
                            newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + SIZE_NO_CHANGE_HEAT_CONSEQUENCE);
                            numberOfConsecutiveCommitsWithNoSizeIncrease = 0;
                        }
                    }
                }
                else
                {
                    newerHeatObject.setHeatLevel(Constants.HEAT_MIN); //No in/decreases in file size yet
                }
//if (fileObject.getFilename().equals("HeatMapPane.java"))
                //System.out.println("Heat: "+newerHeatObject.getHeatLevel()+"\n");

                lastHeatObject = newerHeatObject;
            }
        }
        System.out.println("Finished calculating heat based on file size.");
    }


    public static void assignHeatLevelsNumberOfCommits(Codebase codebase)
    {
        System.out.println("Calculating heat based on number of commits...");
        final int REQUIRED_NUM_COMMITS_WITHOUT_CHANGING = 5; //the number of consecutive commits where the file is not modified in order to reduce the accumulated heat level.
        final int COMMIT_HEAT_CONSEQUENCE = 2; //how much the heat increases when the file is modified
        final int COMMIT_ABSENCE_HEAT_CONSEQUENCE = -1; //how much the heat decreases if the file is not modified for enough consecutive commits

        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            //The oldest commits are at the front of the LinkedHashMap
            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();

            HeatObject lastHeatObject = null;
            int numberOfConsecutiveCommitsWithNoModify = 0;

            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                HeatObject newerHeatObject = commitToHeatObjectEntry.getValue();
                if (lastHeatObject != null)
                {
                    newerHeatObject.setHeatLevel(lastHeatObject.getHeatLevel()); //use previous heat, then modify

/*if (fileObject.getFilename().equals("HeatMapPane.java"))
{
    System.out.println("New Hash: "+commitToHeatObjectEntry.getKey());
    System.out.println("oldFileSize: "+oldFileSize);
    System.out.println("newFileSize: "+newFileSize);
}*/

                    //If the file was committed to, incur heat
                    if (newerHeatObject.getNumberOfCommits() > lastHeatObject.getNumberOfCommits())
                    {
                        newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + COMMIT_HEAT_CONSEQUENCE);
                        numberOfConsecutiveCommitsWithNoModify = 0;
                    }
                    //File was not touched in the commit ↓
                    else
                    {
                        numberOfConsecutiveCommitsWithNoModify++;

                        //If file went unchanged for long enough, the heat improved
                        if (numberOfConsecutiveCommitsWithNoModify >= REQUIRED_NUM_COMMITS_WITHOUT_CHANGING)
                        {
                            newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + COMMIT_ABSENCE_HEAT_CONSEQUENCE);
                            numberOfConsecutiveCommitsWithNoModify = 0;
                        }
                    }
                }
                else
                {
                    newerHeatObject.setHeatLevel(Constants.HEAT_MIN); //No commits to the file yet
                }
//if (fileObject.getFilename().equals("HeatMapPane.java"))
                //System.out.println("Heat: "+newerHeatObject.getHeatLevel()+"\n");

                lastHeatObject = newerHeatObject;
            }
        }
        System.out.println("Finished calculating heat based on number of commits.");
    }


    public static void OLD_assignHeatLevelsNumberOfAuthors(Codebase codebase)
    {
        System.out.println("Calculating heat based on number of authors...");
        /*/final int NEW_AUTHOR_HEAT_CONSEQUENCE = 2; //how much the heat increases when another author joins
        //final int GREATER_THAN_AVERAGE_HEAT_CONSEQUENCE = 1; //how much heat increases for every 0.5 authors more than the average
        final float REQUIRED_DIFFERENCE_FROM_AVERAGE = 0.5f;
        final int DEFAULT_HEAT = 5;*/

        //Determine the average, total, and maximum number of authors.
        //Note that these are localized per commit
        HashMap<String, Integer> totalAuthorsMap = new HashMap<>(); //maps commit hash to total number of authors (includes duplicate authors)

        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            //The oldest commits are at the front of the LinkedHashMap
            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();
            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                //Add the number of authors from the file to the total number of authors on that commit
                HeatObject heatObject = commitToHeatObjectEntry.getValue();
                int authorCount = heatObject.getNumberOfAuthors();
                assert authorCount > 0;
                totalAuthorsMap.merge(commitToHeatObjectEntry.getKey(), authorCount, Integer::sum);
            }

            /*/Select latest commit only
            String latestCommitHash = codebase.getLatestCommitHash();
            HeatObject heatObject = commitHashToHeatObjectMap.get(latestCommitHash);

            int authorCount = heatObject.getNumberOfAuthors();
            assert authorCount > 0;
            totalAuthors += authorCount;

            if (authorCount > maxAuthors)
                maxAuthors = authorCount;*/
        }

        //final int fileCount = fileObjectSet.size();
        /*final HashMap<String, Float> averageAuthorsMap = new HashMap<>(); //maps commit hash to avg. number of authors across all files in that commit
        for (Map.Entry<String, Integer> commitToAuthorCountEntry : totalAuthorsMap.entrySet())
        {
            String commitHash = commitToAuthorCountEntry.getKey();
            final int fileCount = codebase.getCommitFromCommitHash(commitHash).getFileSet().size();
            final float averageAuthors = ((float) commitToAuthorCountEntry.getValue()) / fileCount;
            assert  fileCount > 0;
            averageAuthorsMap.put(commitHash, averageAuthors);
            System.out.println("Avg for commit "+commitHash+" is: "+averageAuthors);
        }*/

        //Merge all author names into one collection to determine total number of authors
        Set<String> allAuthorSet = new LinkedHashSet<>();
        for (FileObject fileObject : fileObjectSet)
        {
            allAuthorSet.addAll(fileObject.getUniqueAuthors());
        }
        final double totalAuthorCount = allAuthorSet.size();


        //Assign heat level to every HeatObject based on number of authors
        for (FileObject fileObject : fileObjectSet)
        {
            //The oldest commits are at the front of the LinkedHashMap
            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();

            //HeatObject lastHeatObject = null;

            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                HeatObject newerHeatObject = commitToHeatObjectEntry.getValue();
                /*if (lastHeatObject != null)
                {
                    //For every 0.5 authors (REQUIRED_DIFFERENCE_FROM_AVERAGE), incur 1 heat
                    /*float averageAtCommit = averageAuthorsMap.get(commitToHeatObjectEntry.getKey());
                    int heatConsequence = (int) Math.ceil(
                            (newerHeatObject.getNumberOfAuthors() - averageAtCommit) / REQUIRED_DIFFERENCE_FROM_AVERAGE);
                    newerHeatObject.setHeatLevel(Constants.HEAT_MIN + heatConsequence);*
                }
                else
                {
                    newerHeatObject.setHeatLevel(Constants.HEAT_MIN); //Only 1 author so far
                }*/

                int heatLevel = (int) Math.round((newerHeatObject.getNumberOfAuthors() / totalAuthorCount) * Constants.HEAT_MAX);
                newerHeatObject.setHeatLevel(heatLevel);

                //lastHeatObject = newerHeatObject;
            }
        }
        System.out.println("Finished calculating heat based on number of authors.");
    }



    public static void assignHeatLevelsNumberOfAuthors(Codebase codebase)
    {
        System.out.println("Calculating heat based on number of authors...");
        final int REQUIRED_NUM_COMMITS_WITH_AUTHOR_ABSENCE = 10; //how many consecutive commits another author must make to a file before a particular author can be considered absent
        final int NEW_AUTHOR_HEAT_CONSEQUENCE = 2; //how much the heat increases when another author joins
        final int AUTHOR_ABSENCE_HEAT_CONSEQUENCE = -1; //how much the heat decreases when an author is absent for long enough

        //Assign heat level to every HeatObject based on number of authors
        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            HashMap<String, Integer> activeAuthors = new HashMap<>(); //the emails of which authors have been committing to the file recently
            //...and how many commits they have pushed recently

            //The oldest commits are at the front of the LinkedHashMap
            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();
            HeatObject lastHeatObject = null;

            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                HeatObject newerHeatObject = commitToHeatObjectEntry.getValue();
                int heatLevel = Constants.HEAT_MIN;
                if (lastHeatObject != null) {
                    heatLevel = lastHeatObject.getHeatLevel();
                }

                //Get the author of the commit
                String commitHash = commitToHeatObjectEntry.getKey();
                String authorEmail = codebase.getCommitFromCommitHash(commitHash).getAuthorEmail();

                //Returning author
                if (activeAuthors.containsKey(authorEmail))
                {
                    //Add the following: 1 to mark this current commit
                    //..and 1 to reverse the subtraction step below that affects all authors, including this one.
                    int newNumberOfCommits = activeAuthors.get(authorEmail) + 1 + 1;
                    activeAuthors.put(authorEmail, newNumberOfCommits);
                }
                //New author -> incur a hefty penalty
                else
                {
                    //Add the following: REQUIRED_NUM_COMMITS_WITH_AUTHOR_ABSENCE to mark this current commit
                    //..and 1 to reverse the subtraction step below that affects all authors, including this one.
                    int newNumberOfCommits = activeAuthors.get(authorEmail) + REQUIRED_NUM_COMMITS_WITH_AUTHOR_ABSENCE + 1;
                    activeAuthors.put(authorEmail, newNumberOfCommits);
                }

                for (Map.Entry<String, Integer> authorEntry : activeAuthors.entrySet())
                {
                    //Decrement the number of recent commits for every author to indicate that they have not modified the file in this commit
                    int decrementedNumberOfCommits = authorEntry.getValue() - 1;
                    //If the value is now 0, remove the author and reduce the heat
                    if (decrementedNumberOfCommits <= 0) {
                        activeAuthors.remove(authorEntry.getKey());
                        heatLevel += AUTHOR_ABSENCE_HEAT_CONSEQUENCE;
                    }
                    else {
                        activeAuthors.put(authorEntry.getKey(), decrementedNumberOfCommits);
                    }
                }

                //Store the new heat level
                newerHeatObject.setHeatLevel(heatLevel);
                lastHeatObject = newerHeatObject;
            }
        }
        System.out.println("Finished calculating heat based on number of authors.");
    }




    public static void assignHeatLevelsOverall(Codebase codebase)
    {
        final float WEIGHT_FILE_SIZE = 0.2f;
        final float WEIGHT_NUM_COMMITS_NUM_OF_COMMITS = 0.4f;
        final float WEIGHT_NUM_OF_AUTHORS = 0.4f;
        /**
         * Create a map that records the sum of all heat levels from every metric.
         * After every call to an assignHeatLevels method, we must call sumHeatLevels(...) to record
         * the latest heat levels in this map.
         */
        HashMap<FileObject, HashMap<String, Float>> fileToCommitToHeatSumMap = new HashMap<>();

        assignHeatLevelsFileSize(codebase);
        sumHeatLevels(codebase, fileToCommitToHeatSumMap, WEIGHT_FILE_SIZE);

        assignHeatLevelsNumberOfCommits(codebase);
        sumHeatLevels(codebase, fileToCommitToHeatSumMap, WEIGHT_NUM_COMMITS_NUM_OF_COMMITS);

        assignHeatLevelsNumberOfAuthors(codebase);
        sumHeatLevels(codebase, fileToCommitToHeatSumMap, WEIGHT_NUM_OF_AUTHORS);

        //Add more metrics here if more are needed in the future...

        //Compute and store overall heat
        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            HashMap<String, Float> commitToHeatSumMap = fileToCommitToHeatSumMap.get(fileObject);

            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();
            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                //Retrieve heat sum
                String commitHash = commitToHeatObjectEntry.getKey();
                int heatSum = Math.round(commitToHeatSumMap.get(commitHash));

                //Store result in the HeatObject
                commitToHeatObjectEntry.getValue().setHeatLevel(heatSum);
            }
        }
    }

    /**
     * Helper method for assignHeatLevelsOverall that adds the heat levels from every HeatObject to the float
     * part of the given fileToCommitToHeatSumMap. That way, the HeatObjects can have their heatLevels recalculated
     * by other assignHeat methods.
     */
    private static void sumHeatLevels(Codebase codebase, HashMap<FileObject, HashMap<String, Float>> fileToCommitToHeatSumMap, float weight)
    {
        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            //Get or create map of commit hash to heat sum
            HashMap<String, Float> commitToHeatSumMap = fileToCommitToHeatSumMap.computeIfAbsent(fileObject, k -> new HashMap<>());

            /*HashMap<String, Float> commitToHeatSumMap = fileToCommitToHeatSumMap.get(fileObject);
            if (commitToHeatSumMap == null)
            {
                commitToHeatSumMap = new HashMap<>();
                fileToCommitToHeatSumMap.put(fileObject, commitToHeatSumMap);
            }*/

            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();
            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                int heatLevel = commitToHeatObjectEntry.getValue().getHeatLevel();
                float weightedHeat = heatLevel * weight;
                String commitHash = commitToHeatObjectEntry.getKey();
                commitToHeatSumMap.merge(commitHash, weightedHeat, Float::sum);
            }
        }
    }


    public static void assignHeatLevels(Codebase codebase, Constants.HeatMetricOptions heatMetricOption)
    {
        switch (heatMetricOption)
        {
            case OVERALL:
                assignHeatLevelsOverall(codebase);
                break;
            case FILE_SIZE:
                assignHeatLevelsFileSize(codebase);
                break;
            case NUM_OF_COMMITS:
                assignHeatLevelsNumberOfCommits(codebase);
                break;
            case NUM_OF_AUTHORS:
                assignHeatLevelsNumberOfAuthors(codebase);
                break;
            default:
                throw new UnsupportedOperationException("Invalid heat metric selected in HeatCalculationUtility.assignHeatLevels(...)");
        }
    }
}
