package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level
import com.example.composition.presentation.util.ViewUtils

class GameFragment : Fragment() {
    private lateinit var level: Level
    private lateinit var viewModel: GameFragmentViewModel
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding==null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameFragmentViewModel::class.java]
        viewModel.startGame(level)
        observeViewModel()
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArguments() {
        try {
            level = ViewUtils.getParcelable<Level>(requireArguments(), KEY_LEVEL)
                ?: throw RuntimeException("Arguments for key $KEY_LEVEL is null")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setClickListeners() {
        binding.tvOption1.setOnClickListener {
            viewModel.handleUserChoose(binding.tvOption1.text.toString().toInt())
        }
        binding.tvOption2.setOnClickListener {
            viewModel.handleUserChoose(binding.tvOption2.text.toString().toInt())
        }
        binding.tvOption3.setOnClickListener {
            viewModel.handleUserChoose(binding.tvOption3.text.toString().toInt())
        }
        binding.tvOption4.setOnClickListener {
            viewModel.handleUserChoose(binding.tvOption4.text.toString().toInt())
        }
        binding.tvOption5.setOnClickListener {
            viewModel.handleUserChoose(binding.tvOption5.text.toString().toInt())
        }
        binding.tvOption6.setOnClickListener {
            viewModel.handleUserChoose(binding.tvOption6.text.toString().toInt())
        }
    }

    private fun observeViewModel() {
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            binding.tvOption1.text = it.options[0].toString()
            binding.tvOption2.text = it.options[1].toString()
            binding.tvOption3.text = it.options[2].toString()
            binding.tvOption4.text = it.options[3].toString()
            binding.tvOption5.text = it.options[4].toString()
            binding.tvOption6.text = it.options[5].toString()
        }

        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }

        viewModel.progressInPercent.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, false)
        }

        viewModel.rightAnswerIndications.observe(viewLifecycleOwner) {
            binding.tvAnswerProgress.text = it
        }

        viewModel.enoughCount.observe(viewLifecycleOwner) {
            binding.tvAnswerProgress.setTextColor(getColorByState(it))
        }
        viewModel.enoughPercent.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }

        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    companion object {

        const val KEY_LEVEL = "level"
        const val RETRY_GAME = "GameFragment"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_LEVEL, level)
                }
            }
        }
    }
}