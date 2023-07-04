package com.otp.otp_code_view


import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.otp.otp_code_view.databinding.VerifyOtpCodeViewBinding

class VerifyCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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
        val views =
            (0 until binding.ctnInputCode.childCount).map { binding.ctnInputCode.getChildAt(it) }
        val et = views[0] as EditText
        val tvs = views.drop(1).map { it as TextView }
        et.filters = arrayOf(InputFilter.LengthFilter(tvs.size))
        et.setOnClickListener { et.placeCursorToEnd() }
        et.doOnTextChanged { text, _, _, _ ->
            onTextChanged?.invoke(text.toString())
            binding.tvCodeInvalid.gone()
            text.toString().apply {
                binding.tvWrong.invisible()
                (indices).forEach {
                    tvs[it].setTextColor(ContextCompat.getColor(context, R.color.color_8b9199))
                    tvs[it].text = get(it).toString()
                    tvs[it].setBackgroundResource(R.drawable.bg_radius_12_stroke)
//                    if (it == length - 1) {
//                        tvs[it].setBackgroundResource(R.drawable.bg_radius_12_stroke)
//                    } else {
//                        tvs[it].setBackgroundResource(R.drawable.bg_radius_12_0af1f6f9)
//                    }
                }
                (length until tvs.size).forEach {
                    tvs[it].setBackgroundResource(R.drawable.bg_radius_12_0af1f6f9)
                    tvs[it].setTextColor(ContextCompat.getColor(context, R.color.color_8b9199))
                    tvs[it].text = ""
                }
                if (length == tvs.size) {
                    (tvs.indices).forEach {
                        tvs[it].setBackgroundResource(R.drawable.bg_radius_12_stroke)
                        tvs[it].setTextColor(ContextCompat.getColor(context, R.color.color_8b9199))
                        tvs[it].text = get(it).toString()
                    }
                    onVerifyDone?.invoke(this)
                }
            }
        }
    }

    fun verifyFailure() {
        showError()
    }

    fun getCode() = binding.edtCode.text?.toString() ?: ""

    fun clearCode() {
        binding.edtCode.setText("")
    }

    fun verifyFailure(errorText: String) {
        binding.tvWrong.text = errorText
        showError()
        if (errorText == context.getString(R.string.invalid_code)) {
            binding.tvWrong.gone()
            binding.tvCodeInvalid.visible()
        } else {
            binding.tvCodeInvalid.gone()
        }
    }

    private fun showError() {
        binding.tvWrong.visible()
        val views =
            (0 until binding.ctnInputCode.childCount).map { binding.ctnInputCode.getChildAt(it) }
        val tvs = views.drop(1).map { it as TextView }
        (tvs.indices).forEach {
            tvs[it].setBackgroundResource(R.drawable.bg_radius_12_stroke_red)
            tvs[it].setTextColor(ContextCompat.getColor(context, R.color.color_ef4d42))
        }
    }
}