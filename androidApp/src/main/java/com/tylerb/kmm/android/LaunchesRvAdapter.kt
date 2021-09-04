package com.tylerb.kmm.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tylerb.kmm.shared.entity.RocketLaunch
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tylerb.kmm.android.databinding.ItemLaunchBinding

class LaunchesRvAdapter(var launches: List<RocketLaunch>) : RecyclerView.Adapter<LaunchesRvAdapter.LaunchViewHolder>() {

    inner class LaunchViewHolder(private val binding: ItemLaunchBinding) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bindData(launch: RocketLaunch) {

            val launchSuccessUtil: Pair<Int, Int> = when(launch.launchSuccess) {
                true -> Pair(R.string.successful, R.color.colorSuccessful)
                false -> Pair(R.string.unsuccessful, R.color.colorUnsuccessful)
                null -> Pair(R.string.no_data, R.color.colorNoData)
            }

            binding.missionName.text = context.getString(R.string.mission_name_field, launch.missionName)
            binding.launchYear.text = context.getString(R.string.launch_year_field, launch.launchYear.toString())
            binding.details.text = context.getString(R.string.details_field, launch.details ?: "")

            with(binding.launchSuccess) {
                text = context.getString(launchSuccessUtil.first)
                setTextColor(ContextCompat.getColor(context, launchSuccessUtil.second))
            }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LaunchViewHolder(ItemLaunchBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bindData(launches[position])
    }

    override fun getItemCount() = launches.count()


}