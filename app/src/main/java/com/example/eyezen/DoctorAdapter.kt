import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.eyezen.DoctorData
import com.example.eyezen.OneDoctorActivity
import com.example.eyezen.R

class DoctorAdapter(private val doctorList: List<DoctorData>) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorImageView: ImageView = itemView.findViewById(R.id.doctorImageView)
        val doctorName: TextView = itemView.findViewById(R.id.doctorName)
        val doctorSpecialization: TextView = itemView.findViewById(R.id.doctorSpecialty)
        val doctorRating: TextView = itemView.findViewById(R.id.doctorRating)
        val doctorDistance: TextView = itemView.findViewById(R.id.doctorDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentDoctor = doctorList[position]
        holder.doctorName.text = currentDoctor.name
        holder.doctorSpecialization.text = currentDoctor.specialty
        holder.doctorRating.text = currentDoctor.rating
        holder.doctorDistance.text = currentDoctor.distance

        // Load image using Coil
        holder.doctorImageView.load(currentDoctor.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder) // Add a placeholder if needed
        }

        // Set a click listener to navigate to DoctorDetailActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, OneDoctorActivity::class.java).apply {
                putExtra("doctor_name", currentDoctor.name)
                putExtra("doctor_specialty", currentDoctor.specialty)
                putExtra("doctor_rating", currentDoctor.rating)
                putExtra("doctor_distance", currentDoctor.distance)
                putExtra("doctor_image_url", currentDoctor.imageUrl)
                putExtra("doctor_bio",currentDoctor.bio)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = doctorList.size
}
