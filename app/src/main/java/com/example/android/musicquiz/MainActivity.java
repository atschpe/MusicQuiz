package com.example.android.musicquiz;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //@param to keep tabs on the score.
    int score = 0;
    int musicClip = 0;

    //Handles playback of musicClips.
    private MediaPlayer mMediaPlayer;

    //Handles audio focus when playing musicClips.
    private AudioManager mAudioManager;

    //Any audio focus change triggers this listener.
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Ducking or pausing is not favourable, as the clips should be heard clearly and in
                // their entirety.
                // Stop playback and clean up resources.
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback once focus is regained.
                mMediaPlayer.start();
            }
        }
    };

    //Completion of playing the musicClip triggers this listener.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //listening to each clip
        ImageButton play1 = (ImageButton) findViewById(R.id.play1);
        play1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicClip = R.raw.bach_gounod;
                playClip(musicClip);
            }
        });

        ImageButton play2 = (ImageButton) findViewById(R.id.play2);
        play2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicClip = R.raw.britten_purcell;
                playClip(musicClip);
            }
        });

        ImageButton play3 = (ImageButton) findViewById(R.id.play3);
        play3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicClip = R.raw.dukas;
                playClip(musicClip);
            }
        });

        ImageButton play4 = (ImageButton) findViewById(R.id.play4);
        play4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicClip = R.raw.mahler;
                playClip(musicClip);
            }
        });

        ImageButton play5 = (ImageButton) findViewById(R.id.play5);
        play5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicClip = R.raw.vivaldi;
                playClip(musicClip);
            }
        });

        ImageButton play6 = (ImageButton) findViewById(R.id.play6);
        play6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicClip = R.raw.prokofiev;
                playClip(musicClip);
            }
        });
    }

    private void playClip(int musicClip) {
        // Release the media player if already in use.
        releaseMediaPlayer();
        // Request audio focus to allow the app to play the audio file. As the music samples
        // are longer the audio focus is set to AUDIOFOCUS_GAIN.
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // With audio focus granted create a {@link MediaPlayer} for the audio resource.
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), musicClip);

            // Start the audio file
            mMediaPlayer.start();

            // Once completed stop and release the MediaPlayer.
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    public void submit(View view) {

        //Determine whether the two correct answers have been checked. (question 1 & 2)
        CheckBox correct1a = (CheckBox) findViewById(R.id.correct1a);
        boolean correctAnswer1a = correct1a.isChecked();
        CheckBox correct1b = (CheckBox) findViewById(R.id.correct1b);
        boolean correctAnswer1b = correct1b.isChecked();
        CheckBox wrong1a = (CheckBox) findViewById(R.id.wrong1a);
        boolean wrongAnswer1a = wrong1a.isChecked();
        CheckBox wrong1b = (CheckBox) findViewById(R.id.wrong1b);
        boolean wrongAnswer1b = wrong1b.isChecked();
        if (correctAnswer1a && correctAnswer1b && !wrongAnswer1a && !wrongAnswer1b) {
            score += 1;
        }

        CheckBox correct2a = (CheckBox) findViewById(R.id.correct2a);
        boolean correctAnswer2a = correct2a.isChecked();
        CheckBox correct2b = (CheckBox) findViewById(R.id.correct2b);
        boolean correctAnswer2b = correct2b.isChecked();
        CheckBox wrong2a = (CheckBox) findViewById(R.id.wrong2a);
        boolean wrongAnswer2a = wrong2a.isChecked();
        CheckBox wrong2b = (CheckBox) findViewById(R.id.wrong2b);
        boolean wrongAnswer2b = wrong2b.isChecked();
        if (correctAnswer2a && correctAnswer2b && !wrongAnswer2a && !wrongAnswer2b) {
            score += 1;
        }

        //Determine whether one or more of these keywords are used in the answer (question 3 & 4)
        EditText response3 = (EditText) findViewById(R.id.response3);
        String writ3 = response3.getText().toString().toLowerCase();
        if (writ3.contains(getString(R.string.answer3_goethe)) ||
                writ3.contains(getString(R.string.answer3_poem)) ||
                writ3.contains(getString(R.string.answer3_apprentice)) ||
                writ3.contains(getString(R.string.answer3_sorcerer))) {
            score += 1;
        }

        EditText response4 = (EditText) findViewById(R.id.response4);
        String writ4 = response4.getText().toString().toLowerCase();
        if (writ4.contains(getString(R.string.answer4_folksong)) ||
                writ4.contains(getString(R.string.answer4_round)) ||
                writ4.contains(getString(R.string.answer4_brother)) ||
                writ4.contains(getString(R.string.answer4_jacob))) {
            score += 1;
        }

        //Determine whether correct the answer has been selected (question 5 & 6)
        RadioButton correct5 = (RadioButton) findViewById(R.id.correct5);
        boolean correctAnswer5 = correct5.isChecked();
        if (correctAnswer5) {
            score += 1;
        }
        RadioButton correct6 = (RadioButton) findViewById(R.id.correct6);
        boolean correctAnswer6 = correct6.isChecked();
        if (correctAnswer6) {
            score += 1;

        }
        //Write toast message indicating amount of correct answers.
        Toast.makeText(this, getString(R.string.toast_part1) + score +
                getString(R.string.toast_part2), Toast.LENGTH_SHORT).show();
        score = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Release MediaPlayer When the activity is stopped.
        releaseMediaPlayer();
    }

    //Clean up the media player by releasing its resources.
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Release the MediaPlayer resources.
            mMediaPlayer.release();

            // Reset MediaPlayer back to null.
            mMediaPlayer = null;

            //Abandon focus whether it was granted or not.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}