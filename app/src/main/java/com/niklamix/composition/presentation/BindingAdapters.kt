package com.niklamix.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.niklamix.composition.R
import com.niklamix.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_required_answers),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_score_answers_text),
        count
    )
}

@BindingAdapter("percentAnswers")
fun bindPercentAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_percent_answers_text),
        count
    )
}

@BindingAdapter("percentRightAnswers")
fun bindPercentRightAnswers(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_percent_right_answers_text),
        getPercentOfRightAnswers(gameResult)
    )
}

@BindingAdapter("icSmile")
fun bindSmile(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getSmileResId(winner))
}

private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestion == 0) {
        0
    } else {
        ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()
    }
}

private fun getSmileResId(winner: Boolean): Int {
    return if (winner) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_smile_sad
    }
}