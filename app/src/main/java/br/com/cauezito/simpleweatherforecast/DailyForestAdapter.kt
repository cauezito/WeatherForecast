package br.com.cauezito.simpleweatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

class DailyForecastViewHolder(view : View, private val sharedPreferencesUtil: SharedPreferencesUtil) : RecyclerView.ViewHolder(view) {
    private val tvTemp : TextView = view.findViewById<TextView>(R.id.tvTemp)
    private val tvDescription : TextView = view.findViewById<TextView>(R.id.tvDescription)

    fun bind(dailyForecast: DailyForecast){
        tvTemp.text = ForecastUtil.formatForecastForShow(dailyForecast.temp, sharedPreferencesUtil.getTemperatureDisplay())
        tvDescription.text = dailyForecast.description
    }
}

class DailyForestAdapter(private val sharedPreferencesUtil: SharedPreferencesUtil ,
                         private val clickHandler : (DailyForecast) -> Unit) : ListAdapter<DailyForecast, DailyForecastViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                //referential equality is used when it's pointing to the SAME object
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                //structural equality is used if both values are the same (or equal)
               return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(itemView, sharedPreferencesUtil)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val dailyForecast = getItem(position)

        holder.bind(dailyForecast)

        holder.itemView.setOnClickListener {
            clickHandler(dailyForecast)
        }
    }
}