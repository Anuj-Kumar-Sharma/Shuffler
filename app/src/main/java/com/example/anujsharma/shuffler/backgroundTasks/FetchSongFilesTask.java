package com.example.anujsharma.shuffler.backgroundTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;

public class FetchSongFilesTask extends AsyncTask<File, Void, ArrayList<File>> {

    private File rootFile;
    private ProgressDialog progressDialog;
    private Context context;

    public FetchSongFilesTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<File> doInBackground(File... params) {
        ArrayList<File> songsList;
        rootFile = params[0];
        songsList = getSongs(rootFile);
        return songsList;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Fetching files");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<File> songsList) {
        progressDialog.dismiss();
    }

    private ArrayList<File> getSongs(File root) {
        ArrayList<File> songsList = new ArrayList();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                songsList.addAll(getSongs(singleFile));
            } else {
                if ((singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".m4a"))
                        && !singleFile.getName().startsWith("Call@")) {
                    songsList.add(singleFile);
                }
            }
        }
        return songsList;
    }
}
