package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class FisherYatesShuffle {

    public static int getNextShufflePosition(Context context) {

        SharedPreference pref = new SharedPreference(context);
        ArrayList<Integer> playlistShuffleArray = pref.getCurrentPlaylistShuffleArray();
        int currentShufflePosition = pref.getCurrentShuffleSongPosition();

        Random random = new Random();

        Log.d("FisherTAG", playlistShuffleArray.size() + " " + currentShufflePosition);

        if (currentShufflePosition == playlistShuffleArray.size()) {
            currentShufflePosition = 0;
            for (int i = 0; i < playlistShuffleArray.size(); i++) playlistShuffleArray.set(i, i);
            pref.setCurrentPlaylistShuffleArray(playlistShuffleArray);
        }

        int randomPosition = currentShufflePosition + random.nextInt(playlistShuffleArray.size() - currentShufflePosition);
        int randomElement = playlistShuffleArray.get(randomPosition);

        playlistShuffleArray.set(randomPosition, playlistShuffleArray.get(currentShufflePosition));
        playlistShuffleArray.set(currentShufflePosition, randomElement);
        currentShufflePosition++;

        /*RecursiveLeastSquare recursiveLeastSquare = new RecursiveLeastSquare(new Matrix(2, 2), new Matrix(2, 2));
        recursiveLeastSquare.updateWithData(new Matrix(2, 2), new Matrix(2, 2));
        recursiveLeastSquare.getB();*/

        pref.setCurrentPlaylistShuffleArray(playlistShuffleArray);
        pref.setCurrentShuffleSongPosition(currentShufflePosition);

        return randomElement;
    }

    public static int getPreviousShufflePosition(Context context) {
        SharedPreference pref = new SharedPreference(context);
        ArrayList<Integer> playlistShuffleArray = pref.getCurrentPlaylistShuffleArray();
        int currentShufflePosition = pref.getCurrentShuffleSongPosition();

        if (currentShufflePosition < 2) return currentShufflePosition;
        currentShufflePosition--;
        pref.setCurrentShuffleSongPosition(currentShufflePosition);
        return playlistShuffleArray.get(currentShufflePosition - 1);
    }

    public static void updateShuffleList(Context context, int position) {
        SharedPreference pref = new SharedPreference(context);
        ArrayList<Integer> playlistShuffleArray = pref.getCurrentPlaylistShuffleArray();
        int currentShufflePosition = pref.getCurrentShuffleSongPosition();

        for (int i = 0; i < playlistShuffleArray.size(); i++) {
            if (playlistShuffleArray.get(i) == position && i >= currentShufflePosition) {
                int currentShuffleElement = playlistShuffleArray.get(currentShufflePosition);
                playlistShuffleArray.set(currentShufflePosition, position);
                playlistShuffleArray.set(i, currentShuffleElement);

                currentShufflePosition++;

                pref.setCurrentPlaylistShuffleArray(playlistShuffleArray);
                pref.setCurrentShuffleSongPosition(currentShufflePosition);
                return;
            }
        }
    }


}
