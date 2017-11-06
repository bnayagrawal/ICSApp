package xyz.bnayagrawal.android.icsapp.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import xyz.bnayagrawal.android.icsapp.R;

public class NoticeDetailActivity extends AppCompatActivity {

    private TextView textViewText;
    private TextView textViewCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notice_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewText = (TextView) findViewById(R.id.nda_text);
        textViewCreatedAt = (TextView) findViewById(R.id.nda_created_at);

        final Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("title"));
            textViewText.setText(bundle.getString("text"));
            textViewCreatedAt.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(bundle.getSerializable("created_at")));
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
