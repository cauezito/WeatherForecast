package br.com.cauezito.simpleweatherforecast.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.api.DailyForecast
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil
import coil.load
import java.text.SimpleDateFormat
import java.util.*

private val DATE_FORMAT = SimpleDateFormat("dd/MM/yyy")

class DailyForecastViewHolder(view : View, private val sharedPreferencesUtil: SharedPreferencesUtil) : RecyclerView.ViewHolder(view) {
    private val tvTemp : TextView = view.findViewById<TextView>(R.id.tvTemp)
    private val tvDescription : TextView = view.findViewById<TextView>(R.id.tvDescription)
    private val dateText : TextView = view.findViewById<TextView>(R.id.tvDate)
    private val ivIcon : ImageView = view.findViewById<ImageView>(R.id.ivIcon)


    fun bind(dailyForecast: DailyForecast){
        val weather = dailyForecast.weather[0]

        tvTemp.text = ForecastUtil.formatForecastForShow(dailyForecast.temp.max, sharedPreferencesUtil.getTemperatureDisplay())
        tvDescription.text = weather.description
        dateText.text = DATE_FORMAT.format(Date(dailyForecast.date * 1000))

        ivIcon.load("http://openweathermap.org/img/wn/${weather.icon}@2x.png")
    }
}

class DailyForestAdapter(private val sharedPreferencesUtil: SharedPreferencesUtil ,
                         private val clickHandler : (DailyForecast) -> Unit) : ListAdapter<DailyForecast, DailyForecastViewHolder>(
    DIFF_CONFIG
) {

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