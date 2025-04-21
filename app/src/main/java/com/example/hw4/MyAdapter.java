package com.example.hw4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hw4.databinding.ItemNoteBinding;

import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> data;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button deleteButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
    
    public MyAdapter(List<String> data) {
        this.data = data;
    }
    
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new MyViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        
        // Обработчик клика для кнопки удаления
        holder.deleteButton.setOnClickListener(v -> {
            // Удаляем элемент из списка
            data.remove(position);
            // Уведомляем адаптер об удалении
            notifyItemRemoved(position);
            // Уведомляем адаптер об изменении позиций следующих элементов
            notifyItemRangeChanged(position, data.size());
        });
    }
    
    @Override
    public int getItemCount() {
        return data.size();
    }
    
    // Новый метод для добавления элемента
    public void addItem(String newItem) {
        data.add(newItem);
        notifyItemInserted(data.size() - 1); // Уведомляем адаптер о новом элементе
    }
    
    // Метод для обновления всего списка
    public void updateList(List<String> newData) {
        data = newData;
        notifyDataSetChanged(); // Уведомляем адаптер об изменениях
    }
}