package com.hmtbasdas.bseuot.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hmtbasdas.bseuot.Listeners.QuestionListener;
import com.hmtbasdas.bseuot.Models.Question;
import com.hmtbasdas.bseuot.databinding.QuestionItemBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    private final ArrayList<Question> questionArrayList;
    private final QuestionListener questionListener;

    public QuestionAdapter(ArrayList<Question> questionArrayList, QuestionListener questionListener) {
        this.questionArrayList = questionArrayList;
        this.questionListener = questionListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        QuestionItemBinding binding = QuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.setData(questionArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        QuestionItemBinding binding;

        public MyViewHolder(@NonNull @NotNull QuestionItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void setData(Question question){
            binding.questionTITLE.setText(question.getQuestionTITLE());
            binding.questionTEXT.setText(question.getQuestionTEXT());
            binding.questionDATE.setText(getReadableDateTime(new Date(question.getQuestionDATE())));
            binding.getRoot().setOnClickListener(v -> questionListener.onQuestionClicked(question));
            binding.reportQuestion.setOnClickListener(v -> questionListener.onReportQuestionClicked(question));
        }

        private String getReadableDateTime(Date date){
            return new SimpleDateFormat("dd/MM/yyyy, EEEE", Locale.getDefault()).format(date);
        }
    }
}
