package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = GameRepositoryImpl
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repo)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repo)
    private lateinit var gameSettings: GameSettings
    private var countOfRigtAnswer = 0
    private var countQuestion = 0
    private val context = application


    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _progressInPercent = MutableLiveData<Int>()
    val progressInPercent: LiveData<Int>
        get() = _progressInPercent

    private val _rightAnswerIndications = MutableLiveData<String>()
    val rightAnswerIndications: MutableLiveData<String>
        get() = _rightAnswerIndications

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    fun startGame(level: Level) {
        gameSettings = getGameSettingsUseCase(level)
        launchTimer(gameSettings.gameTimeInSeconds.toLong() * 1000)
        oneIter()
    }

    private fun oneIter() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
        funUpdateProgress()
        countQuestion++
    }

    fun handleUserChoose(chooseOption: Int) {
        if (chooseOption == question.value?.answer) {
            countOfRigtAnswer++
        }
        oneIter()
    }

    private fun funUpdateProgress() {
        val percent = calculatePercentOfRightAnswer()
        _progressInPercent.value = percent
        _enoughCount.value = countOfRigtAnswer >= gameSettings.minCountOfRightAnswer
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswer
        _rightAnswerIndications.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRigtAnswer,
            gameSettings.minCountOfRightAnswer
        )

    }

    private fun finish() {
        val isCountOfRightAnswerEnough = countOfRigtAnswer >= gameSettings.minCountOfRightAnswer
        val isPercentOfRightAnswerEnough =
            calculatePercentOfRightAnswer() >= gameSettings.minPercentOfRightAnswer
        _gameResult.value = GameResult(
            isCountOfRightAnswerEnough && isPercentOfRightAnswerEnough,
            countOfRigtAnswer,
            countQuestion,
            gameSettings
        )
    }

    private fun launchTimer(time: Long) {
        object : CountDownTimer(time, MILLIS_IN_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finish()
            }

        }.start()
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / MILLIS_IN_SECOND
        val minutes = seconds / SEC_IN_MIN
        val leftSeconds = seconds - minutes * SEC_IN_MIN
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun calculatePercentOfRightAnswer(): Int {
        return (countOfRigtAnswer * 100 / countQuestion.toDouble()).toInt()
    }


    companion object {
        const val MILLIS_IN_SECOND = 1_000L
        const val SEC_IN_MIN = 60L
    }

}