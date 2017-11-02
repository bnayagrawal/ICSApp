package xyz.bnayagrawal.android.icsapp.event.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.*;
import xyz.bnayagrawal.android.icsapp.event.EventData;
import xyz.bnayagrawal.android.icsapp.event.EventDetailActivity;

/**
 * Created by binay on 10/9/2017.
 */

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder>{
    //Hold the MainActivity context
    private Context context;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    List<EventData> ed;

    //public constructor to get the activity context and event data.
    public EventRecyclerAdapter(Context context,List<EventData> ed) {
        this.context = context;
        this.ed = ed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_card_template, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //this method will be called automatically by recyclerview before showing the card(list item) to user, the passed viewholder object will be populated with data to display.
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        EventData eds = ed.get(position);

        //limit 100 words for short description to show
        StringTokenizer st = new StringTokenizer(eds.getDescription()," ");
        String shortDesc = "";
        for(int i = 0; i < st.countTokens(); i++)
            if(i < 100)
                shortDesc += st.nextToken() + " ";
            else break;

        viewHolder.itemTitle.setText(eds.getTitle());
        viewHolder.itemDetail.setText(shortDesc.trim() + "...");
        //TODO: if date is less than 24 hour or less than a month then dont show actual date
        viewHolder.dates.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(eds.getNotification_date()));
        viewHolder.interested.setText(eds.getPeoples_interested() + " Intersted");
        viewHolder.going.setText(eds.getPeoples_going() + " Going");

        //if last card then set bottom margin
        if(position == ed.size() - 1) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewHolder.card.getLayoutParams();
            params.bottomMargin = (int)(8 * context.getResources().getDisplayMetrics().density);
            viewHolder.card.setLayoutParams(params);
        }

        //Set some properties of imageview (used to display event image)
        viewHolder.itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //Picasso image loading and caching framework
        //TODO: Change to string for url.
        Picasso.with(context).load(R.drawable.image_event_default).resize(640,420).centerCrop().into(viewHolder.itemImage,new Callback(){
            @Override
            public void onSuccess() {
                //hide the progress bar
                viewHolder.imageLoadProgress.setVisibility(View.GONE);
                viewHolder.itemImage.setVisibility(View.VISIBLE);

                //animation using xml resource
                Animation image_scale = AnimationUtils.loadAnimation(context, R.anim.image_scale_anim);
                viewHolder.itemImage.startAnimation(image_scale);
            }

            @Override
            public void onError() {
                viewHolder.imageLoadProgress.setVisibility(View.GONE);
                viewHolder.itemImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(R.drawable.image_event_default).resize(640,420).centerCrop().into(viewHolder.itemImage);

                //animation using xml resource
                Animation image_scale = AnimationUtils.loadAnimation(context, R.anim.image_scale_anim);
                viewHolder.itemImage.startAnimation(image_scale);

                Toast.makeText(context,"Failed to load image!",Toast.LENGTH_SHORT).show();
            }
        });

        // Item add animation
        /*if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewHolder.card.startAnimation(animation);
            lastPosition = position;
        }*/
    }

    @Override
    public int getItemCount() {
        return ed.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemDetail;
        private TextView interested;
        private TextView going;
        private TextView dates;
        public TextView view;
        private ProgressBar imageLoadProgress;
        public RecyclerView.LayoutParams lp;
        private View card;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.imageViewEvent);
            itemTitle = itemView.findViewById(R.id.textViewEventLabel);
            itemDetail = itemView.findViewById(R.id.textViewEventShortDesc);
            dates = itemView.findViewById(R.id.textViewTime);
            interested = itemView.findViewById(R.id.textViewInterested);
            going = itemView.findViewById(R.id.textViewGoing);
            view = itemView.findViewById(R.id.textViewView);
            imageLoadProgress = itemView.findViewById(R.id.imageLoadProgress);
            card = itemView;

            //add onClick listener to view
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: open event detail activity
                    Intent intent = new Intent(context,EventDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
