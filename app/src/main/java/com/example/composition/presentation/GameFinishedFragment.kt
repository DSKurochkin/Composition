package com.example.composition.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.presentation.util.ViewUtils
import kotlin.system.exitProcess

class GameFinishedFragment : Fragment() {
    private lateinit var gameResult: GameResult
    private lateinit var gameSettings: GameSettings
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding==null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parseArguments()
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
        ///del
        Toast.makeText(activity, "is winner - ${gameResult.isWinner}", Toast.LENGTH_SHORT).show()
        binding.buttonRetry.setOnClickListener {
            launchChooseFragment()
        }
        //
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArguments() {
        try {
            val res = requireArguments()
            gameResult = ViewUtils.getParcelable<GameResult>(requireArguments(), KEY_GAME_RESULT)
                ?: throw RuntimeException("Arguments for key ${KEY_GAME_RESULT} is null")
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(1)
        }
    }

    private fun launchChooseFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChooseLevelFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(GameFragment.RETRY_GAME, 1)

    }

    companion object {

        const val KEY_GAME_RESULT = "gameResult"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}