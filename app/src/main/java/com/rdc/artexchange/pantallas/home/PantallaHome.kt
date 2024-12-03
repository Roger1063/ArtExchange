package com.rdc.artexchange.pantallas.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rdc.artexchange.R
import com.rdc.artexchange.pantallas.BottomBar

@Composable
fun PantallaHome(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF1F5F9)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.logoartexchange),
                contentDescription = "Logo Art Exchange",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )

            Text(
                text = "Bienvenido a Art Exchange",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A535C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFD7EDEB), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Quienes somos?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A535C),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Art Exchange es una empresa que intenta dar visibilidad a los artistas pequeños que quieran poner en venta su arte.\n\n" +
                                "Por medio de esta plataforma de compra-venta intentamos que los artistas que quieran seguir financiando sus proyectos puedan vender fácilmente sus obras y sacarles beneficio.\n\n" +
                                "Además esta app también sirve para aquellas personas que busquen obras de arte a buen precio y además quieran apoyar a los artistas.",
                        fontSize = 16.sp,
                        color = Color(0xFF1A535C),
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

