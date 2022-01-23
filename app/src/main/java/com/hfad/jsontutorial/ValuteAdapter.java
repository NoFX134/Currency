package com.hfad.jsontutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//Адаптер для RecycleView
public class ValuteAdapter  extends RecyclerView.Adapter<ValuteAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Valutes> valutes;

    ValuteAdapter(Context context, List<Valutes> valutes) {
        this.valutes = valutes;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ValuteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ValuteAdapter.ViewHolder holder, int position) {
        Valutes valute = valutes.get(position);
        holder.charcodeView.setText(valute.getCharCode());
        holder.nameView.setText(valute.getNominal()+" "+ valute.getName());
        holder.valueView.setText(Double.toString(valute.getValue())+" RUB");



    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView charcodeView, nameView,  valueView;
        ViewHolder(View view){
            super(view);
            charcodeView = view.findViewById(R.id.charcode);
            nameView = view.findViewById(R.id.name);
            valueView = view.findViewById(R.id.value);
        }
    }
}