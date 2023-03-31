package com.example.stage2poche;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class OfferItemViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTV, entrepriseTV, villeTV, voirTV;
    public CardView card;
    public OfferItemViewHolder(View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.offer_item_title);
        entrepriseTV = itemView.findViewById(R.id.offer_item_entreprise);
        villeTV = itemView.findViewById(R.id.offer_item_ville);
        voirTV = itemView.findViewById(R.id.offer_item_voir);
        card = itemView.findViewById(R.id.offer_item_card);
    }
}
