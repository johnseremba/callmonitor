package com.johnseremba.call.monitor.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.johnseremba.call.monitor.R
import com.johnseremba.call.monitor.databinding.ItemCallLogBinding
import com.johnseremba.call.monitor.server.data.CallEntry


private val diffCallback = object : DiffUtil.ItemCallback<CallEntry>() {
    override fun areItemsTheSame(
        oldItem: CallEntry,
        newItem: CallEntry
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CallEntry,
        newItem: CallEntry
    ): Boolean = oldItem == newItem

}

class CallLogsAdapter : ListAdapter<CallEntry, RecyclerView.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CallLogsViewHolder(ItemCallLogBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = currentList[position]
        (holder as CallLogsViewHolder).bind(entry)
    }

    private inner class CallLogsViewHolder(
        private val binding: ItemCallLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: CallEntry) {
            with(binding) {
                tvCallName.text = getCallerName(entry)
                tvCallDuration.text = getDurationString(root.context, entry.duration)
            }
        }

        private fun getCallerName(entry: CallEntry): String {
            val name = entry.name
            return if (name == null || name.isEmpty())
                "(${entry.number})"
            else
                name
        }

        private fun getDurationString(context: Context, duration: String?): String {
            // Could be improved to dynamically calculate minutes, hours from seconds
            return String.format(context.getString(R.string.call_duration), duration.orEmpty(), "s")
        }
    }
}
