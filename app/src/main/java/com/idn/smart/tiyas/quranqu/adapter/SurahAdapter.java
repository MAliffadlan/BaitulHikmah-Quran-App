package com.idn.smart.tiyas.quranqu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.idn.smart.tiyas.quranqu.R;
import com.idn.smart.tiyas.quranqu.model.ModelSurah;
import java.util.List;



public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    private List<ModelSurah> items;
    private SurahAdapter.onSelectData onSelectData;
    private Context mContext;

    public interface onSelectData {
        void onSelected(ModelSurah modelSurah);
    }

    public SurahAdapter(Context context, List<ModelSurah> items, SurahAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_surah, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelSurah data = items.get(position);

        holder.txtNumber.setText(data.nomor);
        holder.txtAyat.setText(data.nama);
        holder.txtInfo.setText(data.type + " - " + data.ayat + " Ayat ");
        holder.txtName.setText(data.asma);
        holder.cvSurah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectData.onSelected(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvSurah;
        public TextView txtNumber;
        public TextView txtAyat;
        public TextView txtInfo;
        public TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            cvSurah = itemView.findViewById(R.id.cvSurah);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtAyat = itemView.findViewById(R.id.txtAyat);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }

}
