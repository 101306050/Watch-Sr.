package com.baobomb.watch.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baobomb.watch.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Baobomb on 15/7/22.
 */
public class TrackItemCard extends Card {
    //Wear data
    TextView id;
    String trackId;

    //Constructor
    public TrackItemCard(Context context, String trackId) {
        this(context, R.layout.card_track);
        this.trackId = trackId;
    }

    //Father Constructor
    private TrackItemCard(Context context, int LayoutId) {
        super(context, LayoutId);
    }

    //Set child view of this card view
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        id = (TextView) parent.findViewById(R.id.id);
        if (trackId != null) {
            id.setText("裝置ID : "+ trackId);
        }
    }
}