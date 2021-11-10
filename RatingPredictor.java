/**
 * Filename: RatingPredictor.java
 * Name: Lucas Wan
 * Login: cs8bwi20rj
 * Date: Feb 27, 2020
 * Sources of Help: notes, Piazza
 *
 * This file contains methods for predicting rating for reviews.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.io.*;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * This class is to perform review process and make prediction to raw reviews.
 */
public class RatingPredictor  {
    private HashMap<String, int[]> wordFreqMap;
    private HashSet<String> stopWords;

    /**
     * default constructor
     *
     */
    public RatingPredictor() {
        wordFreqMap = new HashMap<String, int[]>();
        stopWords = new HashSet<String>();
    }

    /**
     * a method to split a string into arraylist
     *
     * @param sentence the string that will be splited
     * @return an arraylist of splited sentence.
     */
    public ArrayList<String> splitLine(String sentence) {
        // null check and empty check
        if (sentence == null || sentence  == "") {
            return null;
        }
        String[] words =  sentence.split(" ");
        ArrayList<String> splitedSentence  =  new ArrayList<>();
        for (String word : words) {
            splitedSentence.add(word);
        }
        return splitedSentence;
    }

    /**
     * a method to check elements in a string arraylist and split the string at
     * hyphens and single quotes.
     *
     * @param words a ArrayList of words that will be checked
     * @return the modified arraylist of words
     */
    public ArrayList<String> splitAtHyphensAndQuotes(ArrayList<String> words) {
        if (words == null || words.size() == 0) {
            return null;
        }
        for (int i = 0; i < words.size(); i++){
            if (words.get(i).contains("-")) {
                String[] splitedParts = words.get(i).split("-");
                // add the  splited parts back to the ArrayList and mke sure
                // they have the correct sequence.
                for (int index = splitedParts.length-1; index >= 0; index--) {
                    words.add(i+1, splitedParts[index]);
                }
                words.remove(i);
            }
            if (words.get(i).contains("'")) {
                String[] splitedParts = words.get(i).split("'");
                // add the  splited parts back to the ArrayList and mke sure
                // they have the correct sequence.
                for (int index = splitedParts.length-1; index >= 0; index--) {
                    words.add(i+1, splitedParts[index]);
                }
                words.remove(i);
            }
        }
        return words;
    }

    /**
     * a method that check elements in given string arraylist and remove
     * punctuations in the words
     *
     * @param words the arraylist that will be checked and modified
     * @return the modified arraylist of words
     */
    public ArrayList<String> removePunctuation(ArrayList<String> words) {
        if (words == null || words.size() == 0) {
            return null;
        }
        for (int i = 0; i < words.size(); ++i) {
            // replace all punctuations with empty string
            String newWord = words.get(i).replaceAll("\\p{Punct}", "");
            words.set(i, newWord);
        }
        return words;
    }

    /**
     * a method to remove all white spaces in each element of the arraylist
     *
     * @param words the arraylist that will be checked and modified
     * @return the modified arraylist of words
     */
    public ArrayList<String> removeWhiteSpaces(ArrayList<String> words) {
        if (words == null || words.size() == 0) {
            return null;
        }
        for (int i = 0; i < words.size(); ++i) {
            // replace all spaces with empty string
            words.set(i, words.get(i).replace(" ", ""));
        }
        return words;
    }

    /**
     * a method to remove all empty words in the arraylist of words
     *
     * @param words the arraylist that will be checked and modified
     * @return the modified arraylist of words
     */
    public ArrayList<String> removeEmptyWords(ArrayList<String> words) {
        if (words == null || words.size() == 0) {
            return null;
        }
        for (int i = 0; i < words.size(); ++i) {
            if (words.get(i).isEmpty()) {
                words.remove(i);
                // all index of elements behind are abstracted by 1, minus i
                // by 1 to make sure to go through every element.
                i--;
            }
        }
        return words;
    }

    /**
     * a method to remove all single letter words in the arraylist of words
     *
     * @param words the arraylist that will be checked and modified
     * @return the modified arraylist of words
     */
    public ArrayList<String> removeSingleLetterWords(ArrayList<String> words) {
        if (words == null || words.size() == 0) {
            return null;
        }
        for (int i = 0; i < words.size(); ++i) {
            if (words.get(i).length() == 1) {
                words.remove(i);
                // all index of elements behind are abstracted by 1, minus i
                // by 1 to make sure to check every element.
                i--;
            }
        }
        return words;
    }

    /**
     * a method to make every string of the arraylist to lowercase
     *
     * @param words the arraylist that will be checked and modified
     * @return the modified arraylist of words
     */
    public ArrayList<String> toLowerCase(ArrayList<String> words) {
        if (words == null || words.size() == 0) {
            return null;
        }
        for (int i = 0; i < words.size(); ++i) {
            words.set(i, words.get(i).toLowerCase());
        }
        return words;
    }

    /**
     * a method to remove all stopwords in the arraylist
     *
     * @param words the arraylist that will be checked and modified
     * @return the modified arraylist of words
     */
    public ArrayList<String> removeStopWords(ArrayList<String> arrList) {
        if (arrList == null || arrList.size() == 0) {
            return null;
        }
        for (int i = 0; i < arrList.size(); ++i) {
            if (stopWords.contains(arrList.get(i))) {
                arrList.remove(i);
                // all index of elements behind are abstracted by 1, minus i
                // by 1 to make sure to check every element.
                i--;
            }
        }
        return arrList;
    }

