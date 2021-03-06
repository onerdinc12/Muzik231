package onder.com.jcplayerolucak;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcAudioPlayer;
import com.example.jean.jcplayer.JcNotificationPlayer;
import com.example.jean.jcplayer.JcPlayerService;
import com.example.jean.jcplayer.JcPlayerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.jean.jcplayer.JcNotificationPlayer.NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity implements JcPlayerService.OnInvalidPathListener {
private LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
private JcPlayerView player;
private RecyclerView recyclerView;
private AudioAdapter audioAdapter;
        InterstitialAd mInterstitialAd;
        private static final String rssFeed = "http://formaal.net/jsonparsing.txt";
        private static final String ARRAY_NAME = "student";
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String URL = "url";
        private static final String YAZAR = "yazar";
        private static final String BIRTDATE = "birthdate";
        List<Item> arrayOfList;
        ArrayList<JcAudio> jcAudios = new ArrayList<>();
@Override
protected void onCreate(Bundle savedInstanceState) {

        mInterstitialAd = new InterstitialAd(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        player = (JcPlayerView) findViewById(R.id.jcplayer);


        mInterstitialAd.setAdUnitId("ca-app-pub-2542754690371777/3865915602"); //reklam id


        mInterstitialAd.setAdListener(new AdListener() { //reklamımıza listener ekledik
                @Override
                public void onAdClosed() { //reklam kapatıldığı zaman tekrardan reklamın yüklenmesi için
                        requestNewInterstitial();
                }
        });

        requestNewInterstitial();



        if (Utils.isNetworkAvailable(MainActivity.this)) {
                new MyTask().execute(rssFeed);
        } else {
                Toast.makeText(this, "İnternet Bağlantısı Yok", Toast.LENGTH_SHORT).show();
        }

        arrayOfList = new ArrayList<>();




        }
        private void requestNewInterstitial() { //Test cihazı Admob dan ban yememek için
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("3FDFC899E76D396E6819BB74CE419F47")
                        .build();

                mInterstitialAd.loadAd(adRequest);
        }


public void playAudio(JcAudio jcAudio){
        player.playAudio(jcAudio);

        }
        class MyTask extends AsyncTask<String, Void, String> {

                ProgressDialog pDialog;

                @Override
                protected void onPreExecute() {
                        super.onPreExecute();

                        pDialog = new ProgressDialog(MainActivity.this);
                        pDialog.setMessage("Loading...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                }

                @Override
                protected String doInBackground(String... params) {
                        return Utils.getJSONString(params[0]);
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (null != pDialog && pDialog.isShowing()) {
                                pDialog.dismiss();
                        }

                        if (null == result || result.length() == 0) {

                                MainActivity.this.finish();
                        } else {

                                try {
                                        JSONObject mainJson = new JSONObject(result);
                                        JSONArray jsonArray = mainJson.getJSONArray(ARRAY_NAME);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject objJson = jsonArray.getJSONObject(i);

                                                Item objItem = new Item();

                                                objItem.setId(objJson.getInt(ID));
                                                objItem.setName(objJson.getString(NAME));
                                                objItem.setCity(objJson.getString(URL));
                                                objItem.setGender(objJson.getString(YAZAR));
                                                objItem.setBirthdate(objJson.getString(BIRTDATE));


                                                arrayOfList.add(objItem);

                                        }
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                // check data...
                                for(int i =0;i<arrayOfList.size();i++){
                                        Item items = arrayOfList.get(i);
                                        jcAudios.add(JcAudio.createFromURL(items.getName(),items.getCity(),items.getGender(),items.getBirthdate()));
                                }



                                player.initPlaylist(jcAudios);
                                player.registerInvalidPathListener(MainActivity.this);
                                adapterSetup();





                        }

                }
        }

protected void adapterSetup() {
        audioAdapter = new AudioAdapter(this,mInterstitialAd);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(audioAdapter);
        audioAdapter.setupItems(player.getMyPlaylist());
        }




@Override
public void onPause(){
        super.onPause();
        player.createNotification();

        }


        int count =0;
        @Override
        public void onBackPressed() {
                count++;
                Toast.makeText(this, "Çıkmak için 2 kez basın", Toast.LENGTH_SHORT).show();

                if(count > 1){
                        super.onBackPressed();
                }
        }

        @Override
protected void onDestroy() {
        NotificationManager notifManager= (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        super.onDestroy();
        player.kill();



        }

@Override
public void onPathError(JcAudio jcAudio) {
        Toast.makeText(this, jcAudio.getPath() + " with problems", Toast.LENGTH_LONG).show();

        }
        }