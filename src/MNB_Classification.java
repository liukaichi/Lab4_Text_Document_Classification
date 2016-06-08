import java.util.Set;

/**
 * This class is used for
 * (i) training an MNB and
 * (ii) classifying unlabeled documents using the trained MNB.
 * Created by liukaichi on 6/7/2016.
 */

public class MNB_Classification
{

    /**
     * This method determines the words which should be chosen to represent
     * documents in the training set and test set based on the information gain (IG)
     * of each vocabulary term w
     */
    /**
     *
     * @param trainingSet
     * @param numWordsinVocab must be >= 1. If M = |V|, then NO feature selection is applied
     */
    public SelectedFeatures featureSelection(Set trainingSet, int numWordsinVocab)
    {
        return null;
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
        return null;
    }

    private class SelectedFeatures
    {
        Set<String> selectedWords;
    }
}
