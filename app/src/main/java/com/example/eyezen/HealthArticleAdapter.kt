import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.eyezen.HealthArticle
import com.example.eyezen.HealthArticleDetailActivity
import com.example.eyezen.R

class HealthArticleAdapter(private val healthArticleList: List<HealthArticle>) :
    RecyclerView.Adapter<HealthArticleAdapter.HealthArticleViewHolder>() {

    inner class HealthArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val healthArticleImageView: ImageView = itemView.findViewById(R.id.healthtreatmentImageView)
        val healthArticleTitleTextView: TextView = itemView.findViewById(R.id.healthtreatmentTitleTextView)
        val healthArticleDescriptionTextView: TextView = itemView.findViewById(R.id.healthtreatmentDescriptionTextView)
        val readMoreTextView: TextView = itemView.findViewById(R.id.healthreadMoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.health_article_item, parent, false)
        return HealthArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: HealthArticleViewHolder, position: Int) {
        val currentArticle = healthArticleList[position]

        // Logging the details of the current health article
        Log.d("HealthArticleAdapter", "Title: ${currentArticle.title}")
        Log.d("HealthArticleAdapter", "Description: ${currentArticle.description}")
        Log.d("HealthArticleAdapter", "Image URL: ${currentArticle.imageUrl}")

        holder.healthArticleTitleTextView.text = currentArticle.title
        holder.healthArticleDescriptionTextView.text = currentArticle.description

        // Load image using Coil
        holder.healthArticleImageView.load(currentArticle.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }

        // Set a click listener to navigate to HealthArticleDetailActivity (for Read More)
        holder.readMoreTextView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, HealthArticleDetailActivity::class.java).apply {
                putExtra("title", currentArticle.title)
                putExtra("description", currentArticle.description)
                putExtra("imageUrl", currentArticle.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = healthArticleList.size
}
