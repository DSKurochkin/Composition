package com.example.composition.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private val args by navArgs<GameFinishedFragmentArgs>()
    private lateinit var gameResult: GameResult
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding==null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameResult = args.gameResult
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        outputRes()
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    private fun outputRes() {

        binding.emojiResult.setImageResource(isWin())

        binding.tvRequiredAnswers.text = String
            .format(
                resources.getString(R.string.required_score),
                gameResult.gameSettings.minCountOfRightAnswer
            )

        binding.tvScoreAnswers.text = String
            .format(
                resources.getString(R.string.score_answers),
                gameResult.countOfRightAnswer
            )

        binding.tvRequiredPercentage.text = String
            .format(
                resources.getString(R.string.required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswer
            )


        binding.tvScorePercentage.text = String
            .format(
                resources.getString(R.string.score_percentage),
                ((gameResult.countOfRightAnswer / gameResult.countQuestion.toDouble()) * 100).toInt()
            )

    }

    private fun isWin(): Int {
        return if (gameResult.isWinner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

}