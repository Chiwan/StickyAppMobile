package info.androidhive.materialdesign.activity;

import android.app.MediaRouteButton;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.view.DrawingView;

public class SendNotesActivity extends AppCompatActivity {

    private ImageButton paletteButton;
    private float smallBrush, mediumBrush, largeBrush;
    private LinearLayout colorPalette;
    private ImageButton brushButton;
    private int usePaletteColor; // 0 if not open, 1 if open with paletteButton, 2 if open with brushButton
    private ImageButton textButton;
    private ImageButton closeButton;
    private ImageButton sendButton;
    private EditText note;
    private DrawingView drawView;
    private ImageButton firstColorBrush;
    private ImageButton secondColorBrush;
    private ImageButton thirdColorBrush;
    private ImageButton fourthColorBrush;
    private LinearLayout bottomSendLayout;
    private LinearLayout topSendLayout;
    private ImageButton photoButton;
    private ImageButton eraserButton;
    private ImageButton currPaint;
    private Context context;
    private int PICK_IMAGE_REQUEST = 1;
    private boolean dropPicture = false;
    private ImageView backgroundSend;
    private ImageButton fifthColorBrush;
    private ImageButton sixththColorBrush;
    private LinearLayout colorBackgroundLayout;
    private ImageButton firstColorBackground;
    private ImageButton secondColorBackground;
    private ImageButton thirdColorBackground;
    private ImageButton fourthColorBackground;
    private ImageButton fifthColorBackground;
    private ImageButton sixththColorBackground;
    private LinearLayout paletteLayout;
    private LinearLayout pictureFilesLayout;
    private String idWall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notes);
        usePaletteColor=0;

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        paletteLayout = (LinearLayout) findViewById(R.id.paletteLayout);
        pictureFilesLayout = (LinearLayout) findViewById(R.id.pictureFilesLayout);

        bottomSendLayout = (LinearLayout) findViewById(R.id.bottomSend);
        topSendLayout = (LinearLayout) findViewById(R.id.topSend);
        colorPalette = (LinearLayout) findViewById(R.id.colorLayout);
        paletteButton = (ImageButton) findViewById(R.id.paletteButton);
        brushButton = (ImageButton) findViewById(R.id.brushButton);
        textButton = (ImageButton) findViewById(R.id.textButton);
        eraserButton = (ImageButton) findViewById(R.id.eraserButton);
        note= (EditText) findViewById(R.id.noteEditText);
        colorBackgroundLayout = (LinearLayout) findViewById(R.id.colorBackgroundLayout);
        firstColorBrush = (ImageButton) findViewById(R.id.firstColorBrush);
        secondColorBrush = (ImageButton) findViewById(R.id.secondColorBrush);
        thirdColorBrush = (ImageButton) findViewById(R.id.thirdColorBrush);
        fourthColorBrush = (ImageButton) findViewById(R.id.fourthColorBrush);
        fifthColorBrush = (ImageButton) findViewById(R.id.fifthColorBrush);
        sixththColorBrush = (ImageButton) findViewById(R.id.sixthColorBrush);
        firstColorBackground = (ImageButton) findViewById(R.id.firstColorBackground);
        secondColorBackground = (ImageButton) findViewById(R.id.secondColorBackground);
        thirdColorBackground = (ImageButton) findViewById(R.id.thirdColorBackground);
        fourthColorBackground = (ImageButton) findViewById(R.id.fourthColorBackground);
        fifthColorBackground = (ImageButton) findViewById(R.id.fifthColorBackground);
        sixththColorBackground = (ImageButton) findViewById(R.id.sixthColorBackground);
        drawView = (DrawingView)findViewById(R.id.drawing);
        drawView.setBrushSize(smallBrush);
        currPaint = (ImageButton) colorPalette.getChildAt(0);
        photoButton = (ImageButton) findViewById(R.id.photoButton);
        backgroundSend = (ImageView) findViewById(R.id.backgroundSend);
        backgroundSend.setOnTouchListener(new MyTouchListener());



        //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        this.context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idWall = extras.getString("idWall");
        }



        photoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        paletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                if(colorBackgroundLayout.getVisibility()==View.GONE && usePaletteColor==0){
                    //colorPalette.setVisibility(View.VISIBLE);
                    System.out.println("Palette background Visible");
                    colorBackgroundLayout.setVisibility(View.VISIBLE);
                    usePaletteColor=1;
                    buttonUse(paletteButton);
                }else if(colorBackgroundLayout.getVisibility()==View.VISIBLE && usePaletteColor==1){
                    //colorPalette.setVisibility(View.GONE);
                    System.out.println("Palette background Gone");
                    colorBackgroundLayout.setVisibility(View.GONE);
                    usePaletteColor=0;
                    buttonUse();
                }else{
                    //do nothing for the moment
                    usePaletteColor=1;
                    colorBackgroundLayout.setVisibility(View.VISIBLE);
                    buttonUse(paletteButton);
                    System.out.println("Palette background do nothing");
                }
                colorPalette.setVisibility(View.GONE);
                eraserButton.setVisibility(View.GONE);
                noteWritten();
                resetColor();
                drawView.setColor("#00000000");
                System.out.println("Palette background end");
            }
        });

        brushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                eraserButton.setVisibility(View.VISIBLE);
                if((colorPalette.getVisibility()==View.GONE || colorBackgroundLayout.getVisibility()==View.VISIBLE) && usePaletteColor==0){
                    colorPalette.setVisibility(View.VISIBLE);
                    usePaletteColor = 2;
                    buttonUse(brushButton);
                    System.out.println("Brush Visible");
                }else if(colorPalette.getVisibility()==View.VISIBLE && usePaletteColor==2){
                    colorPalette.setVisibility(View.GONE);
                    eraserButton.setVisibility(View.GONE);
                    usePaletteColor =0;
                    System.out.println("Brush Gone");
                    buttonUse();
                }else{
                    //do nothing for the moment
                    usePaletteColor=2;
                    colorPalette.setVisibility(View.VISIBLE);
                    buttonUse(brushButton);
                    System.out.println("Brush nothing");

                }
                colorBackgroundLayout.setVisibility(View.GONE);
                noteWritten();
                resetColor();
                drawView.setColor("#000000");
                System.out.println("Brush Gone");
            }
        });


        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                if((colorPalette.getVisibility()==View.GONE || colorBackgroundLayout.getVisibility()==View.VISIBLE) && usePaletteColor==0){
                    colorPalette.setVisibility(View.VISIBLE);
                    usePaletteColor=3;
                    buttonUse(textButton);
                    note.setVisibility(View.VISIBLE);
                    note.setFocusableInTouchMode(true);
                    System.out.println("Text visible");
                }else if(colorPalette.getVisibility()==View.VISIBLE && usePaletteColor==3){
                    colorPalette.setVisibility(View.GONE);
                    usePaletteColor = 0;
                    buttonUse();
                    noteWritten();
                }else{
                    //do nothing for the moment
                    usePaletteColor=3;
                    colorPalette.setVisibility(View.VISIBLE);
                    note.setVisibility(View.VISIBLE);
                    note.setFocusableInTouchMode(true);
                    buttonUse(textButton);
                }
                colorBackgroundLayout.setVisibility(View.GONE);
                eraserButton.setVisibility(View.GONE);
                resetColor();
                drawView.setColor("#00000000");
            }
        });

        closeButton = (ImageButton) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), "You send a note", duration);
                toast.show();
                finish();
            }
        });

        eraserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(largeBrush);
                buttonUse(eraserButton);
            }
        });

        sendButton.setOnTouchListener(new MyTouchListener());
        //drawView.setOnTouchListener(new MyTouchListener());
        topSendLayout.setOnDragListener(new MyDragListener());


    }

    public void buttonUse(ImageButton imB){
        buttonUse();
        String color = "#4f000000";
        imB.setBackgroundColor(Color.parseColor(color));
    }
    public void buttonUse(){
        textButton.setBackground(getResources().getDrawable(android.R.color.transparent));
        brushButton.setBackground(getResources().getDrawable(android.R.color.transparent));
        paletteButton.setBackground(getResources().getDrawable(android.R.color.transparent));
        eraserButton.setBackground(getResources().getDrawable(android.R.color.transparent));
    }

    public boolean noteWritten(){
        if(note.getText().toString().isEmpty()){
            note.setVisibility(View.GONE);
            note.setFocusableInTouchMode(false);
            return false;
        }
        return true;
    }

    public void paintClicked(View view){

        if(usePaletteColor==2) {
            //ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
        if(usePaletteColor==1){
            String color = view.getTag().toString();
            drawView.setBackgroundColor(Color.parseColor(color));
            drawView.setColor("#00000000");

        }
        if(usePaletteColor==3){
            String color = view.getTag().toString();
            note.setTextColor(Color.parseColor(color));
            drawView.setColor("#00000000");
        }
        //use chosen color
        resetColor();
        //(ImageView)view.setImageResource(Color.parseColor("#4f000000"));
        ImageView imageView = (ImageView)view;
        imageView.setImageResource(R.drawable.valide_blanc);

    }
    public void resetColor(){
        firstColorBrush.setImageResource(android.R.color.transparent);
        secondColorBrush.setImageResource(android.R.color.transparent);
        thirdColorBrush.setImageResource(android.R.color.transparent);
        fourthColorBrush.setImageResource(android.R.color.transparent);
        fifthColorBrush.setImageResource(android.R.color.transparent);
        sixththColorBrush.setImageResource(android.R.color.transparent);
    }

    public void resetBackgroundColor(){
        firstColorBackground.setImageResource(android.R.color.transparent);
        secondColorBackground.setImageResource(android.R.color.transparent);
        thirdColorBackground.setImageResource(android.R.color.transparent);
        fourthColorBackground.setImageResource(android.R.color.transparent);
        fifthColorBackground.setImageResource(android.R.color.transparent);
        sixththColorBackground.setImageResource(android.R.color.transparent);
    }

    public void backgroundColorClicked(View view){

        if(usePaletteColor==2) {
            //ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
        if(usePaletteColor==1){
            String color = view.getTag().toString();
            drawView.setBackgroundColor(Color.parseColor(color));
            drawView.setColor("#00000000");

        }
        if(usePaletteColor==3){
            String color = view.getTag().toString();
            note.setTextColor(Color.parseColor(color));
            drawView.setColor("#00000000");
        }
        //use chosen color
        resetBackgroundColor();
        //(ImageView)view.setImageResource(Color.parseColor("#4f000000"));
        ImageView imageView = (ImageView)view;
        imageView.setImageResource(R.drawable.valide_couleur_fond);

    }

    class MyTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                //View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                //view.startDrag(data, shadowBuilder, view, 0);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(drawView);
                drawView.startDrag(data, shadowBuilder, drawView, 0);
                /*
                drawView.setVisibility(View.GONE);
                paletteLayout.setVisibility(View.GONE);
                pictureFilesLayout.setVisibility(View.GONE);
                sendButton.setVisibility(View.GONE);
                */

                topSendLayout.setVisibility(View.VISIBLE);
                bottomSendLayout.setVisibility(View.VISIBLE);
                backgroundSend.setVisibility(View.VISIBLE);
                //sendButton.setVisibility(View.GONE);

                //sendButton.setVisibility(View.GONE);
                return true;
            } else {
                //sendButton.setVisibility(View.VISIBLE);
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            LinearLayout view = (LinearLayout) v;

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    System.out.println("Drag started");

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    System.out.println("Drag entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    System.out.println("Drag exited");

                    break;
                case DragEvent.ACTION_DROP:
                    //Record picture
                    //Send picture with Asynchtask
                    eraserButton.setVisibility(View.GONE);
                    paletteLayout.setVisibility(View.GONE);
                    pictureFilesLayout.setVisibility(View.GONE);
                    sendButton.setVisibility(View.GONE);
                    colorBackgroundLayout.setVisibility(View.GONE);
                    colorPalette.setVisibility(View.GONE);
                    bottomSendLayout.setVisibility(View.INVISIBLE);
                    topSendLayout.setVisibility(View.INVISIBLE);
                    backgroundSend.setVisibility(View.INVISIBLE);

                    savePicture();
                    dropPicture=true;


                    System.out.println("Drag drop");
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(!dropPicture){
                        /*
                        eraserButton.setVisibility(View.VISIBLE);
                        drawView.setVisibility(View.VISIBLE);

                        paletteLayout.setVisibility(View.VISIBLE);
                        pictureFilesLayout.setVisibility(View.VISIBLE);
                        */

                        bottomSendLayout.setVisibility(View.INVISIBLE);
                        topSendLayout.setVisibility(View.INVISIBLE);
                        backgroundSend.setVisibility(View.INVISIBLE);
                        sendButton.setVisibility(View.VISIBLE);
                    }

                    System.out.println("Drag ended");
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void savePicture(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap myBitmap = frameLayout.getDrawingCache();
        saveBitmap(myBitmap);
    }

    private void saveBitmap(Bitmap bitmap){
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            System.out.println(filePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        new HttpUpload(context,filePath,idWall).execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("Image Request", DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ImageView imgPreview = (ImageView) findViewById(R.id.imgPreview);
            imgPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgPreview.setImageBitmap(bitmap);
            paletteButton.setVisibility(View.GONE);
            drawView.setBackground(getResources().getDrawable(android.R.color.transparent));


        }
    }


}
