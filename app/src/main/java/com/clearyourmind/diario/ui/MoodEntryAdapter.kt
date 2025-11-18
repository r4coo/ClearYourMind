package com.clearyourmind.diario.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clearyourmind.diario.R
import com.clearyourmind.diario.data.MoodEntry
import com.clearyourmind.diario.databinding.ListItemEntryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MoodEntryAdapter : ListAdapter<MoodEntry, MoodEntryAdapter.EntryViewHolder>(EntriesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ListItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EntryViewHolder(private val binding: ListItemEntryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        fun bind(entry: MoodEntry) {
            binding.entryDate.text = dateFormatter.format(Date(entry.timestamp))
            binding.entryMoodScore.text = binding.root.context.getString(R.string.mood_display_format, entry.moodScore)
            binding.entryReflection.text = entry.reflection

            // Muestra la imagen solo si existe la ruta
            binding.entryImage.isVisible = entry.imagePath != null
            if (binding.entryImage.isVisible) {
                // Nota: En una app real, aquí usarías una librería como Glide o Coil para cargar la imagen desde 'entry.imagePath'
                binding.entryImage.setImageResource(R.drawable.placeholder_image)
            }

            // Muestra la ubicación solo si existe
            binding.entryLocation.isVisible = !entry.location.isNullOrEmpty()
            if(binding.entryLocation.isVisible) {
                binding.entryLocation.text = "Ubicación: ${entry.location}"
            }
        }
    }

    class EntriesComparator : DiffUtil.ItemCallback<MoodEntry>() {
        override fun areItemsTheSame(oldItem: MoodEntry, newItem: MoodEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoodEntry, newItem: MoodEntry): Boolean {
            return oldItem == newItem
        }
    }
}
