import android.content.Intent
import android.util.Log // Import Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.eyezen.TreatmentData
import com.example.eyezen.TreatmentDetailActivity
import com.example.eyezen.R

class TreatmentAdapter(private val treatmentList: List<TreatmentData>) :
    RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder>() {

    inner class TreatmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val treatmentImageView: ImageView = itemView.findViewById(R.id.treatmentImageView)
        val treatmentTitleTextView: TextView = itemView.findViewById(R.id.treatmentTitleTextView)
        val treatmentDescriptionTextView: TextView = itemView.findViewById(R.id.treatmentDescriptionTextView)
        val readMoreTextView: TextView = itemView.findViewById(R.id.readMoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayurwedic_article, parent, false)
        return TreatmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val currentTreatment = treatmentList[position]

        // Logging the details of the current treatment
        Log.d("TreatmentAdapter", "Title: ${currentTreatment.title}")
        Log.d("TreatmentAdapter", "Description: ${currentTreatment.description}")
        Log.d("TreatmentAdapter", "Image URL: ${currentTreatment.imageUrl}")

        holder.treatmentTitleTextView.text = currentTreatment.title
        holder.treatmentDescriptionTextView.text = currentTreatment.description

        // Load image using Coil
        holder.treatmentImageView.load(currentTreatment.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }

        // Set a click listener to navigate to TreatmentDetailActivity (for Read More)
        holder.readMoreTextView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TreatmentDetailActivity::class.java).apply {
                putExtra("treatment_title", currentTreatment.title)
                putExtra("treatment_description", currentTreatment.description)
                putExtra("treatment_image_url", currentTreatment.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = treatmentList.size
}
