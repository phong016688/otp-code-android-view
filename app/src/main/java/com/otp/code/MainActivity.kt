package com.otp.code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.otp.code.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    private var currentCode: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        binding.verifyCode.setOnVerifyListener(onVerify = {
            if (it == currentCode) {
                Toast.makeText(this, "Verify Success", Toast.LENGTH_SHORT).show()
            } else {
                binding.verifyCode.showError("Invalid Code")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                currentCode = Random.nextInt(99999..999999).toString()
                binding.tvCode.text = "Code: $currentCode"
                delay(60_000)
            }
        }
    }

    override fun onStop() {
        job?.cancel()
        job = null
        super.onStop()
    }
}