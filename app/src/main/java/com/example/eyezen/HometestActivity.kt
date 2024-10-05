package com.example.eyezen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class HometestActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE = 100
    private val TAG = "HomestestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hometest)

        Log.d(TAG, "onCreate: Starting the HomestestActivity")

        // Find the download button
        val downloadButton = findViewById<Button>(R.id.downloadButton)  //

        // Set up a click listener for the button
        downloadButton.setOnClickListener {
            // Check for storage permission before downloading
            if (isStoragePermissionGranted()) {
                // Permission already granted, proceed to download
                Log.d(TAG, "onCreate: Permission already granted")
                downloadPDF()
            } else {
                // Request storage permission
                Log.d(TAG, "onCreate: Requesting storage permission")
                requestStoragePermission()
            }

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.doctor_one)

            // Set the current selected item as active
            bottomNavigationView.selectedItemId = R.id.doctorsection  // Assuming Home is the default screen

            // Set a listener for navigation item selection
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (this::class != DashboardActivity::class) {
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        true
                    }
                    R.id.navigation_reports -> {
                        if (this::class != TestResultsActivity::class) {
                            val intent = Intent(this, TestResultsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        true
                    }
                    R.id.navigation_notifications -> {
                        if (this::class != NotificationActivity::class) {
                            //val intent = Intent(this, NotificationActivity::class.java)
                            //startActivity(intent)
                            //finish()
                        }
                        true
                    }
                    R.id.navigation_profile ->  {if (this::class != ProfileActivity::class) {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                        true
                    }
                    else -> false
                }
            }
        }
    }

    // Function to check if the storage permission is granted
    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val writePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            Log.d(TAG, "isStoragePermissionGranted: Read permission = $readPermission")
            Log.d(TAG, "isStoragePermissionGranted: Write permission = $writePermission")

            if (readPermission == PackageManager.PERMISSION_GRANTED &&
                writePermission == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "isStoragePermissionGranted: Both permissions are granted")
                true
            } else {
                Log.d(TAG, "isStoragePermissionGranted: Permissions are not granted")
                false
            }
        } else {
            // For versions lower than Marshmallow, permissions are granted at install time
            Log.d(TAG, "isStoragePermissionGranted: Permissions granted by default for SDK < 23")
            true
        }
    }

    // Function to request storage permission
    private fun requestStoragePermission() {
        Log.d(TAG, "requestStoragePermission: Requesting permissions")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            STORAGE_PERMISSION_CODE
        )
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            Log.d(TAG, "onRequestPermissionsResult: Handling the result of permission request")
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, continue with your operation
                Log.d(TAG, "onRequestPermissionsResult: Permission granted, proceeding with download")
                downloadPDF()
            } else {
                // Permission denied, show a message
                Log.d(TAG, "onRequestPermissionsResult: Permission denied")
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to download the PDF from Firebase Storage
    private fun downloadPDF() {
        Log.d(TAG, "downloadPDF: Downloading the PDF file...")

        // Firebase Storage reference (replace with your actual file URL)
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://eyezen-94888.appspot.com/Safe+Eyes+America+Eye+Chart.pdf")

        // Create a temporary file to store the downloaded PDF
        val localFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "HomeEyeTest.pdf")

        storageReference.getFile(localFile).addOnSuccessListener {
            Log.d(TAG, "downloadPDF: File downloaded successfully at ${localFile.absolutePath}")
            Toast.makeText(this, "PDF downloaded successfully", Toast.LENGTH_SHORT).show()

            // Optionally open the PDF file
            openPDF(localFile)

        }.addOnFailureListener {
            Log.e(TAG, "downloadPDF: Failed to download file: ${it.message}")
            Toast.makeText(this, "Error downloading PDF: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openPDF(file: File) {
        val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intent)
    }

}
