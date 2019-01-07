package com.example.arno.homeracer.Helpers;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arno.homeracer.Objects.Score;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HighscoreManager  {
    // An arraylist of the type "score" we will use to work with the scores inside the class
    private ArrayList<Score> scores=null;
    static String resultPost;
    static String stringy;


    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }

    public static String getHighscoreFile() {

        return HIGHSCORE_FILE;
    }

    // The name of the file where the highscores will be saved
    private static final String HIGHSCORE_FILE = "scores.dat";

    //Initialising an in and outputStream for working with the file
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public HighscoreManager() {
        //initialising the scores-arraylist
        scores = new ArrayList<Score>();
    }

    public ArrayList<Score> getScores() {
        loadScoreFile();
        sort();
        return scores;
    }

    private void sort() {
        ScoreComparator comparator = new ScoreComparator();
        Collections.sort(scores, comparator);
    }

    public void addScore(String name, Long score, boolean sortRace) {
        loadScoreFile();
        scores.add(new Score(name, score, sortRace));
        updateScoreFile();
    }

    public void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Laad] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Laad] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Laad] CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            }
        }
    }

    public void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    public String getHighscoreString() {
        String highscoreString = "";
        final int max = 5;

        ArrayList<Score> scores;
        scores = getScores();

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString += (i + 1) + ".\t" + scores.get(i).getUsernameHS() + "\t\t" + scores.get(i).getTime() + "\n";
            i++;
        }
        return highscoreString;
    }




    protected HighscoreManager(Parcel in) {
        this.scores = new ArrayList<Score>();
        in.readList(this.scores, Score.class.getClassLoader());
        this.outputStream = in.readParcelable(ObjectOutputStream.class.getClassLoader());
        this.inputStream = in.readParcelable(ObjectInputStream.class.getClassLoader());
    }

    //VANAF HIER IS ALLES IN GEBRUIK

    static public void postTime(final Long time, final String username, Context context){
        String url = "https://aothomeracer.azurewebsites.net/api/score/";

        Map<String, String> params = new HashMap<String, String>();
        params.put("RaceName", username);
        params.put("TimeScore", time.toString());
        JSONObject jsonObj = new JSONObject(params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());;
                Log.d("HIGHSCOREADD", "HIGHSCORE");;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HIGHSCOREADD", "HIGHSCORE");;

            }
        });
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    static public void GetHighscore(final String raceName, final Context context, final ServerCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://aothomeracer.azurewebsites.net/api/score/" + raceName;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("volleyGetHighscore", "onResponse: " + response);
                        stringy = new String(response);
                        callback.onSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyGetHighscore", "onResponse: " + error.toString());
                resultPost = error.toString();
            }
        });
        Volley.newRequestQueue(context).add(stringRequest);
    }

    static public String getString(){
        return stringy;
    }
}
