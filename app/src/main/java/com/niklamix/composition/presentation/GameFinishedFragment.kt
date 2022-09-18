package com.niklamix.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.niklamix.composition.R
import com.niklamix.composition.databinding.FragmentGameFinishedBinding
import com.niklamix.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
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
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        bindingViews()
    }

    private fun setupClickListener() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.btnTryAgain.setOnClickListener {
            retryGame()
        }
    }

    private fun bindingViews() {
        with(binding) {
            tvRequiredAnswers.text = String.format(
                getString(R.string.tv_required_answers),
                gameResult.gameSettings.minCountOfRightAnswers,
            )
            tvScoreAnswers.text = String.format(
                getString(R.string.tv_score_answers_text),
                gameResult.countOfRightAnswers,
            )
            tvPercentAnswers.text = String.format(
                getString(R.string.tv_percent_answers_text),
                gameResult.gameSettings.minPercentOfRightAnswer,
            )
            tvPercentRightAnswers.text = String.format(
                getString(R.string.tv_percent_right_answers_text),
                getPercentOfRightAnswers(),
            )
            ivSmile.setImageResource(getSmileResId())
        }
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_smile
        }
    }

    private fun getPercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestion == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestion.toDouble())*100).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager
            .popBackStack(GameFragment.FRAGMENT_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    companion object {

        private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}