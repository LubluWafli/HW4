package com.example.hw4;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw4.databinding.ActivityMainBinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> data = new ArrayList<>(); // Изменим на изменяемый ArrayList
    private int itemCounter = 1; // Счетчик для новых элементов
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Инициализация RecyclerView
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(data);
        recyclerView.setAdapter(adapter);
        
        EditText textInput = binding.textInput;
        // Обработчик кнопки
        Button addButton = binding.createNoteButton;
        addButton.setOnClickListener(v -> {
            // Добавляем новый элемент
            String newItem = String.valueOf(textInput.getText());
            adapter.addItem(newItem);
            textInput.setText("");
            // Прокручиваем к новому элементу
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });
    }
}
