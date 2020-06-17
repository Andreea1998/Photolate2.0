package com.example.photolate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;

public class SecondActivity extends AppCompatActivity {

    TextView mResult;
    ImageView mPreviewIv;
    Uri image_uri;
    Translate translate;

    private String translate(String originalText) {
        //String language = ((MainActivity)getApplicationContext()).getTranslateTo();
        String language = MainActivity.getLanguage(MainActivity.getTranslateTo());
        Translation translation = MainActivity.translate.translate(originalText, Translate.TranslateOption.targetLanguage(language), Translate.TranslateOption.model("base"));
        return translation.getTranslatedText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mResult = findViewById(R.id.result);
        mPreviewIv = findViewById(R.id.imageIv);
        Intent intent = getIntent();
        image_uri = intent.getParcelableExtra("ImageUri");

        mPreviewIv.setImageURI(image_uri);

        //get drawable bitmap for text recognition
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        String originalText ="Bine ai venit!";
        String language = MainActivity.getLanguage(MainActivity.getTranslateTo());
        System.out.println("Limba: " + language);
        String translated= translate(originalText);
        System.out.println(translated);

        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!recognizer.isOperational()) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = recognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            //get text from sb until there is no text
            for (int i = 0; i < items.size(); i++) {
                TextBlock myItem = items.valueAt(i);
                sb.append(translate(myItem.getValue()));
                sb.append("\n");
            }
            //set text to edit text
            mResult.setText(sb.toString());
        }
    }
}
