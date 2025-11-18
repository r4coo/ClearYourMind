package com.clearyourmind.diario.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.clearyourmind.diario.R
import com.clearyourmind.diario.data.AppDatabase
import com.clearyourmind.diario.data.MoodEntry
import com.clearyourmind.diario.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "ClearYourMindApp"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MoodViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var entryAdapter: MoodEntryAdapter

    private var currentPhotoPath: String? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions[Manifest.permission.CAMERA] == true -> dispatchTakePictureIntent()
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> requestLocation()
            else -> Toast.makeText(this, "Permisos necesarios denegados.", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.setImagePath(currentPhotoPath)
        } else {
            currentPhotoPath = null
            viewModel.setImagePath(null)
            Toast.makeText(this, "Captura de foto cancelada.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val database = AppDatabase.getDatabase(application)
        val viewModelFactory = MoodViewModelFactory(database.moodEntryDao())
        viewModel = ViewModelProvider(this, viewModelFactory)[MoodViewModel::class.java]

        setupUI()
        setupListeners()
        setupObservers()
    }

    private fun setupUI() {
        entryAdapter = MoodEntryAdapter()
        binding.entriesRecyclerView.adapter = entryAdapter
        binding.entriesRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.moodDisplay.text = getString(R.string.mood_display_format, binding.moodSeekBar.progress)
    }

    private fun setupListeners() {
        binding.moodSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.moodDisplay.text = getString(R.string.mood_display_format, progress)
                binding.moodDisplay.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.scale_up_down))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.saveButton.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce))
            saveDailyEntry()
        }

        binding.cameraButton.setOnClickListener { checkCameraPermissions() }
        binding.locationButton.setOnClickListener { checkLocationPermissions() }
    }

    private fun setupObservers() {
        viewModel.uiMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            Log.i(TAG, "Mensaje de UI: $message")
        }

        viewModel.allEntries.observe(this) { entries: List<MoodEntry> ->
            entryAdapter.submitList(entries)
        }

        viewModel.currentImagePath.observe(this) { path ->
            binding.cameraButton.text = if (path.isNullOrBlank()) "Añadir Foto" else "Foto Añadida"
        }

        viewModel.currentLocation.observe(this) { location ->
            binding.locationButton.text = if (location.isNullOrBlank()) "Guardar Ubicación" else "Ubicación OK"
        }
    }

    private fun checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent()
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation()
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.e(TAG, "Error al crear el archivo de imagen: ${ex.message}")
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.setLocation(location.latitude, location.longitude)
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación actual.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al obtener la ubicación.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDailyEntry() {
        val mood = binding.moodSeekBar.progress
        val reflection = binding.reflectionInput.text.toString()
        viewModel.saveEntry(mood, reflection)

        binding.reflectionInput.text.clear()

        viewModel.clearTemporaryData()
    }
}