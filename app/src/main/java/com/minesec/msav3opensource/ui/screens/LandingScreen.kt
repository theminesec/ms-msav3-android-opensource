package com.minesec.msav3opensource.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.landing.presentation.LandingAction
import com.theminesec.multiplatform.msa_core.feature.landing.presentation.LandingState


import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.template.AuthProgressBar


@Composable
fun LandingScreenUI(
    state: LandingState, triggerAction: (LandingAction) -> Unit
) = MsaUiStateHandler(false,state.errorState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.first_screen_bg),
            contentDescription = "first screen background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.CenterStart
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = MsaTheme.spacing.lg),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(MsaTheme.spacing.lg))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.minesec_logo_light),
                    contentDescription = "MineSec Logo",
                    modifier = Modifier.height(MsaTheme.iconSize.xl2)
                )

            }
            Spacer(Modifier.weight(0.3f))
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f), // Pushes buttons to bottom
                //verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.redefining_payments),
                    color = Color(0xFFFFFFFF),
                    style = MsaTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        lineHeight = 64.sp
                    ),
                    modifier = Modifier.padding(bottom = MsaTheme.spacing.md)
                )
                Text(
                    text = stringResource(R.string.seamlessly_connecting_you),
                    color = Color(0xFFFFFFFF),
                    style = MsaTheme.typography.titleMedium,
                )
            }
            Spacer(Modifier.weight(0.7f))


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md) // Spacing between buttons
            ) {
                AuthProgressBar(state.progress)
                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.copyright_footer),
                    color = Color(0xFFFFFFFF),
                    style = MsaTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(MsaTheme.spacing.md))
            }
        }
    }
}

@Preview()
@Composable
fun PreviewFirstScreen() {
    LandingScreenUI(LandingState()) {}
}