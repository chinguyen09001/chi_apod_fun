package com.example.chiapodfun.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chiapodfun.model.ApodResponse
import com.example.chiapodfun.repo.ApodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

class ApodViewModel @Inject constructor(private val repository: ApodRepository) : ViewModel() {
    val todayDate = LocalDate.now().toEpochDay()

    private val _apod = mutableStateOf<ApodResponse?>(null)
    val apod: State<ApodResponse?> = _apod

    private val _pictureDate = mutableStateOf<String?>(null)
    val pictureDate: State<String?> = _pictureDate

    private val _apodViewModelUiState = MutableStateFlow(
        ApodViewModelUiState(
            pictureDate = pictureDate,
            apod = apod,
            getNewPic = ::getNewPic
        )
    )
    val apodViewModelUiState: StateFlow<ApodViewModelUiState> = _apodViewModelUiState

    init {
        viewModelScope.launch {
            _apod.value = repository.getApod(LocalDate.ofEpochDay(todayDate).toString())
            _pictureDate.value = LocalDate.ofEpochDay(todayDate).toString()
        }
    }

    fun getNewPic() {
        val newDate = getRandomDateBetweenTodayAnd(LocalDate.of(1995, 6, 15))

        viewModelScope.launch {
            _pictureDate.value = newDate.toString()
            _apod.value = repository.getApod(newDate.toString())
        }
    }

    private fun getRandomDateBetweenTodayAnd(targetDate: LocalDate): LocalDate {
        val startDate = targetDate.toEpochDay()
        val endDate = todayDate
        val randomDay = Random.nextLong(startDate, endDate + 1)

        return LocalDate.ofEpochDay(randomDay)
    }

}

data class ApodViewModelUiState(
    val pictureDate: State<String?>,
    val apod: State<ApodResponse?>,
    val getNewPic: () -> Unit
)