package com.hmtbasdas.bseuot.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hmtbasdas.bseuot.Listeners.ReportConfessionListener;
import com.hmtbasdas.bseuot.Models.Confession;
import com.hmtbasdas.bseuot.databinding.ConfessionItemBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ConfessionAdapter extends RecyclerView.Adapter<ConfessionAdapter.MyViewHolder> {

    private final ArrayList<Confession> confessionArrayList;
    private final ReportConfessionListener reportConfessionListener;

    public ConfessionAdapter(ArrayList<Confession> confessionArrayList, ReportConfessionListener reportConfessionListener) {
        this.confessionArrayList = confessionArrayList;
        this.reportConfessionListener = reportConfessionListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ConfessionItemBinding confessionItemBinding = ConfessionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(confessionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.setData(confessionArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return confessionArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ConfessionItemBinding binding;

        public MyViewHolder(@NonNull @NotNull ConfessionItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void setData(Confession confession){
            binding.confessionContent.setText(confession.getConfessionCONTENT());
            binding.confessionDate.setText(getReadableDateTime(new Date(confession.getConfessionDATE())));
            binding.reportConfession.setOnClickListener(v -> reportConfessionListener.onClickReportConfession(confession));
        }

        private String getReadableDateTime(Date date){
            return new SimpleDateFormat("dd/MM/yyyy, EEEE", Locale.getDefault()).format(date);
        }
    }
}
