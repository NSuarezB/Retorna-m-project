package com.example.retornam20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<Map<String, Object>> localDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public ChatAdapter(List<Map<String, Object>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_prestec, parent, false);

        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder viewHolder, final int position) {
        Map<String, Object> chat = localDataSet.get(position);
        String emisor = (String) chat.get("emisor");
        String receptor = (String) chat.get("receptor");
        String id = (String) chat.get("id");


    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Context context;

        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            textView = view.findViewById(R.id.titolObjecte);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
