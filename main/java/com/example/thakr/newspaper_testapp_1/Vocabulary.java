package com.example.thakr.newspaper_testapp_1;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

public class Vocabulary extends Fragment {
    public View view;
    public SearchView searchView;
    public ListView listView;
    public TextView Word;
    public TextView Meaning;
    public static ArrayList<String> items = new ArrayList<>();
    public static HashMap<String,String> meanings = new HashMap();
    Button add_word;
    Button ok_button;
    String store_word;
    String store_meaning;
    public static Uri uri_array;
    public static Uri uri_hash;

    public static String MeaningShown;

    String BinaryFileName = "BINARY_FILE.dat";
    String BinaryFileLocation = "BINARYFILES";

    File file = MainActivity.file;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Vocabulary.uri_array = Uri.parse(MainActivity.fileVocab + "binary_array.dat");
        Vocabulary.uri_hash = Uri.parse(MainActivity.fileVocab + "binary_hash.dat");

        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(uri_array.toString()));
            items = (ArrayList<String>) objectInputStream.readObject();
            objectInputStream.close();

            objectInputStream = new ObjectInputStream(new FileInputStream(uri_hash.toString()));
            meanings = (HashMap<String,String>) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vocabulary() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vocabulary, container, false);

        //file = new File(getActivity().getExternalFilesDir("binaryfiles"),"/");
        //File binary_array = new File(file,"binary_array.dat");
        //File binary_hash = new File(file,"binary_hash.dat");

        Word = (TextView) view.findViewById(R.id.Word);
        Meaning = (TextView) view.findViewById(R.id.Meaning);
        add_word = (Button) view.findViewById(R.id.add_word);
        ok_button = (Button) view.findViewById(R.id.ok_button);
        searchView = view.findViewById(R.id.SearchView);

        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = searchView.getQuery().toString();
                Toast.makeText(getContext(),word,Toast.LENGTH_SHORT).show();
                ok_button.setVisibility(View.GONE);
                new CallbackTask().execute(word);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ok_button.setVisibility(View.GONE);
                String word = searchView.getQuery().toString();
                Word.setText(word);
                if(meanings.containsKey(word)) {
                    String found_meaning = meanings.get(word);
                    Meaning.setText(found_meaning);
                    listView.setVisibility(View.GONE);
                    Word.setVisibility(View.VISIBLE);
                    Meaning.setVisibility(View.VISIBLE);
                    ok_button.setVisibility(View.VISIBLE);
                }
                else {
                    new CallbackTask().execute(word);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        listView = (ListView) view.findViewById(R.id.VocabularyListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.clearFocus();
                String found = items.get(position);
                String found_meaning = meanings.get(found);
                Word.setText(found);
                Meaning.setText(found_meaning);
                listView.setVisibility(View.GONE);
                Word.setVisibility(View.VISIBLE);
                Meaning.setVisibility(View.VISIBLE);
                ok_button.setVisibility(View.VISIBLE);
            }
        });

        add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                items.add(store_word);
                Collections.sort(items);
                meanings.put(store_word,store_meaning);
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(uri_array.toString()));
                    out.writeObject(items);
                    out.close();
                    out = new ObjectOutputStream(new FileOutputStream(uri_hash.toString()));
                    out.writeObject(meanings);
                    out.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                ok_button.setVisibility(View.GONE);
                Word.setVisibility(View.GONE);
                Meaning.setVisibility(View.GONE);
                add_word.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                VocabUpdate VU = new VocabUpdate();
                VU.execute();

            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok_button.setVisibility(View.GONE);
                Word.setVisibility(View.GONE);
                Meaning.setVisibility(View.GONE);
                add_word.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }


    public class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            Meaning.setText("Searching...");
            listView.setVisibility(View.GONE);
            Meaning.setVisibility(View.VISIBLE);
            add_word.setVisibility(View.GONE);
            ok_button.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            //TODO: replace with your own app id and app key
            final String app_id = "000e7c7b";
            final String app_key = "b1bb500edb1e528f3b6341a69daeb92b";

            store_word = params[0].toLowerCase();

            try {
                URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/" + "en" + "/" + store_word);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                store_meaning = stringBuilder.toString();
                int start =  store_meaning.indexOf("definitions");
                start =  store_meaning.indexOf(" \"",start);
                int end =  store_meaning.indexOf("\"",start+2);
                store_meaning =  store_meaning.substring(start+2,end);
                return store_meaning;

            }
            catch (Exception e) {
                e.printStackTrace();
                add_word.setVisibility(View.GONE);
                return "Meaning not Found";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //System.out.println(result);
            Log.d("ABC", result);
            super.onPostExecute(result);
            if (result.equals("Meaning not Found")) {
                Word.setVisibility(View.GONE);
                add_word.setVisibility(View.GONE);
            }
            else {
                Word.setVisibility(View.VISIBLE);
                add_word.setVisibility(View.VISIBLE);
            }
            Word.setText(store_word);
            Word.setVisibility(View.VISIBLE);
            Meaning.setText(result);

            ok_button.setVisibility(View.VISIBLE);
        }
    }
}