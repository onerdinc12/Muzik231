package onder.com.jcplayerolucak;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcAudioPlayer;
import com.example.jean.jcplayer.JcNotificationPlayer;
import com.example.jean.jcplayer.JcPlayerService;
import com.example.jean.jcplayer.JcPlayerView;

import java.util.ArrayList;

import static com.example.jean.jcplayer.JcNotificationPlayer.NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity implements JcPlayerService.OnInvalidPathListener {
private LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
private JcPlayerView player;
private RecyclerView recyclerView;
private AudioAdapter audioAdapter;

@Override
protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        player = (JcPlayerView) findViewById(R.id.jcplayer);

        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        jcAudios.add(JcAudio.createFromURL("Sezen Aksu","http://formaal.net/sarki.mp3","GFB"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","GFB"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","GFB"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","GFB"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","KFY"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","UNİFEB"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","UNİFEB"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","Onder"));
        jcAudios.add(JcAudio.createFromURL("Göksel Rüya","http://formaal.net/sarki2.mp3","UNİFEB"));
        player.initPlaylist(jcAudios);




        player.registerInvalidPathListener(this);
        adapterSetup();
        }


public void playAudio(JcAudio jcAudio){
        player.playAudio(jcAudio);

        }

protected void adapterSetup() {
        audioAdapter = new AudioAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(audioAdapter);
        audioAdapter.setupItems(player.getMyPlaylist());
        }

@Override
public void onPause(){
        super.onPause();
        player.createNotification();

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