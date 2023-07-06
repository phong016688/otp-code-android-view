package com.otp.code

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.otp.code.databinding.WhiteActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class WhiteActivity : AppCompatActivity() {
    private lateinit var binding: WhiteActivityBinding
    private var job: Job? = null
    private var currentCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WhiteActivityBinding.inflate(layoutInflater)
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
        binding.btnChangeBg.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                currentCode = Random.nextInt(9999999..99999999).toString()
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