package com.example.capstone.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.databinding.ItemQuickActionBinding;
import com.example.capstone.model.QuickAction;

public class QuickActionAdapter extends ListAdapter<QuickAction, QuickActionAdapter.ViewHolder> {

    public interface OnActionClickListener {
        void onActionClick(QuickAction action, int position);
    }

    private final OnActionClickListener clickListener;

    private static final DiffUtil.ItemCallback<QuickAction> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<QuickAction>() {
                @Override
                public boolean areItemsTheSame(@NonNull QuickAction oldItem, @NonNull QuickAction newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }

                @Override
                public boolean areContentsTheSame(@NonNull QuickAction oldItem, @NonNull QuickAction newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public QuickActionAdapter(OnActionClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuickActionBinding binding = ItemQuickActionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuickAction action = getItem(position);
        holder.bind(action);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuickActionBinding binding;

        public ViewHolder(@NonNull ItemQuickActionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onActionClick(getItem(position), position);
                }
            });
        }

        public void bind(QuickAction action) {
            binding.tvActionLabel.setText(action.getTitle());
            binding.imgActionIcon.setImageResource(action.getIconResId());

            int tintColor = ContextCompat.getColor(
                    itemView.getContext(),
                    action.getIconTintResId()
            );
            ImageViewCompat.setImageTintList(
                    binding.imgActionIcon,
                    ColorStateList.valueOf(tintColor)
            );

            itemView.setEnabled(action.isEnabled());
            binding.tvActionLabel.setTextColor(ContextCompat.getColor(
                    itemView.getContext(),
                    action.isEnabled() ? R.color.text_primary : R.color.text_disabled
            ));
        }
    }
}
