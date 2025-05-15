package com.example.hw4;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hw4.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Context;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.Manifest;



public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "notes";
    SharedPreferences mNotes;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> data = new ArrayList<>(); // Изменим на изменяемый ArrayList
    private int itemCounter = 1; // Счетчик для новых элементов
    private ActivityMainBinding binding;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 42;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mNotes = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        
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
            showNotification(newItem);
        });
    }
    private void createNotificationChannel() {
        String name = "Name";
        String descriptionText = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(descriptionText);
        
        // Регистрируем канал в системе
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    private void showNotification(String noteDescription) {
        // Создаем NotificationChannel, но это делается только для API 26+
        // Потому что NotificationChannel -- это новый класс и его нет в support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        // Все цветные иконки отображаются только в оттенках серого
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Заметка создана!")
                .setContentText(noteDescription)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_ID); // константа вашего выбора
            }
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }
}
