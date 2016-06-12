import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is for training an MNB
 */
public class MNB_Probability
{
    ClassProbabilities classProbabilities = new ClassProbabilities();
    WordProbabilities wordProbabilities = new WordProbabilities();
    /**
     * This method computes the probability of each distinct word in each natural class in C using training
     * set. In computing the word probabilities, you are required to use the Laplacian Smoothed Estimate.
     *
     * @param
     * @return
     */
    public WordProbabilities computeWordProbability(MNB_Classification.CollectionSet training_set)
    {

        for (String word : training_set.wordsToDocNameToClassName.keySet())
        {
            for (String className : training_set.getClassInfos().keySet())
            {
                double termFreqInClass = ClassManager.getTermFrequency(word, className);
                double probability = (termFreqInClass + 1) / (training_set.getClassInfos().size() + training_set.vocabSize)
                wordProbabilities.addEntry(word, className, probability);
            }
        }
        return wordProbabilities;
    }

    /**
     * This method retrieves the probability value of a natural class.
     */
    public ClassProbabilities computeClassProbability(String className, MNB_Classification.CollectionSet training_set)
    {

        for (Map.Entry<String, Integer> classInfo : training_set.getClassInfos().entrySet())
        {
            classProbabilities.classToProbability.put(className, ((double)classInfo.getValue().intValue())/training_set.totalDocumentCount);
        }
        return classProbabilities;
    }

    public double getWordProbability(String word, String className)
    {
        
    }
}

class ClassProbabilities
{
    Map<String, Double> classToProbability; //class name to probability
}

class WordProbabilities
{
    HashMap<String, List<Pair<String, Double>>> wordToProbability = new HashMap<>(); //Word to class to probability

    public void addEntry(String word, String className, Double probability)
    {
        //already has word.
        if (wordToProbability.containsKey(word))
        {
            wordToProbability.get(word).add(new Pair<>(className, probability));

        } else //doesn't have word yet.
        {
            List<Pair<String, Double>> newList = new ArrayList<>();
            newList.add(new Pair<>(className, probability));
            wordToProbability.put(word, newList);
        }
    }
}


