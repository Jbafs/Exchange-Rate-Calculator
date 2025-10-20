package com.q1_project.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Interface with static methods to retrieve JSONs from the link
 */
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
            ret = new URI(useURL+endPoint).toURL().openConnection();
        } catch (Exception e) {
            try {
                ret = new URI(backUpURL + endPoint).toURL().openConnection();
            } catch (Exception ex) {
                System.out.println("END POINT BROKEN"+ex);
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
        } catch (IOException e) {
            System.out.println(""+e);
            return null;
        }

        assert br != null;
        return readJSON(br);
    }

    /**
     * Get JSON with string key and double value
     * @param endPoint site to search
     * @return hashmap or null if not available
     */
    static HashMap<String, Double> getDoubleJSONFrom(String endPoint){
        BufferedReader br;
        URLConnection urlc = getURLConnection(endPoint);
        try {
            //get the buffered reader if possible
            if (urlc != null) br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            else throw new IOException();
        } catch (IOException e) {
            System.out.println(""+e);
            return null;
        }

        return readJSONDouble(br);
    }

    /**
     * Take in a buffered reader br that links to a JSON then read through it. Only works with JSONS
     * that only have one layer of brackets
     * @param br : Buffered reader that links to json
     * @return 2 String arraylists or null if not working
     */
    static ArrayList<String>[] readJSON(BufferedReader br){
        String l;
        ArrayList<String>[] ret = new ArrayList[2];
        ret[0] = new ArrayList<>();
        ret[1] = new ArrayList<>();

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
        System.out.println(Arrays.toString(ret));
        return ret;
    }

    /**
     * Take in a buffered reader br that links to a JSON then read through it. Only works with JSONS
     * that only have one layer of brackets, values are doubles instead of strings
     * @param br : Buffered reader that links to json
     * @return Hashmap with key values mapped
     */
    static HashMap<String, Double> readJSONDouble(BufferedReader br){

        String l;
        HashMap<String, Double> ret = new HashMap<>();

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
