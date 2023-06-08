import static android.support.v4.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.app.Activity;


import com.icodechef.android.tick.TickApplication;

import java.util.ArrayList;
import java.util.List;

//public class VoiceRecognition {
//
//
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> results = data.getStringArrayListExtra(
//                    RecognizerIntent.EXTRA_RESULTS);
//            String spokenText = results.get(0);
//            // Do something with spokenText
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//}