    /**
     * a method that read a file containing stopwords, create a hashset of the
     * stopwords and output the HashSet to the output file
     *
     * @param inFile the directory of the file that containing the words
     * @param outFile the directory of the file that will be written
     */
    public void createStopWordsSet(String inFile, String outFile)
            throws FileNotFoundException{
        File in_File = new File(inFile);
        File out_File = new File(outFile);
        PrintWriter pw = new PrintWriter(out_File);
        Scanner sc = new Scanner(in_File);
        // add every word to the hashset
        while (sc.hasNextLine()) {
            stopWords.add(sc.nextLine());
        }
        for (String word : stopWords) {
            pw.println(word);
        }
        sc.close();
        pw.close();
    }

    /**
     * a method to read raw input and process them into clean data by calling
     * the previous methods. And then write them into outFile.
     *
     * @param inFile the directory of the file will br read
     * @param outFile the directory of the file will be written
     */
    public void cleanData (String inFile, String outFile,
            boolean ratingIncluded) throws FileNotFoundException {
        File in_File = new File(inFile);
        File out_File = new File(outFile);
        Scanner sc = new Scanner(in_File);
        PrintWriter pw = new PrintWriter(out_File);
        while (sc.hasNextLine()) {
            int rating = 0;
            // if ratings are included, collect them first before they are
            // removed by removeSingleLetterWords()
            if (ratingIncluded) {
                rating = sc.nextInt();
            }
            // process(clean) the line
            ArrayList<String> currentList = splitLine(sc.nextLine());
            currentList = splitAtHyphensAndQuotes(currentList);
            currentList = removePunctuation(currentList);
            currentList = removeWhiteSpaces(currentList);
            currentList = removeEmptyWords(currentList);
            currentList = removeSingleLetterWords(currentList);
            currentList = toLowerCase(currentList);
            currentList = removeStopWords(currentList);

            String result = "";
            // handle the case for empty reviews. Empty case will become null
            // after removeSingleLetterWords()
            if (currentList != null) {
                for (String word : currentList) {
                    result += (word + " ");
                }
            }
            // add the ratings back to the front if they are included
            if (ratingIncluded) {
                result = rating + " " + result;
            }
            pw.println(result);
        }
        sc.close();
        pw.close();
    }

    /**
     * a method to update the hashmao wordFreqMap according to the rules
     *
     * @param inCleanFile the directory of the file that will be read
     */
    public void updateHashMap(String inCleanFile) throws FileNotFoundException {
        File inFile = new File(inCleanFile);
        Scanner sc = new Scanner(inFile);
        while (sc.hasNextLine()) {
            // collect the rating for every word in the line
            int rating = sc.nextInt();
            // split and store the line into a arraylist
            ArrayList<String> words = splitLine(sc.nextLine());
            for (String word : words) {
                if (!wordFreqMap.containsKey(word)) {
                    // set the corresponding values to the int array
                    int[] arr = new int[2];
                    arr[0] = rating;
                    arr[1] = 1;
                    wordFreqMap.put(word, arr);
                }
                else {
                    // set the corresponding values to the int array when
                    //  the map already contains the key
                    int[] currentArr = wordFreqMap.get(word);
                    currentArr[0] += rating;
                    currentArr[1] += 1;
                    wordFreqMap.put(word, currentArr);
                }
            }
        }
        sc.close();
    }

    /**
     * a method to make prediction for the reviews in inCleanFile and write the
     * rating to the outRating File
     *
     * @param inCleanFile the file that contains raw reviews.
     * @param ourRatingFile the file that will be written.
     */
    public void rateReviews(String inCleanFile, String outRatingsFile)
            throws FileNotFoundException {
        File inFile = new File(inCleanFile);
        File outFile = new File(outRatingsFile);
        Scanner sc = new Scanner(inFile);
        PrintWriter pw = new PrintWriter(outRatingsFile);
        while (sc.hasNextLine()) {
            ArrayList<String> words = splitLine(sc.nextLine());
            // declare the sum of ratings for all the words in the review
            double sumOfRating = 0;
            for (String word : words) {
                double wordRating = 0;
                // get the rating for the word if it is contained in the map
                if (wordFreqMap.containsKey(word)) {
                    int[] arr = wordFreqMap.get(word);
                    wordRating = (double)arr[0] / (double)arr[1];
                }
                // give neutral rating fo the word if it is not contained
                else {
                    wordRating = 2.0;
                }
                sumOfRating += wordRating;
            }
            // calculate the rating for the review
            double averageRating = sumOfRating / words.size();
            // round it to one decimal place and print the rating
            DecimalFormat df = new DecimalFormat("0.0");
            pw.println(df.format(averageRating));
        }
        sc.close();
        pw.close();
    }


    public static void main(String[] args) throws FileNotFoundException {
        RatingPredictor AI = new RatingPredictor();
        AI.createStopWordsSet("stopwords.txt", "uniqueStopwords.txt");
        AI.cleanData("rawReviewRatingsBig.txt", "cleanReviewRatings.txt",
                true);
        AI.updateHashMap("cleanReviewRatings.txt");
        AI.cleanData("rawReviewsBig.txt", "cleanReviews.txt", false);
        AI.rateReviews("cleanReviews.txt", "ratings.txt");
    }
}
