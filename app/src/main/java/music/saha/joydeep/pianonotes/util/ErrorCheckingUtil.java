package music.saha.joydeep.pianonotes.util;

import android.util.Log;

import java.util.ArrayList;

import music.saha.joydeep.pianonotes.R;

/**
 * Created by joydeep on 05/03/17.
 */

public class ErrorCheckingUtil {

    private final static String TAG = "ErrorCheckingUtil";

    /**
     *  Checks input string for errors
     *  Returns ID list of notes, if valid
     */
    public static ArrayList<Integer> checkErrors(String notes) {

        ArrayList<Integer> noteList = new ArrayList<>();
        char[] notesArray = notes.toLowerCase().toCharArray();
        final int notesLength = notesArray.length;

        for (int i = 0; i < notesLength; i++) {

            String note = "";
            if (notesArray[i] == '.') {

                noteList.add(-1);
                Log.d(TAG, ".");
                continue;

            } else if (i < (notesLength - 1)) {

                note += notesArray[i];
                note += notesArray[i + 1];
                i++;
                if ((i < (notesLength - 1)) && (notesArray[i + 1] == 's')) {
                    note += 's';
                    i++;
                }
                Log.d(TAG, note);

            } else {
                return null;
            }

            switch (note) {
                case "a1":
                    noteList.add(R.raw.a1);
                    break;
                case "a1s":
                    noteList.add(R.raw.a1s);
                    break;
                case "b1":
                    noteList.add(R.raw.b1);
                    break;
                case "c1":
                    noteList.add(R.raw.c1);
                    break;
                case "c1s":
                    noteList.add(R.raw.c1s);
                    break;
                case "c2":
                    noteList.add(R.raw.c2);
                    break;
                case "d1":
                    noteList.add(R.raw.d1);
                    break;
                case "d1s":
                    noteList.add(R.raw.d1s);
                    break;
                case "e1":
                    noteList.add(R.raw.e1);
                    break;
                case "f1":
                    noteList.add(R.raw.f1);
                    break;
                case "f1s":
                    noteList.add(R.raw.f1s);
                    break;
                case "g1":
                    noteList.add(R.raw.g1);
                    break;
                case "g1s":
                    noteList.add(R.raw.g1s);
                    break;
                default:
                    return null;
            }
        }
        return noteList;
    }
}
