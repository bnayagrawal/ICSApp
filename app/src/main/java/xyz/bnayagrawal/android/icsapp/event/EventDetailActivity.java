package xyz.bnayagrawal.android.icsapp.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.bnayagrawal.android.icsapp.R;

public class EventDetailActivity extends AppCompatActivity {

    private TextView textViewEventDescription;
    private ImageView imageViewEventImage;
    private TextView textViewPhone;
    private TextView textViewVenue;
    private TextView textViewDate;
    private TextView textViewCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewEventDescription = (TextView) findViewById(R.id.eda_description);
        imageViewEventImage = (ImageView) findViewById(R.id.event_detail_backdrop);
        textViewVenue = (TextView) findViewById(R.id.eda_venue);
        textViewDate = (TextView) findViewById(R.id.eda_date);
        textViewPhone = (TextView) findViewById(R.id.eda_phone);
        textViewCreatedAt = (TextView) findViewById(R.id.eda_created_at);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("title"));
            textViewEventDescription.setText(bundle.getString("description"));

            Picasso.with(getApplicationContext()).load(bundle.getString("image_url")).into(imageViewEventImage,new Callback(){
                @Override
                public void onSuccess() {
                    //no use
                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(R.drawable.image_event_default).into(imageViewEventImage);
                    Toast.makeText(getApplicationContext(),"Failed to load image !",Toast.LENGTH_SHORT).show();
                }
            });

            textViewCreatedAt.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(bundle.getSerializable("created_at")));
            textViewPhone.setText(bundle.getString("phone"));
            textViewVenue.setText(bundle.getString("venue"));
            try {
                Date start_time = (Date) bundle.getSerializable("start_time");
                Date end_time = (Date) bundle.getSerializable("end_time");
                String date = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(start_time) + " - " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(end_time);
                textViewDate.setText(date);
            }
            catch (Exception ex) {
                textViewDate.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format((Date) bundle.getSerializable("start_time")));
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No data recieved!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
