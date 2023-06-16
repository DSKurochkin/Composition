package com.example.composition.domain.entity

data class GameResult(
    val isWinner:Boolean,
    val countOfRightAnswer: Int,
    val countQuestion: Int,
    val gameSettings: GameSettings
)
