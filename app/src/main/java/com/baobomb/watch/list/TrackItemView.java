package com.baobomb.watch.list;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baobomb.watch.R;
import com.baobomb.watch.card.TrackItemCard;

import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by baobomb on 2015/10/8.
 */
public class TrackItemView extends RelativeLayout {

    CardViewNative cardViewNative;
    CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams
            .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public TrackItemView(Context context) {
        super(context);
        cardViewNative = new CardViewNative(context);
        this.setBackgroundResource(R.drawable.title_background);
        cardViewNative.setLayoutParams(layoutParams);
        this.addView(cardViewNative);
    }


    public void bind(String trackId) {
        TrackItemCard videoCard = new TrackItemCard(getContext(), trackId);
        cardViewNative.setCard(videoCard);
    }
}
