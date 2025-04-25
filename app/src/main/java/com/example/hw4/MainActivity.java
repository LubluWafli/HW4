package com.example.hw4;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hw4.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Context;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "notes";
    SharedPreferences mNotes;
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
        mNotes = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        // Инициализация RecyclerView
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(data);
        recyclerView.setAdapter(adapter);
        
        for (int i = 1; i < mNotes.getInt("itemNum",0) + 1; i++)
        {
            adapter.addItem(mNotes.getString(String.valueOf(i), "null"));
        }
        
        EditText textInput = binding.textInput;
        // Обработчик кнопки
        FloatingActionButton addButton = binding.createNoteButton;
        addButton.setOnClickListener(v -> {
            // Добавляем новый элемент
            String newItem = String.valueOf(textInput.getText());
            adapter.addItem(newItem);
            
            SharedPreferences.Editor editor = mNotes.edit();
            editor.putString(String.valueOf(adapter.getItemCount()), newItem);
            editor.putInt("itemNum", adapter.getItemCount());
            editor.apply();
            textInput.setText("");
            // Прокручиваем к новому элементу
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });
    }

}
