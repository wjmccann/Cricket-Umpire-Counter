package com.wjmccann.cricketumpirecounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

public class Counter extends AppCompatActivity {

    public int myBalls = 0;
    public int overs = 0;
    public int wickets = 0;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        final Button clickBalls = (Button) findViewById(R.id.clickBalls);

        mDrawerList = (ListView)findViewById(R.id.navList);

        addDrawerItems();

        clickBalls.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                myBalls--;

                if(myBalls < 0){
                    myBalls = 5;
                    overs--;
                    if(overs < 0){
                        overs = 0;
                        myBalls = 0;
                    }
                    TextView oversText = (TextView) findViewById(R.id.oversText);
                    oversText.setText(Integer.toString(overs));
                }

                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                String ballText = Integer.toString(myBalls);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                clickBalls.startAnimation(myAnim);
                ((Button) v).setText(ballText);
                vibrator.vibrate(100);
                return true;
            }
        });
    }

    public void addDrawerItems() {
        String[] osArray = { "Instructions", "Colours", "Rate Me!", "About" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        AlertDialog instrDialog = new AlertDialog.Builder(Counter.this).create();
                        instrDialog.setTitle("Instructions");
                        instrDialog.setMessage("Tap the Ball number to count balls.\n" +
                                "\n" +
                                "Hold the Ball number to subtract balls.\n" +
                                "\n" +
                                "Tap the Wickets number to add wickets.");
                        instrDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        instrDialog.show();
                        break;
                    case 1:
                        ColorChooserDialog dialog = new ColorChooserDialog(Counter.this);
                        dialog.setTitle("Choose color");
                        dialog.setColorListener(new ColorListener() {
                            @Override
                            public void OnColorClick(View v, int color) {
                                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                                RelativeLayout layout=(RelativeLayout) findViewById(R.id.activity_counter);
                                layout.setBackgroundColor(color);
                            }
                        });
                        //customize the dialog however you want
                        dialog.show();
                        break;
                    case 2:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.wjmccann.cricketumpirecounter")));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.wjmccann.cricketumpirecounter")));
                        }
                        break;
                    case 3:
                        AlertDialog aboutDialog = new AlertDialog.Builder(Counter.this).create();
                        aboutDialog.setTitle("Cricket Umpire Counter");
                        aboutDialog.setMessage("Version 1.1");
                        aboutDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        aboutDialog.show();
                        break;
                }
            }
        });
    }

    public void myMethod(View v){
        DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(mDrawerList);
    }

    public void ballClick(View v) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        myBalls++;
        Button clickBalls = (Button) findViewById(R.id.clickBalls);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        String ballText = Integer.toString(myBalls);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        clickBalls.startAnimation(myAnim);
        ((Button) v).setText(ballText);
        vibrator.vibrate(50);

        if (myBalls == 6) {
            overs++;
            myBalls = 0;
            TextView oversText = (TextView) findViewById(R.id.oversText);
            oversText.setText(Integer.toString(overs));
            ((Button) v).setText(Integer.toString(myBalls));

            long pattern[] = {100, 100, 100, 100, 100, 100, 100, 100, 100};
            vibrator.vibrate(pattern, -1);

            Toast myToast = Toast.makeText(getApplicationContext(), "End of Over", Toast.LENGTH_LONG);
            myToast.show();
        }
    }

    public void wicketClick(View v) {
        wickets++;
        TextView wicketText = (TextView) findViewById(R.id.wicketText);
        wicketText.setText(Integer.toString(wickets));

        if (wickets == 11) {
            wickets = 0;
            wicketText.setText(Integer.toString(wickets));
        }
        if (wickets == 10){
            Toast wToast = Toast.makeText(getApplicationContext(), "End of Innings", Toast.LENGTH_LONG);
            wToast.show();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Counter Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
