package com.example.tarefas;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TarefasPrefs";  // Nome do arquivo de preferências
    private static final String KEY_TAREFAS = "tarefas";  // Chave para armazenar as tarefas
    private ArrayList<String> tarefas;  // Lista para armazenar as tarefas
    private TarefaAdapter tarefaAdapter;  // Adapter para o RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Alterar a cor da barra de status (onde ficam as horas e data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_navy));
        }

        // Inicializar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar a lista de tarefas
        tarefas = carregarTarefas();

        // Configurar o Adapter
        tarefaAdapter = new TarefaAdapter(tarefas, position -> {
            // Remove a tarefa ao clicar e notifica a exclusão
            tarefas.remove(position);
            salvarTarefas(tarefas);
            tarefaAdapter.notifyItemRemoved(position);
            Toast.makeText(MainActivity.this, "Tarefa excluída", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(tarefaAdapter);

        // Vincular os elementos do layout
        EditText editTextTask = findViewById(R.id.editTextTask);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);

        // Ação do botão para adicionar tarefa
        buttonAddTask.setOnClickListener(v -> {
            String task = editTextTask.getText().toString().trim();

            if (!task.isEmpty()) {
                tarefas.add(task);
                salvarTarefas(tarefas);
                tarefaAdapter.notifyItemInserted(tarefas.size() - 1);
                editTextTask.setText("");
                Toast.makeText(MainActivity.this, "Tarefa adicionada: " + task, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Digite uma nova tarefa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Função para carregar as tarefas do SharedPreferences
    private ArrayList<String> carregarTarefas() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> tarefasSet = prefs.getStringSet(KEY_TAREFAS, new HashSet<>());
        return new ArrayList<>(tarefasSet);
    }

    // Função para salvar as tarefas no SharedPreferences
    private void salvarTarefas(ArrayList<String> tarefas) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(KEY_TAREFAS, new HashSet<>(tarefas));
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        salvarTarefas(tarefas);
    }

}

