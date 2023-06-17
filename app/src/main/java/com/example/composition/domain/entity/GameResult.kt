package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GameResult(
    val isWinner: Boolean,
    val countOfRightAnswer: Int,
    val countQuestion: Int,
    val gameSettings: GameSettings
) : Parcelable
