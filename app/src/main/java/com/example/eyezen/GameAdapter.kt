import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.eyezen.GameData
import com.example.eyezen.R

class GameAdapter(
    private val context: Context,
    private val gameList: List<GameData>,
    private val onGameClick: (String) -> Unit // Click listener function
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameTitleTextView: TextView = itemView.findViewById(R.id.gameTitleTextView)
        val gameScoreTextView: TextView = itemView.findViewById(R.id.gameScoreTextView)
        val gameImageView: ImageView = itemView.findViewById(R.id.gameImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.gameTitleTextView.text = game.gameTitle
        holder.gameScoreTextView.text = "Highest Score: ${game.score}"

        // Dynamically get the drawable resource ID using the game title
        val imageRes = when (game.gameTitle) {
            "Color Cubes" -> R.drawable.colorecube
            "Color Match" -> R.drawable.colorematch
            "Find Number" -> R.drawable.coloreblind
            else -> R.drawable.ic_placeholder // Fallback/default image
        }

        // Use Coil or any image loading library to load the image from drawable
        holder.gameImageView.load(imageRes) {
            crossfade(true) // Optional: adds a fade-in effect
            placeholder(R.drawable.ic_placeholder) // Optional: placeholder while loading
        }

        holder.itemView.setOnClickListener {
            onGameClick(game.gameTitle)
        }
    }


    override fun getItemCount(): Int {
        return gameList.size
    }
}
