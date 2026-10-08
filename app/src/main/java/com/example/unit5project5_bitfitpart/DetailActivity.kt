package com.example.unit5project5_bitfitpart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var etFoodName: EditText
    private lateinit var etCalories: EditText
    private lateinit var etNotes: EditText
    private lateinit var ivPhotoPreview: ImageView
    private lateinit var btnSelectPhoto: Button
    private lateinit var btnSave: Button

    private var selectedPhotoUri: Uri? = null

    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedPhotoUri = uri
                // Request persistable permission if possible
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) { }

                ivPhotoPreview.visibility = View.VISIBLE
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(ivPhotoPreview)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etFoodName = findViewById(R.id.etFoodName)
        etCalories = findViewById(R.id.etCalories)
        etNotes = findViewById(R.id.etNotes)
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview)
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
        btnSave = findViewById(R.id.btnSave)

        btnSelectPhoto.setOnClickListener {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        btnSave.setOnClickListener {
            val name = etFoodName.text.toString().trim()
            val caloriesStr = etCalories.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calories = caloriesStr.toIntOrNull()
            if (calories == null) {
                Toast.makeText(this, "Please enter valid calories", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val entity = BitFitEntity(
                name = name,
                calories = calories,
                notes = if (notes.isNotEmpty()) notes else null,
                photoUri = selectedPhotoUri?.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                (application as BitFitApplication).db.bitFitDao().insert(entity)
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, "Entry saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}