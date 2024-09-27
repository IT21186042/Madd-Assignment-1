import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eyezen.IconData
import com.example.eyezen.R

class IconAdapter(
    private val iconList: List<IconData>,
    private val onItemClick: (IconData) -> Unit  // Lambda function to handle click events
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    // ViewHolder class to hold item views
    class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImage)
        val iconLabel: TextView = itemView.findViewById(R.id.iconLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        // Inflate the custom layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon_button, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        // Get the current icon data
        val currentIcon = iconList[position]

        // Set the icon image and label text
        holder.iconImageView.setImageResource(currentIcon.iconResId)
        holder.iconLabel.text = currentIcon.label

        holder.itemView.setOnClickListener {
            onItemClick(currentIcon)  // Pass the clicked icon data to the listener
        }
    }

    override fun getItemCount(): Int {
        return iconList.size
    }
}
