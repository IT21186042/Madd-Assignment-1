import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.eyezen.ArticleData
import com.example.eyezen.ArticleDetailActivity
import com.example.eyezen.R

class ArticleAdapter(private var articleList: List<ArticleData>, private val context: Context) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.articleImage)
        val articleTitle: TextView = itemView.findViewById(R.id.articleTitle)
        val articleDate: TextView = itemView.findViewById(R.id.articleDate)
        val articleTime: TextView = itemView.findViewById(R.id.articleTime)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]
        Log.d("ArticleAdapter", "Article Title: ${article.title}, Image URL: ${article.imageUrl}")

        holder.articleTitle.text = article.title
        holder.articleDate.text = article.date
        holder.articleTime.text = article.readtime

        // Load image using Coil
        holder.articleImage.load(article.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder) // Add a placeholder if needed
        }

        holder.itemView.setOnClickListener {

            val intent = Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("imageUrl", article.imageUrl)
                putExtra("description", article.description)
            }
            context.startActivity(intent)
        }
    }



    // Method to update the list of articles
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ArticleData>) {
        articleList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = articleList.size
}
