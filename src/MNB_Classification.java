import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is used for
 * (i) training an MNB and
 * (ii) classifying unlabeled documents using the trained MNB.
 * Created by liukaichi on 6/7/2016.
 */

public class MNB_Classification
{

    MNB_Probability mnb_probability = new MNB_Probability();

    public MNB_Classification()
    {
        PorterStemmer stemmer = new PorterStemmer();
        CollectionSet trainingSet = new CollectionSet();
        Map<String, Integer> classes; //class name to documentNumber;
        CollectionSet testSet = new CollectionSet();


        //I need a testSet list too

        String folderPath = "testDocs";
        try
        {
            File folder = new File(folderPath);
            if (folder.isDirectory())
            {
                int totalTrainingDocumentCount = 0;
                int randomCounter = 0;
                for (File classFolder : folder.listFiles())
                {

                    int documentCount = 0; //it's not always the same as number of files, because some files are empty

                    File classDocuments[] = classFolder.listFiles();
                    Collections.shuffle(Arrays.asList(classDocuments));

                    for (File document : classDocuments)
                    {
                        CollectionSet currentSet;
                        //if (randomCounter == 4) currentSet = testSet;
                        //else currentSet = trainingSet;
                        currentSet = trainingSet;

                        DocumentInfo documentInfo = new DocumentInfo();
                        documentInfo.className = classFolder.getName();
                        documentInfo.name = document.getName();
                        //scan the document
                        Scanner inScanner = new Scanner(new FileReader(document));
                        inScanner.useDelimiter("\n\n");
                        inScanner.next();

                        String domainPattern = "[a-z0-9\\-\\.]+\\.(com|org|net|mil|edu|(co\\.[a-z].))";
                        Pattern pFind = Pattern.compile(domainPattern);

                        boolean isEmpty = true;
                        while (inScanner.hasNextLine())
                        {
                            String str = inScanner.nextLine().toLowerCase();
                            if (pFind.matcher(str).find() == false)
                            {
                                for (String word : str.split("\\s"))
                                {
                                    if (!word.equals(""))
                                    {
                                        isEmpty = false;
                                        currentSet.addWord(stemmer.stem(word), documentInfo);
                                    }
                                }
                            }
                        }
                        // TODO: this should only increment with non-empty documents.
                        if (!isEmpty)
                        {
                            documentCount++;
                            currentSet.totalDocumentCount++;
                            if (randomCounter == 4) randomCounter = 0;
                            else randomCounter++;
                        }
                    }
                    trainingSet.getClassInfos().put(classFolder.getName(), documentCount);
                }
            }
            featureSelection(trainingSet, 10);

        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
    /**
     * This method determines the words which should be chosen to represent
     * documents in the training set and test set based on the information gain (IG)
     * of each vocabulary term w
     */
    /**
     * @param trainingSet
     * @param numWordsinVocab must be >= 1. If M = |V|, then NO feature selection is applied
     */
    public ArrayList<Word> featureSelection(CollectionSet trainingSet, int numWordsinVocab)
    {
        if (trainingSet.wordsToDocNameToClassName.size() == numWordsinVocab) return null;
        if (trainingSet.wordsToDocNameToClassName
                .size() < numWordsinVocab) numWordsinVocab = trainingSet.wordsToDocNameToClassName.size();
        ArrayList<Word> allWords = new ArrayList<>();

        for (String word : trainingSet.wordsToDocNameToClassName.keySet())
        {
            double pc = 0;
            double pw = trainingSet.wordsToDocNameToClassName.get(word).size() / trainingSet.totalDocumentCount;
            double pnotw= (trainingSet.wordsToDocNameToClassName.get(word).size() - trainingSet.totalDocumentCount) / trainingSet.totalDocumentCount;
            double pcgivenw= 0;
            double pcwithoutw= 0;
            double totalScore= 0;

            //all the summations
            for (String className : trainingSet.getClassInfos().keySet())
            {
                double temppc = trainingSet.getClassInfos().get(className) / trainingSet.totalDocumentCount;
                pc += temppc * (Math.log(temppc) / Math.log(2));
                double temppcgivenw = 0;
                for (String document : trainingSet.wordsToDocNameToClassName.get(word).keySet())
                {
                    if (trainingSet.wordsToDocNameToClassName.get(word).get(document).contains(className))
                    {
                        temppcgivenw++;
                    }
                }
                double temppcwithoutw = trainingSet.totalDocumentCount - temppcgivenw;

                temppcgivenw /= trainingSet.wordsToDocNameToClassName.get(word).size();
                if (temppcgivenw != 0)
                {
                    pcgivenw += temppcgivenw * Math.log(temppcgivenw) / Math.log(2);
                }
                temppcwithoutw /= trainingSet.wordsToDocNameToClassName.get(word).size();
                pcwithoutw += temppcwithoutw * Math.log(temppcwithoutw) / Math.log(2);
            }

            double wordScore = -(pc) + pw * pcgivenw + pnotw * pcwithoutw;
            allWords.add(new Word(word, wordScore));

        }
        Collections.sort(allWords);
        return new ArrayList<>(allWords.subList(0,numWordsinVocab));
    }

    /**
     * This method assigns the most probable class for a particular document
     * in test set. In performing the classification task, you are required
     * to use the getWordProbability and getClass-Probability methods,
     * which are defined in Sections 2.2.3 and 2.2.4, respectively
     *
     * @param documentFilePath
     * @return the class that shoudl be assigned to the document
     */
    public String label(String documentFilePath)
    {

    }

    private class SelectedFeatures
    {
        Set<String> selectedWords;
    }

    public class CollectionSet
    {
        double totalDocumentCount = 0;
        int vocabSize = 0;

        Map<String, Map<String, String>> wordsToDocNameToClassName; //word to <documentName, class>
        Map<String, Integer> classInfos; //class to document count

        public CollectionSet()
        {
            wordsToDocNameToClassName = new HashMap<>();
            classInfos = new HashMap<>();
        }

        public void addWord(String word, DocumentInfo newInfo)
        {
            if (wordsToDocNameToClassName.containsKey(word))
            {
                wordsToDocNameToClassName.get(word).put(newInfo.name, newInfo.className);
            }
            else
            {
                HashMap<String, String> newDocuments = new HashMap<>();
                newDocuments.put(newInfo.name, newInfo.className);
                wordsToDocNameToClassName.put(word, newDocuments);
            }
        }

        public Map<String,Integer> getClassInfos()
        {
            return classInfos;
        }
    }

    private static class DocumentInfo
    {
        String name;
        String className;
    }

    private class Word implements Comparable
    {
        String word;
        double score;

        public Word(String word, double wordScore)
        {
            this.word = word;
            this.score = wordScore;
        }

        @Override public int compareTo(Object o)
        {
            if (o instanceof Word)
            {
                Word other = (Word) o;
                if (this.score > other.score) return -1;
                else if (this.score == other.score) return 0;
                else return 1;
            }
            else
            {
                System.out.println("tried to compare words with something no bueno.");
                return 0;
            }
        }
    }
}
