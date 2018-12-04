package com.example.thakr.newspaper_testapp_1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class Article extends Fragment {
    private TextToSpeech tts;
    public View view;
    public TextView textView;
    public Button playbutton;
    public SeekBar pitch;
    public SeekBar speed;
    public ActionMode.Callback actionModeCallBack;

    int SpeechPos = 0;

    public static SharedData sharedData;

    public String TEXT_TO_DISPLAY = "Loading";

    String store_word, store_meaning, Meaning;

    public Article() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionModeCallBack = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.word_select_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.add_word_action) {

                    View view = getActivity().getCurrentFocus();
                    if (view instanceof TextView) {
                        TextView temp = (TextView) view;
                        final int selStart = temp.getSelectionStart();
                        final int selEnd = temp.getSelectionEnd();

                        String selectedText = temp.getText().subSequence(selStart, selEnd).toString();

                        if (selectedText.split(" ").length > 1) {
                            Toast.makeText(getContext(), "Please Select 1 Word", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            new CallbackTask().execute(selectedText, "Add");
                        }

                    }
                    actionMode.finish();
                    return true;
                }
                else if (menuItem.getItemId() == R.id.show_meaning_action) {

                    View view = getActivity().getCurrentFocus();
                    if (view instanceof TextView) {
                        TextView temp = (TextView) view;
                        final int selStart = temp.getSelectionStart();
                        final int selEnd = temp.getSelectionEnd();

                        String selectedText = temp.getText().subSequence(selStart, selEnd).toString();

                        if (selectedText.split(" ").length > 1) {
                            Toast.makeText(getContext(), "Please Select 1 Word", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Finding the Meaning for you", Toast.LENGTH_SHORT).show();
                            new CallbackTask().execute(selectedText);
                        }

                    }
                    actionMode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        };

        sharedData = MainActivity.sharedData;

        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(sharedData.getNewspaperSelected());
            }
        };

        sharedData.getNewspaper().observe(this, observer);
        Log.d("DEF", "1");
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("DEF", "2");
                if (status == TextToSpeech.SUCCESS) {
                    Log.d("DEF", "3");
                    int result = tts.setLanguage(Locale.ENGLISH);
                    Log.d("DEF", "4");
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("DEF", "5");
                        Log.e("TTS", "Language not supported");
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article, container, false);

        textView = view.findViewById(R.id.ArticleTextView);
        textView.setText(sharedData.getNewspaperSelected());
        textView.setCustomSelectionActionModeCallback(actionModeCallBack);

        pitch = view.findViewById(R.id.seekbarpitch);
        speed = view.findViewById(R.id.seekbarspeed);

        playbutton = view.findViewById(R.id.play);
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playbutton.getText().equals("PLAY")) {
                    String s = textView.getText().toString();
                    Log.d("DEF", "6");
                    //speakSpeech(s);
                    speak(s);
                    playbutton.setText("STOP");
                }
                else {
                    tts.stop();
                    playbutton.setText("PLAY");
                }


            }
        });

        tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                tts.stop();
                playbutton.setText("START");
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            @Override
            public void onStart(String utteranceId) {
                playbutton.setText("STOP");
            }

            @Override
            public void onDone(String utteranceId) {
                tts.stop();
                playbutton.setText("PLAY");
            }

            @Override
            public void onError(String utteranceId) {
                tts.stop();
                playbutton.setText("PLAY");
            }
        });


        return view;
    }


    @Override
    public void onPause() {

        if (tts != null) {
            playbutton.setText("PLAY");
            tts.stop();
            //tts.shutdown();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    public void speak(String text) {

        Log.d("DEF", "7");
        float p = (float) pitch.getProgress() / 50;
        if (p < 0.1) {
            p = 0.1f;
        }
        float s = (float) speed.getProgress() / 50;
        if (s < 0.1) {
            s = 0.1f;
        }
        Log.d("DEF", "8");
        tts.setSpeechRate(s);
        Log.d("DEF", "9");
        tts.setPitch(p);
        Log.d("DEF", "10");
        //speech(text);
        /*
        Spannable spannable = new SpannableString(text);
        String[] SeperatedTexts = text.split(" ");
        BackgroundColorSpan RedBackground = new BackgroundColorSpan(Color.RED);
        int Count = 0;

        for (int i = 0; i < SeperatedTexts.length; i++) {
            Count += SeperatedTexts[i].length() + 1;
            tts.speak(SeperatedTexts[i], TextToSpeech.QUEUE_FLUSH, null);
            //spannable.setSpan(CharacterStyle.wrap(RedBackground), 0, Count, 0);
            //textView.setText(spannable);
        }
        */
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("DEF", "11");
    }

    public void speakSpeech(String speech) {
        HashMap<String, String> myHash = new HashMap<String, String>();
        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "done");
        String[] splitspeech = speech.split("\\.");
        for (int i = 0; i < splitspeech.length; i++) {

            if (i == 0) { // Use for the first splited text to flush on audio stream

                tts.speak(splitspeech[i].toString().trim(),TextToSpeech.QUEUE_FLUSH, myHash);

            } else { // add the new test on previous then play the TTS

                tts.speak(splitspeech[i].toString().trim(), TextToSpeech.QUEUE_ADD,myHash);
            }

            tts.playSilence(750, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void MakeSnackBar(String S) {
        final Snackbar snackbar = Snackbar.make(view, S, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void MakeToast (String S) {
        Toast toast = Toast.makeText(view.getContext(), S, Toast.LENGTH_SHORT );
        toast.show();
    }

    private void speech(String charSequence) {

        int position = SpeechPos;

        int sizeOfChar= charSequence.length();
        String testStri= charSequence.substring(position, sizeOfChar);

        int next = 20;
        int pos =0;
        while(true) {
            String temp="";
            Log.e("in loop", "" + pos);

            try {

                temp = testStri.substring(pos, next);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, temp);
                tts.speak(temp, TextToSpeech.QUEUE_ADD, params);

                pos = pos + 20;
                next = next + 20;


            } catch (Exception e) {
                temp = testStri.substring(pos, testStri.length());
                tts.speak(temp, TextToSpeech.QUEUE_ADD, null);
                break;

            }

        }

    }

    public class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            Meaning = "Searching";
            store_meaning = "Searching";
        }

        @Override
        protected String doInBackground(String... params) {
            //TODO: replace with your own app id and app key
            final String app_id = "000e7c7b";
            final String app_key = "b1bb500edb1e528f3b6341a69daeb92b";

            store_word = params[0].toLowerCase();

            try {
                if (Vocabulary.meanings.containsKey(store_word)) {
                    store_meaning = Vocabulary.meanings.get(store_word);
                }
                else {
                    URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/" + "en" + "/" + store_word);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("app_id", app_id);
                    urlConnection.setRequestProperty("app_key", app_key);

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
                }

                Meaning = store_word.toUpperCase() + " - " + store_meaning;
            }
            catch (Exception e) {
                e.printStackTrace();
                store_meaning = "Meaning not Found";
                Meaning = "Meaning not Found";
            }

            if (params.length == 2) {
                if (store_meaning.equals("Meaning not Found")) {
                    // NOTHING
                }
                else {
                    if (Vocabulary.meanings.containsKey(store_word)) {
                        Log.d("ABC", "ALREADY ADDED");
                        //MakeToast("WORD ALREADY ADDED");
                    }
                    else {
                        Vocabulary.items.add(store_word);
                        Collections.sort(Vocabulary.items);
                        Vocabulary.meanings.put(store_word, store_meaning);
                        try {
                            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Vocabulary.uri_array.toString()));
                            out.writeObject(Vocabulary.items);
                            out.close();
                            out = new ObjectOutputStream(new FileOutputStream(Vocabulary.uri_hash.toString()));
                            out.writeObject(Vocabulary.meanings);
                            out.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return Meaning;
        }

        @Override
        protected void onPostExecute(String result) {
            //System.out.println(result);
            Log.d("ABC", result);
            super.onPostExecute(result);
            MakeSnackBar(result);
            VocabUpdate VU = new VocabUpdate();
            VU.execute();
        }
    }
}
