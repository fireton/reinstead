package ru.hugeping.reinstead;

import org.libsdl.app.SDLActivity;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.File;
import android.util.Log;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.view.accessibility.AccessibilityManager;
// import android.widget.Toast;

public class reinsteadActivity extends SDLActivity
{
	TextToSpeech tts;
	boolean ttsInitialized;
	boolean ttsStarted;
	String ttsCached;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AssetManager asset_manager = getApplicationContext().getAssets();
		try {
			String path = Assets.copyDirorfileFromAssetManager(asset_manager, "data", getFilesDir() + "/");
			Assets.copyAssetFile(asset_manager, "data/stamp", getFilesDir() + "/stamp");
			Log.v("reinstead", path);
		} catch(IOException e) {
			// Nom nom
			Log.v("reinstead", "Can't copy assets");
		}
		super.onCreate(savedInstanceState);
		ttsInitialized = false;
		ttsStarted = false;
		// Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT).show();
	}
	protected void ttsInit() {
		ttsInitialized = true;
		reinsteadActivity base = this;
		tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				ttsStarted = true;
				if (status != TextToSpeech.ERROR) {
					Log.v("reinstead", "Started TTS");
					tts.speak(ttsCached, TextToSpeech.QUEUE_FLUSH, null);
					ttsCached = "";
					//tts.setLanguage(Locale.getDefault());
				}
			}
		});
	}

	public boolean isSpeakEnabled() {
		AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
		boolean isAccessibilityEnabled = am.isEnabled();
		boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
		return isAccessibilityEnabled || isExploreByTouchEnabled;
	}

	public void Speak(String text) {
		if (!ttsInitialized)
			ttsInit();
		if (tts == null)
			return;
		if (!ttsStarted) {
			ttsCached = ttsCached + text;
			return;
		}
/*		if (tts.isSpeaking()) {
			tts.stop();
		} */
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void onDestroy(){
		if (tts !=null)
			tts.shutdown();
		super.onDestroy();
	}

	public void onPause(){
		if(tts !=null){
			tts.stop();
		}
		super.onPause();
	}
}
