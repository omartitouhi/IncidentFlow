package com.omartitouhi.incidentflow.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omartitouhi.incidentflow.R;
import com.omartitouhi.incidentflow.data.model.IncidentListItem;

import java.util.ArrayList;
import java.util.List;

public class IncidentListAdapter extends RecyclerView.Adapter<IncidentListAdapter.ViewHolder> {

    private final List<IncidentListItem> items = new ArrayList<>();

    public void submitList(List<IncidentListItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleView;
        private final TextView categoryView;
        private final TextView priorityView;
        private final TextView statusView;
        private final TextView dateView;
        private final TextView locationView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.incidentItemTitle);
            categoryView = itemView.findViewById(R.id.incidentItemCategory);
            priorityView = itemView.findViewById(R.id.incidentItemPriority);
            statusView = itemView.findViewById(R.id.incidentItemStatus);
            dateView = itemView.findViewById(R.id.incidentItemDate);
            locationView = itemView.findViewById(R.id.incidentItemLocation);
        }

        void bind(IncidentListItem item) {
            titleView.setText(item.getTitle());
            categoryView.setText(itemView.getContext().getString(R.string.incident_list_category_value, item.getCategory()));
            priorityView.setText(itemView.getContext().getString(R.string.incident_list_priority_value, item.getPriority()));
            statusView.setText(itemView.getContext().getString(R.string.incident_list_status_value, item.getStatus()));
            dateView.setText(itemView.getContext().getString(R.string.incident_list_date_value, item.getDateText()));
            locationView.setText(itemView.getContext().getString(R.string.incident_list_location_value, item.getLocation()));
        }
    }
}

