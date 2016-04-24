package com.TextMining;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Marwa Khan on 11/3/2016.
 * Class Description: This class is responsible for removing stop words such as (من ، هو )
 * In addition, it also remove links, phone numbers within a text message 
 */
public class WordNormalization {

    // Method to remove the words 
    // Input: String 
    // Output: String 
    public static String RemoveWords(String str)
    {
        String newStr = "";

        if (str.length() == 1 && str.matches("([\u0600-\u06FF])|([0-9])|([a-zA-Z])"))
            return newStr;

        if (!str.contains(" ")) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("IgnoreWordsDatabase");

                //Remove the Word if it in the stop list
                query.whereEqualTo("StopWord", str);

                try {
                    List<ParseObject> OutputList = query.find();

                    if (OutputList.size() == 0)
                        newStr += str;
                        
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
        else
        {
             newStr =  RemoveConnectedWords(str);
        }

        return newStr;
    }

    // Method to remove ignore words from a sentence (2 or more connected words)
    private static String RemoveConnectedWords(String str)
    {
        String[] ArrayStr = str.split(" ");

        String newString= "";

        ParseQuery<ParseObject> query = ParseQuery.getQuery("IgnoreWordsDatabase");

        for (int index= 0; index < ArrayStr.length; ++index)
        {
            if(!ArrayStr[index].equals(" ")) {
                query.whereEqualTo("StopWord", ArrayStr[index]);

                try {
                    List<ParseObject> OutputList = query.find();

                    if (OutputList.size() == 0)
                        newString += ArrayStr[index] + " ";

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (!newString.isEmpty())
           newString = newString.substring(0, newString.length() - 1);
        return newString;
    }

    // Method to filter words by removing Hamza, Diactrities...etc.
    public static String FilterWords(String str) {

        if (str.matches("^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
            return str;

        //Remove Diacrities + Shaddah
        String CurrentStr = str.replaceAll("[\u064E\u064B\u064F\u064C\u0650\u064D\u0652\u0640\u0651]", "");

        CurrentStr = CurrentStr.toLowerCase();

        //Replace Hamza
        CurrentStr = CurrentStr.replaceAll("[\u0623\u0625\u0622]", "\u0627");

        //Remove Taa Marbutah
        CurrentStr = CurrentStr.replaceAll("\u0629", "\u0647");


        String regex =  "(https?|ftp|file){1}:?/?/?[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String regex2 =  "(www.){1}?/?/?[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";


        if ((CurrentStr.toLowerCase().contains("http") && CurrentStr.matches(regex) == false) || (CurrentStr.contains("www") && CurrentStr.matches(regex2) == false ) )
        {
            String regexToSplit =  "(?=[^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]])(?<=[.|\u0600])";

            String[] testingURL = CurrentStr.replaceAll(regexToSplit, " ").split(" ");

            String newStr = "";

            for(int index = 0; index < testingURL.length; ++index)
            {
                if (testingURL[index].contains("http") || testingURL[index].contains("www")) {

                    if (!testingURL[index].matches(regex))
                    {
                        testingURL[index] = testingURL[index].substring(0, testingURL[index].length() - 1);

                        PrepareData.HasURL = true;
                    }
                }

                newStr +=testingURL[index] + " ";
            }

            if (!newStr.isEmpty())
            newStr = newStr.substring(0, newStr.length() - 1);

            return newStr;
        }

        if (!CurrentStr.matches(regex) && !CurrentStr.matches(regex2)) {
            CurrentStr = CurrentStr.replaceAll("[^A-Z|a-z|0-9|\u0621-\u065F|\u066E-\u06D3|\u06D5]", " ");

            if (CurrentStr.startsWith(" "))
                CurrentStr = CurrentStr.substring(1);
            else if (CurrentStr.endsWith(" "))
                CurrentStr = CurrentStr.substring(0, CurrentStr.length() - 1);

            CurrentStr = CurrentStr.replaceAll("[0-9@%$!~><*-]", " ");
            CurrentStr = CurrentStr.replaceAll("\\s+", " ");
        }

        //CurrentStr;
        return CurrentStr;
    }

}
