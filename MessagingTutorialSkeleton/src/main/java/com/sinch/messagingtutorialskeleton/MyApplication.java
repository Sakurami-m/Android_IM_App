package com.sinch.messagingtutorialskeleton;

/**
 * Created by Haya on 1/4/2016.
 */
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.TextMining.PrepareData;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "6zuqfy2rPtBTFIhHcrQy1TbuCwXLvhJ1mcFZEA4R", "iK6g3dGw9Am5XMZo8O7nph3Lve5bGq8XiTQ8OtD8");
        // Reading from TransDatabase
        List<ParseObject> textMSGList=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TransDatabase");//TransDatabase
            try {
                textMSGList = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        //array to covert list to array
        try {
            String[] array = new String[textMSGList.size()];
            int g=0;
            //add the data in the list to the array
            for (ParseObject comment : textMSGList) {
                array[g]=comment.get("FilteredMsg").toString();
                g++;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "تأكد من اتصالك بالانترنت", Toast.LENGTH_LONG).show();

        }

    }
}
