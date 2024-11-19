package com.example.tarefas;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private List<String> listaDeTarefas; // Lista de tarefas
    private OnTarefaClickListener onTarefaClickListener;

    // Interface para ouvir o clique na tarefa
    public interface OnTarefaClickListener {
        void onTarefaClick(int position);
    }

    // Construtor
    public TarefaAdapter(List<String> listaDeTarefas, OnTarefaClickListener listener) {
        this.listaDeTarefas = listaDeTarefas;
        this.onTarefaClickListener = listener;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        holder.bind(listaDeTarefas.get(position));
    }

    @Override
    public int getItemCount() {
        return listaDeTarefas.size();
    }

    class TarefaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTarefa;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTarefa = itemView.findViewById(android.R.id.text1);

            // Adiciona o listener de clique para exclusão
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Deseja realmente excluir esta tarefa?")
                            .setPositiveButton("Sim", (dialog, which) -> {
                                listaDeTarefas.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(v.getContext(), "Tarefa excluída", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Não", null)
                            .show();
                }
            });
        }

        public void bind(String tarefa) {
            textViewTarefa.setText(tarefa);
        }
    }
}