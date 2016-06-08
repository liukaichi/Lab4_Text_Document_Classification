import java.util.Map;
import java.util.Set;

/**
 * This class is for training an MNB
 */
public class MNB_Probability
{

    /**
     *This method computes the probability of each distinct word in each natural class in C using training
     set. In computing the word probabilities, you are required to use the Laplacian Smoothed Estimate.
     * @param
     * @return
     */
    public WordProbabilities computeWordProbability(Set training_set)
    {
        return null;
    }

    /**
     * This method retrieves the probability value of a natural class.
     * */
    public ClassProbabilities  computeClassProbability(String className)
    {
        return null;
    }
}


class ClassProbabilities
{
    Map<String, Double> classToProbability; //class name to probability
}

class WordProbabilities
{
    Map<String, Double> wordToProbability; //Word to probability
}


