package com.q1_project.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public interface JsonManager {
    String useURL="https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/";
    String backUpURL = "https://latest.currency-api.pages.dev/v1/";

    /**
     * Get the url connection to endpoint string
     * @param endPoint site to attempt connection to
     * @return URLConnection to site or null if not
     */
    static URLConnection getURLConnection(String endPoint){
        URLConnection ret;
        try {
            ret = new URL(useURL+endPoint).openConnection();
        } catch (IOException e) {
            try {
                ret = new URL(backUpURL + endPoint).openConnection();
            } catch (IOException ex) {
                System.out.println("END POINT BROKEN");
                return null;
            }
        }

        return ret;
    }

    /**
     * Obtains hashmap from API
     * @param endPoint endpoint to search
     * @return hashmap with string key and value or null if not viable
     */
    static ArrayList<String>[] getJSONFrom(String endPoint){
        BufferedReader br = null;
        URLConnection urlc = getURLConnection(endPoint);
        try {
            if (urlc != null) br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            else throw new IOException();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }

        return readJSON(br);
    }

    /**
     * Get JSON with string key and double value
     * @param endPoint site to search
     * @return hashmap or null if not available
     */
    static HashMap<String, Double> getDoubleJSONFrom(String endPoint){
        BufferedReader br = null;
        URLConnection urlc = getURLConnection(endPoint);
        try {
            if (urlc != null) br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            else throw new IOException();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }

        return readJSONDouble(br);
    }

    /**
     * Get key from value you know in hashmap (for finding shortname from country in list)
     * @param map map to search
     * @param value value to search for
     * @return key or null if not applicable
     */
    static String getKeyByValue(HashMap<String, String> map, String value) {
        for (HashMap.Entry<String, String> entry : map.entrySet())
            if (entry.getValue().equals(value)) // Use .equals() for object comparison
                return entry.getKey();
        return null; // Return null if the value is not found
    }


    /**
     * Take in a buffered reader br that links to a JSON then read through it. Only works with JSONS
     * that only have one layer of brackets
     * @param br : Buffered reader that links to json
     * @return 2 String arraylists or null if not working
     */
    static ArrayList<String>[] readJSON(BufferedReader br){

        String l = null;
        ArrayList<String>[] ret = new ArrayList[2];
        ret[0] = new ArrayList<String>();
        ret[1] = new ArrayList<String>();

        try{
            br.readLine();
            //loop through lines
            while ((l=br.readLine())!=null) {
                //check if end
                if(l.contains("}")) continue;
                //split into key and value
                String[] pair = l.split(":");
                //take unnecessary things off strings
                String key = pair[0].replace('"', ' ').trim();
                String value = pair[1].replace('"', ' ').replace(',',' ').trim();
                //add to return lists
                ret[0].add(key);
                ret[1].add(value);
            }
            //close reader
            br.close();
        }catch(IOException e) {
            //catch exception
            System.out.println("You're wrong");
            return null;
        }
        //return hash
        System.out.println(ret);
        return ret;
    }

    /**
     * Take in a buffered reader br that links to a JSON then read through it. Only works with JSONS
     * that only have one layer of brackets, values are doubles instead of strings
     * @param br : Buffered reader that links to json
     * @return Hashmap with key values mapped
     */
    static HashMap<String, Double> readJSONDouble(BufferedReader br){

        String l = null;
        HashMap<String, Double> ret = new HashMap<String, Double>();

        try{
            br.readLine();
            br.readLine();
            br.readLine();
            //loop through lines
            while ((l=br.readLine())!=null) {
                //check if end
                if(l.contains("}")) continue;
                //split into key and value
                String[] pair = l.split(":");
                //take unnecessary things off strings
                String key = pair[0].replace('"', ' ').trim();
                Double value = Double.parseDouble(pair[1].replace('"', ' ').replace(',',' ').trim());
                //add to return hash
                ret.put(key,value);
            }
            //close reader
            br.close();
        }catch(IOException e) {
            //catch exception
            System.out.println("You're wrong");
            return null;
        }
        //return hash
        System.out.println(ret);
        return ret;
    }
}
