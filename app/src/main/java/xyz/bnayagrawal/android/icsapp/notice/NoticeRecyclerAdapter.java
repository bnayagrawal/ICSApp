package xyz.bnayagrawal.android.icsapp.notice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import xyz.bnayagrawal.android.icsapp.R;

/**
 * Created by binay on 11/2/2017.
 */

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<NoticeData> nd;

    public NoticeRecyclerAdapter(Context context,List<NoticeData> nd) {
        this.context = context;
        this.nd = nd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notice_card_template, viewGroup, false);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final NoticeData noticeData = nd.get(position);
        viewHolder.notice_title.setText(noticeData.getTitle());

        //limit 100 words for short description to show
        StringTokenizer st = new StringTokenizer(noticeData.getText()," ");
        String shortDesc = "";
        for(int i = 0; i < st.countTokens(); i++)
            if(i < 100)
                shortDesc += st.nextToken() + " ";
            else break;
        shortDesc = shortDesc.trim() + "...";
        viewHolder.notice_content.setText(shortDesc);
        //if doesn't contain attachment hide
        /*if(!noticeData.hasAttachment())
            viewHolder.notice_attachment.setVisibility(View.INVISIBLE);*/

        //TODO: if date is less than 24 hour or less than a month then dont show actual date
        viewHolder.notice_time.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(noticeData.getCreated_at()));
        viewHolder.notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,NoticeDetailActivity.class);
                intent.putExtra("title",noticeData.getTitle());
                intent.putExtra("text",noticeData.getText());
                intent.putExtra("created_at",noticeData.getCreated_at());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nd.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView notice_title;
        private TextView notice_content;
        private TextView notice_time;
        private LinearLayout notice_attachment;
        private View notice;

        public ViewHolder(View itemView) {
            super(itemView);
            notice_title = itemView.findViewById(R.id.notice_card_title);
            notice_content = itemView.findViewById(R.id.notice_card_content);
            notice_time = itemView.findViewById(R.id.notice_card_time);
            notice_attachment = itemView.findViewById(R.id.notice_card_attachment);
            notice = itemView;

            //add onClick listener to view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: open notice detail activity
                }
            });
        }
    }
}
