package onder.com.jcplayerolucak;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.JcAudio;

import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Onder on 19.03.2017.
 */

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterViewHolder> implements View.OnClickListener{
    private Context context;
    private MainActivity activity;
    private List<JcAudio> jcAudioList;
    int durumNe;


    public AudioAdapter(MainActivity activity) {
        this.activity = activity;
        this.context = activity;
    }


    @Override
    public AudioAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.audio_item, parent, false);
        AudioAdapterViewHolder audiosViewHolder = new AudioAdapterViewHolder(view);
        audiosViewHolder.itemView.setOnClickListener(this);




        return audiosViewHolder;
    }

    @Override
    public void onBindViewHolder(AudioAdapterViewHolder holder, final int position) {
        String title = jcAudioList.get(position).getTitle();
        String yazanGrup = jcAudioList.get(position).getYazar();
        holder.audioTitle.setText(title);
        holder.audioYazar.setText(yazanGrup);



        holder.itemView.setTag(jcAudioList.get(position));

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Şarkı Sözleri");
        final TextView sarkiSozView = (TextView) dialog.findViewById(R.id.sarkiSozleri_custom);
        final String  sarkiIsim= jcAudioList.get(position).getTitle();
        final String sarkiUrl = jcAudioList.get(position).getPath();

        final String paylasimBirlestir = " " +  sarkiIsim + " " + sarkiUrl;


        //Burada share işlemleri yapılıypr
        holder.paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Tribün besteleri uygulamasi ile paylaşıldı" +  paylasimBirlestir);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);

            }
        });
        //burada indirme işlemi yapılıyor

        holder.indir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(sarkiUrl));
                enqueue = dm.enqueue(request);


                Toast.makeText(context, "İndirme İşlemi Başladı İndir Butonuna Uzun Basarak Klasörü Açabilirsiniz", Toast.LENGTH_LONG).show();

            }
        });
        holder.indir.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent i = new Intent();
                i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                context.startActivity(i);

                return false;
            }
        });
        //burada sarkisozleri uygulamasi yapılıyor
        holder.btnSarkiSozleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                 switch (position){
                     case 0:
                         sarkiSozView.setText("Onder Erdinç");
                         break;

                     case 1:
                         sarkiSozView.setText("Onder Erdinç 2");
                         break;



                 }
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return jcAudioList == null ? 0 : jcAudioList.size();
    }

    public void setupItems(List<JcAudio> jcAudioList) {
        this.jcAudioList = jcAudioList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        JcAudio JcAudio = (com.example.jean.jcplayer.JcAudio) view.getTag();
        activity.playAudio(JcAudio);




    }

    static class AudioAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView audioTitle;
        private TextView audioYazar;
        private ImageButton btnSarkiSozleri;
        private ImageButton paylas;
        private ImageButton indir;

        public AudioAdapterViewHolder(View view){
            super(view);
            this.audioTitle = (TextView) view.findViewById(R.id.audio_title);
            this.audioYazar = (TextView) view.findViewById(R.id.audio_yazari);
            this.btnSarkiSozleri = (ImageButton) view.findViewById(R.id.btnSarkiSozleri);
            this.paylas = (ImageButton) view.findViewById(R.id.paylas);
            this.indir = (ImageButton) view.findViewById(R.id.indir);




        }




    }
    private long enqueue;
    private DownloadManager dm;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {


                    }
                }
            }
        }
    };
}
