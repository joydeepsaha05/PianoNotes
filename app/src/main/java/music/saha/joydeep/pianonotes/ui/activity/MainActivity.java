package music.saha.joydeep.pianonotes.ui.activity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import music.saha.joydeep.pianonotes.Constants;
import music.saha.joydeep.pianonotes.R;
import music.saha.joydeep.pianonotes.util.ErrorCheckingUtil;

public class MainActivity extends AppCompatActivity implements Constants {

    private static final String TAG = "MainActivity";
    private Snackbar mSnackbar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_text_notes)
    EditText mNotesEditText;

    @BindView(R.id.layout_main)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.input_layout_notes)
    TextInputLayout mLayoutNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);
    }

    @OnClick(R.id.fab)
    void play() {
        mLayoutNotes.setErrorEnabled(false);

        if (mNotesEditText.getText().toString().isEmpty()) {
            mLayoutNotes.setError(getString(R.string.empty));
            return;
        }

        ArrayList<Integer> notesList = ErrorCheckingUtil.checkErrors(
                mNotesEditText.getText().toString());

        if (notesList == null) {
            mLayoutNotes.setError(getString(R.string.error_notes));
            mNotesEditText.requestFocus();
            return;
        }

        final MusicPlayer musicPlayer = new MusicPlayer(notesList);

        mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.playing,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.cancel(true);
                        mFab.setClickable(true);
                    }
                });
        mSnackbar.show();

        musicPlayer.execute();
    }

    // Plays the notes in a background thread
    private class MusicPlayer extends AsyncTask<String, String, String> {

        private ArrayList<Integer> notesList;
        private final Object lock;
        private MediaPlayer player;

        MusicPlayer(ArrayList<Integer> notesList) {
            this.notesList = notesList;
            lock = new Object();
        }

        @Override
        protected void onPreExecute() {
            mFab.setClickable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            for (Integer noteId : notesList) {
                if (isCancelled()) {
                    return null;
                }
                if (noteId == -1) {
                    try {
                        Thread.sleep(WAIT_TIME_MILLIS);
                    } catch (InterruptedException e) {
                        Log.d(TAG, e.toString());
                    }
                } else {
                    player = MediaPlayer.create(getApplicationContext(), noteId);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            player.release();
                            player = null;
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    });
                    player.start();
                    try {
                        synchronized (lock) {
                            lock.wait();
                        }
                    } catch (InterruptedException e) {
                        Log.d(TAG, e.toString());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mSnackbar.dismiss();
            mFab.setClickable(true);
        }
    }
}
