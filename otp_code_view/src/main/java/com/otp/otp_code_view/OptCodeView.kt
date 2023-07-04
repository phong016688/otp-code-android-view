package com.otp.otp_code_view


import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.otp.otp_code_view.databinding.VerifyOtpCodeViewBinding

class VerifyCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: VerifyOtpCodeViewBinding
    private var onVerifyDone: ((String) -> Unit)? = null
    private var onTextChanged: ((String) -> Unit)? = null


    init {
        binding = VerifyOtpCodeViewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        handleInputCode()
    }

    private fun handleInputCode() {
        val allViews = getAllViews()
        val listTextView = allViews.drop(1).map { it as TextView }
        val mainEditText = allViews[0] as EditText
        mainEditText.filters = arrayOf(InputFilter.LengthFilter(listTextView.size))
        mainEditText.setOnClickListener { mainEditText.setSelection(mainEditText.text.length) }
        mainEditText.doOnTextChanged { text, _, _, _ -> handleShowCode(text, listTextView) }
    }

    private fun handleShowCode(text: CharSequence?, listTextView: List<TextView>) {
        onTextChanged?.invoke(text.toString())
        binding.tvCodeInvalid.visibility = GONE
        text.toString().apply {
            handleBackgroundHasCode(listTextView)
            handleBackgroundNotHasCode(listTextView)
            if (length == listTextView.size) {
                handleBackgroundDoneEnter(listTextView)
                onVerifyDone?.invoke(this)
                binding.root.hideKeyBoard()
            }
        }
    }

    private fun String.handleBackgroundDoneEnter(listTextView: List<TextView>) {
        (listTextView.indices).forEach {
            listTextView[it].setBackgroundResource(R.drawable.bg_radius_12_yellow2)
            listTextView[it].setTextColor(ContextCompat.getColor(context, R.color.color_f2f2f2))
            listTextView[it].text = get(it).toString()
        }
    }

    private fun String.handleBackgroundNotHasCode(listTextView: List<TextView>) {
        (length until listTextView.size).forEach {
            listTextView[it].setBackgroundResource(R.drawable.bg_radius_12)
            listTextView[it].setTextColor(ContextCompat.getColor(context, R.color.color_f2f2f2))
            listTextView[it].text = ""
        }
    }

    private fun String.handleBackgroundHasCode(listTextView: List<TextView>) {
        (indices).forEach {
            listTextView[it].setTextColor(ContextCompat.getColor(context, R.color.color_f2f2f2))
            listTextView[it].text = get(it).toString()
            listTextView[it].setBackgroundResource(R.drawable.bg_radius_12_yellow)
        }
    }

    private fun getAllViews() =
        (0 until binding.ctnInputCode.childCount).map { binding.ctnInputCode.getChildAt(it) }

    private fun View.hideKeyBoard() {
        runCatching {
            val inputMethodManager = context.getSystemService(InputMethodManager::class.java)
            inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun setOnVerifyListener(onVerify: (String) -> Unit) {
        onVerifyDone = onVerify
    }

    fun getCode() = binding.edtCode.text?.toString() ?: ""

    fun clearCode() {
        binding.edtCode.setText("")
    }

    fun showError(error: String) {
        binding.tvCodeInvalid.visibility = View.VISIBLE
        binding.tvCodeInvalid.text = error
        val views = getAllViews()
        val tvs = views.drop(1).map { it as TextView }
        (tvs.indices).forEach {
            tvs[it].setBackgroundResource(R.drawable.bg_radius_12_stroke_red)
            tvs[it].setTextColor(ContextCompat.getColor(context, R.color.color_ef4d42))
        }
    }
}