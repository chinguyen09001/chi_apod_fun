package com.example.chiapodfun.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chiapodfun.model.ApodResponse
import com.example.chiapodfun.repo.ApodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

class ApodViewModel @Inject constructor(
    private val repository: ApodRepository
) : ViewModel() {
    private val todayDate = LocalDate.now().toEpochDay()
    private val minDate: LocalDate = LocalDate.of(1995, 6, 15)

    private val _apod = mutableStateOf<ApodResponse?>(null)
    val apod: State<ApodResponse?> = _apod

    private val _pictureDate = mutableStateOf<String?>(null)
    val pictureDate: State<String?> = _pictureDate

    private val _apodViewModelUiState = MutableStateFlow(
        ApodViewModelUiState(
            pictureDate = pictureDate,
            apod = apod,
            getPicOnDate = ::getPicOnDate,
            getRandomPic = ::getRandomPic
        )
    )
    val apodViewModelUiState: StateFlow<ApodViewModelUiState> = _apodViewModelUiState

    init {
        viewModelScope.launch {
            _apod.value = repository.getApod(LocalDate.ofEpochDay(todayDate).toString())
            _pictureDate.value = LocalDate.ofEpochDay(todayDate).toString()
        }
    }

    fun getRandomPic() {
        val newDate = getRandomDateBetweenTodayAnd(minDate)

        viewModelScope.launch {
            _pictureDate.value = newDate.toString()
            _apod.value = repository.getApod(newDate.toString())
        }
    }

    fun getPicOnDate(inputDate: String?) {
        if (inputDate == null) return
        viewModelScope.launch {
            try {
                val date = LocalDate.parse(inputDate)
                _pictureDate.value = date.toString()
                _apod.value = repository.getApod(date.toString())
            } catch (e: DateTimeException) {
                //log here
            }
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
    val getRandomPic: () -> Unit,
    val getPicOnDate: (String?) -> Unit
)