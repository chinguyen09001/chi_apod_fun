package com.example.chiapodfun.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.example.chiapodfun.di.DaggerAppComponent
import com.example.chiapodfun.model.ApodResponse
import com.example.chiapodfun.repo.ApodRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class ApodFragment : Fragment() {

    @Inject
    lateinit var repository: ApodRepository

    lateinit var viewModel: ApodViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerAppComponent.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel = ApodViewModel(repository)

        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.apodViewModelUiState.collectAsState()
                ApodContent(uiState)
            }
        }
    }

    @Composable
    private fun ApodContent(uiState: ApodViewModelUiState) {
        val context = LocalContext.current
        val apod by remember { uiState.apod }
        val pictureDate by remember { uiState.pictureDate }
        var inputText by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                Text(
                    text = "Welcome to Astronomy Picture of the Day!",
                    modifier = Modifier
                        .background(Color(0xFF102B43))
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(15.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp
                )
            }
        ) { innerPadding ->
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF000042))
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FillerWhiteText("Date: $pictureDate")

                if(apod != null) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        model = apod!!.url,
                        contentDescription = apod!!.title
                    )
                } else {
                    FillerWhiteText("Something went wrong")
                }

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Enter date (YYYY-mm-dd) after 1995-06-15") },
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                )

                GetPicButton(
                    text = "Get the pic of selected date!",
                    onClick = {
                        val (isValid, errorText) = validateDateInput(inputText)
                        if(isValid) {
                            uiState.getPicOnDate(inputText)
                        } else {
                            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                FillerWhiteText("or")

                GetPicButton(
                    text = "Get a random pic!",
                    onClick = { uiState.getRandomPic() }
                )
            }
        }

    }

    @Composable
    private fun GetPicButton(
        text: String,
        onClick: () -> Unit
    ) {
        Button(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
            colors = ButtonColors(
                containerColor = Color(0xFFffd700),
                contentColor = Color(0xFF102B43),
                disabledContainerColor = Color(0xFFffd700),
                disabledContentColor = Color(0xFFffd700)
            ),
            onClick = onClick
        ) {
            Text(
                text = text,
                modifier = Modifier.wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }

    @Composable
    private fun FillerWhiteText(text: String) {
        Text(
            text = text,
            modifier = Modifier
                .wrapContentHeight()
                .padding(5.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }

    /**
     * Validate given date.
     *
     * Not good if format is wrong, or before 1995/06/15
     *
     * Otherwise good
     */
    private fun validateDateInput(input: String): Pair<Boolean, String?> {
        val minDate: LocalDate = LocalDate.of(1995, 6, 15)

        return try {
            val parsedDate = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE)   //ISO LOCAL DATE is YYYY-mm-dd
            if(parsedDate.isBefore(minDate)) {
                false to "Date must be after 1995-06-15"
            } else {
                true to null
            }
        } catch (e: DateTimeParseException) {
            false to "Invalid text or date format. Use YYYY-mm-dd (e.g. 1995-11-12)"
        }
    }

    @Suppress("UnrememberedMutableState")
    @Preview
    @Composable
    fun ApodContentPreview() {
        val uiState = ApodViewModelUiState(
            pictureDate = mutableStateOf("2025-06-18"),
            apod = mutableStateOf(
                ApodResponse(
                    date = "2025-06018",
                    explanation = "",
                    hdurl = "",
                    title = "",
                    url = ""
                )
            ),
            getRandomPic = {},
            getPicOnDate = {}
        )

        ApodContent(uiState)
    }

}