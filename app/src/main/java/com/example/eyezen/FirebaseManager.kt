import android.util.Log
import com.example.eyezen.DoctorData
import com.example.eyezen.GameData
import com.example.eyezen.HealthArticle
import com.example.eyezen.QuizQuestion
import com.example.eyezen.TestResultData
import com.example.eyezen.TreatmentData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseManager {

    private val doctorRef: DatabaseReference = Firebase.database.getReference("Doctors")
    private val quizRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("quizQuestions")
    private val testResultsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("testResults")
    private val treatmentsRef = FirebaseDatabase.getInstance().getReference("treatments")
    private val scoresRef = FirebaseDatabase.getInstance().getReference("games")
    val healthArticlesRef = FirebaseDatabase.getInstance().getReference("healthArticle")





    suspend fun getDoctorData(doctorId: String): DoctorData? {
        return try {
            val dataSnapshot = doctorRef.child(doctorId).get().await()
            dataSnapshot.getValue(DoctorData::class.java)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun getAllDoctors(): List<DoctorData>? {
        return try {
            val snapshot = doctorRef.get().await()
            snapshot.children.mapNotNull { it.getValue(DoctorData::class.java) }
        } catch (e: Exception) {
            null // Return null if there's an error
        }
    }

    // Method to retrieve all quiz questions
    suspend fun getQuizQuestions(): List<QuizQuestion> {
        return try {
            val snapshot = quizRef.get().await()

            snapshot.children.mapNotNull { it.getValue(QuizQuestion::class.java) }
        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }



    // Method to fetch test results from Firebase
    suspend fun getTestResults(): List<TestResultData>? {
        return try {
            val snapshot = testResultsRef.get().await()
            snapshot.children.mapNotNull { it.getValue(TestResultData::class.java) }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getTreatments(): List<TreatmentData>? {
        return try {
            val snapshot = treatmentsRef.get().await()
            snapshot.children.mapNotNull { it.getValue(TreatmentData::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun getGames(): List<GameData>? {
        return try {
            val snapshot = FirebaseDatabase.getInstance().getReference("games").get().await()
            snapshot.children.mapNotNull { it.getValue(GameData::class.java) }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateScoreInDatabase(gameId: String, score: Int, callback: (Boolean) -> Unit) {
        val gameScore = mapOf("score" to score)
        scoresRef.child(gameId).updateChildren(gameScore).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    suspend fun getHealthArticles(): List<HealthArticle>? {
        return try {
            val snapshot = healthArticlesRef.get().await()
            snapshot.children.mapNotNull { it.getValue(HealthArticle::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



}
