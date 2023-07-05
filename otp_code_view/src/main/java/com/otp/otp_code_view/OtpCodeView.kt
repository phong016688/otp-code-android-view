package com.otp.otp_code_view


import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.text.InputFilter
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.otp.otp_code_view.databinding.VerifyOtpCodeViewBinding

class OtpCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: VerifyOtpCodeViewBinding
    private var onVerifyDone: ((String) -> Unit)? = null
    private var codeColor: Int
    private var codeLength: Int
    private var codeBGColor: Int
    private var codeStrokeColor: Int
    private var codeStrokeErrorColor: Int
    private var codeStrokeWidth: Float
    private var codeErrorColor: Int
    private var codeDoneStrokeColor: Int
    private var codeRadius: Float

    init {
        binding = VerifyOtpCodeViewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.OtpCodeView,
            defStyleAttr,
            defStyleAttr
        )
        codeLength = attributes.getInteger(
            R.styleable.OtpCodeView_code_length,
            6
        )
        codeStrokeWidth = attributes.getDimension(
            R.styleable.OtpCodeView_code_stroke_width,
            1 * ratioDpToPixels
        )
        codeRadius = attributes.getDimension(
            R.styleable.OtpCodeView_code_radius,
            12 * ratioDpToPixels
        )
        codeColor = attributes.getColor(
            R.styleable.OtpCodeView_code_color,
            ContextCompat.getColor(context, R.color.color_f2f2f2)
        )
        codeBGColor = attributes.getColor(
            R.styleable.OtpCodeView_code_background_color,
            ContextCompat.getColor(context, R.color.color_0af1f6f9)
        )
        codeStrokeColor = attributes.getColor(
            R.styleable.OtpCodeView_code_stroke_color,
            ContextCompat.getColor(context, R.color.color_b3fec70a)
        )
        codeStrokeErrorColor = attributes.getColor(
            R.styleable.OtpCodeView_code_stroke_error_color,
            ContextCompat.getColor(context, R.color.color_ef4d42)
        )
        codeErrorColor = attributes.getColor(
            R.styleable.OtpCodeView_code_error_color,
            ContextCompat.getColor(context, R.color.color_ef4d42)
        )
        codeDoneStrokeColor = attributes.getColor(
            R.styleable.OtpCodeView_code_done_stroke_color,
            ContextCompat.getColor(context, R.color.color_fec70a)
        )
        attributes.recycle()
        handleInputCode()
    }

    private fun handleInputCode() {
        val allViews = getAllViews()
        val listTextView = allViews.drop(1).map { it as TextView }
        val mainEditText = allViews[0] as EditText
        mainEditText.filters = arrayOf(InputFilter.LengthFilter(listTextView.size))
        mainEditText.setOnClickListener { mainEditText.setSelection(mainEditText.text.length) }
        mainEditText.doOnTextChanged { text, _, _, _ -> handleShowCode(text, listTextView) }
        listTextView.forEach {
            it.setTextColor(codeColor)
            it.background = getDrawable(codeStrokeColor, false)
        }
    }

    private fun handleShowCode(text: CharSequence?, listTextView: List<TextView>) {
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
            listTextView[it].background = getDrawable(codeDoneStrokeColor, true)
            listTextView[it].setTextColor(codeColor)
            listTextView[it].text = get(it).toString()
        }
    }

    private fun String.handleBackgroundNotHasCode(listTextView: List<TextView>) {
        (length until listTextView.size).forEach {
            listTextView[it].background = getDrawable(codeStrokeColor, false)
            listTextView[it].setTextColor(codeColor)
            listTextView[it].text = ""
        }
    }

    private fun String.handleBackgroundHasCode(listTextView: List<TextView>) {
        (indices).forEach {
            listTextView[it].setTextColor(codeColor)
            listTextView[it].background = getDrawable(codeStrokeColor, true)
            listTextView[it].text = get(it).toString()
        }
    }

    private fun getDrawable(strokeColor: Int, hasStroke: Boolean): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setColor(codeBGColor)
        drawable.cornerRadius = codeRadius
        if (hasStroke) drawable.setStroke(codeStrokeWidth.toInt(), strokeColor)
        return drawable
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
            tvs[it].background = getDrawable(codeStrokeErrorColor, true)
            tvs[it].setTextColor(codeErrorColor)
        }
    }

    companion object {
        @JvmField
        val ratioDpToPixels =
            Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT

        @JvmField
        val ratioPixelsToDp: Float = (1.0 / ratioDpToPixels.toDouble()).toFloat()
    }
}