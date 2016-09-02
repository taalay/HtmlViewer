package com.tali.admin.htmlviewer.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tali.admin.htmlviewer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private EditText editText;
    private Button go;
    private TextView textView;
    private String content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        editText = (EditText) v.findViewById(R.id.editText);
        go = (Button) v.findViewById(R.id.go);
        textView = (TextView) v.findViewById(R.id.textView);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    String temp = editText.getText().toString();
                    if(temp.toLowerCase().startsWith("http")){
                        content = temp;
                    }else {
                        content = "http://" + temp;
                    }
                    new UrlLoadTask().execute();
                }
            }
        });
        return v;
    }

    private class UrlLoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String temp = null;
            try {
                temp = loadUrl(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }

        private String loadUrl(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                URLConnection c = (URLConnection) url.openConnection();
                c.setReadTimeout(10000);
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return (buf.toString());
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
}

