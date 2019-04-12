package edu.miracostacollege.superheroesquiz.Model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONLoader {

    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException {

        List<Superhero> allSuperHeroes = new ArrayList<>();
        String json = null;
        InputStream is = context.getAssets().open("cs134superheroes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperheroesJSON = jsonRootObject.getJSONArray("CS134Superheroes");

            int numHeroes = allSuperheroesJSON.length();

            JSONObject superheroJSON;
            String name, thingToKnow, superpower, imageName;
            Superhero superhero = null;

            for (int i =0; i < numHeroes; i++) {
                superheroJSON = allSuperheroesJSON.getJSONObject(i);

                name = superheroJSON.getString("Name");
                thingToKnow = superheroJSON.getString("OneThing");
                superpower = superheroJSON.getString("Superpower");
                imageName = superheroJSON.getString("FileName");

                superhero = new Superhero(name, thingToKnow, superpower, imageName);
                allSuperHeroes.add(superhero);
            }

        } catch (JSONException e) {
            Log.e("Superhero Quiz", e.getMessage());
        }

        return allSuperHeroes;
    }



}
