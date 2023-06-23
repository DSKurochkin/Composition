package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

interface OnOptionClickListener {
    fun onOptionClick(numOfOption: Int)
}

@BindingAdapter("parseIntToString")
fun parseIntToStringForBinding(tv: TextView, value: Int) {
    tv.text = value.toString()
}


@BindingAdapter("requiredAnswers")
fun bindRequiredAnswer(tv: TextView, count: Int) {
    bindStringAndInt(tv, R.string.required_score, count)
}

@BindingAdapter("scoreAnswer")
fun bindScoreAnswer(tv: TextView, count: Int) {
    bindStringAndInt(tv, R.string.score_answers, count)
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(tv: TextView, count: Int) {
    bindStringAndInt(tv, R.string.required_percentage, count)
}

@BindingAdapter("answersProgress")
fun bindAnswersProgress(tv: TextView, curVal: Int, minVal: Int) {
    bindStringAndInt(tv, R.string.progress_answers, curVal, minVal)
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(tv: TextView, gameResult: GameResult) {
    tv.text = String
        .format(
            tv.resources.getString(com.example.composition.R.string.score_percentage),
            ((gameResult.countOfRightAnswer / gameResult.countQuestion.toDouble()) * 100).toInt()
        )
}


@BindingAdapter("emojiResult")
fun bindEmojiResult(iv: ImageView, isWin: Boolean) {
    if (isWin) iv.setImageResource(R.drawable.ic_smile)
    else iv.setImageResource(R.drawable.ic_sad)
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(view: TextView, enough: Boolean) {
    view.setTextColor(getColorByState(view.context, enough))
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(view: ProgressBar, enough: Boolean) {
    view.progressTintList = ColorStateList.valueOf(getColorByState(view.context, enough))
}

@BindingAdapter("optionClick")
fun bindOptionClick(view: TextView, listener: OnOptionClickListener) {
    view.setOnClickListener {
        listener.onOptionClick(view.text.toString().toInt())
    }
}


private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}


private fun bindStringAndInt(tv: TextView, stringConst: Int, vararg values: Int) {
    when (values.size) {
        1 -> tv.text = String.format(tv.context.resources.getString(stringConst), values[0])
        2 -> tv.text =
            String.format(tv.context.resources.getString(stringConst), values[0], values[1])
    }


}
