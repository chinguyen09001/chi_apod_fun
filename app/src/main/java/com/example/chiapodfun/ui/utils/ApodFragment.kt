package com.example.chiapodfun.ui.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.example.chiapodfun.di.DaggerAppComponent
import com.example.chiapodfun.repo.ApodRepository
import com.example.chiapodfun.ui.ApodViewModel
import com.example.chiapodfun.ui.ApodViewModelUiState
import javax.inject.Inject

class ApodFragment: Fragment() {

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
        val apod by remember { uiState.apod }
        val pictureDate by remember { uiState.pictureDate }

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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Date: $pictureDate",
                    modifier = Modifier.wrapContentHeight(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                apod?.let {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        model = it.url,
                        contentDescription = it.title
                    )
                }

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
                    onClick = { uiState.getNewPic() }
                ) {
                    Text(
                        text = "Get a random pic!",
                        modifier = Modifier.wrapContentHeight(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }

    }
}