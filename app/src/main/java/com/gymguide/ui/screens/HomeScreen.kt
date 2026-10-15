package com.gymguide.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

private val GymGreen = Color(0xFF20C997)
private val Ink = Color(0xFF142338)
private val Muted = Color(0xFF718096)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val quoteState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text("⌂", fontSize = 29.sp, fontWeight = FontWeight.Bold) },
                    label = { Text("Inicio", fontWeight = FontWeight.SemiBold) },
                    colors = homeNavigationColors()
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate("grupos") },
                    icon = { Text("⚒", fontSize = 27.sp) },
                    label = { Text("Ejercicios") },
                    colors = homeNavigationColors()
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate("rutinas") },
                    icon = { Text("▤", fontSize = 27.sp) },
                    label = { Text("Rutinas") },
                    colors = homeNavigationColors()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(Ink),
                    contentAlignment = Alignment.Center
                ) { DumbbellIcon(Modifier.size(26.dp), GymGreen) }
                Spacer(Modifier.width(10.dp))
                Text("Gym", color = Ink, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text("Guide", color = GymGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                BellIcon(Modifier.size(25.dp), Ink)
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Hola, Virginia 👋", color = Ink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Tu entrenamiento empieza aquí.", color = Muted, style = MaterialTheme.typography.bodyLarge)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HomeCard("✚", "Ejercicios", "Explora por músculo", Modifier.weight(1f)) { onNavigate("grupos") }
                HomeCard("▤", "Mis rutinas", "Organiza tu progreso", Modifier.weight(1f)) { onNavigate("rutinas") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HomeCard("◴", "Temporizador", "Controla tus descansos", Modifier.weight(1f)) { onNavigate("temporizador") }
                HomeCard("▥", "IMC", "Conoce tu estado", Modifier.weight(1f)) { onNavigate("imc") }
            }

            QuoteCard(state = quoteState, onRetry = viewModel::cargarFrase)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFE8FBF5))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BulbIcon(Modifier.size(30.dp), GymGreen)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Tip del día", color = Ink, fontWeight = FontWeight.Bold)
                    Text("Mantén una buena técnica. La calidad siempre va por delante del peso.", color = Ink, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun homeNavigationColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.Black,
    selectedTextColor = Color.Black,
    unselectedIconColor = Color(0xFF718096),
    unselectedTextColor = Color(0xFF718096),
    indicatorColor = Color.Transparent
)

@Composable
private fun DumbbellIcon(modifier: Modifier, color: Color) {
    Canvas(modifier) {
        val barWidth = size.width * .48f
        val barLeft = (size.width - barWidth) / 2f
        drawLine(color, Offset(barLeft, size.height / 2f), Offset(barLeft + barWidth, size.height / 2f), size.height * .12f, cap = StrokeCap.Round)
        drawRoundRect(color, Offset(size.width * .08f, size.height * .25f), Size(size.width * .18f, size.height * .5f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
        drawRoundRect(color, Offset(size.width * .74f, size.height * .25f), Size(size.width * .18f, size.height * .5f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
        drawRoundRect(color, Offset(size.width * .28f, size.height * .12f), Size(size.width * .12f, size.height * .76f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
        drawRoundRect(color, Offset(size.width * .60f, size.height * .12f), Size(size.width * .12f, size.height * .76f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
    }
}

@Composable
private fun BellIcon(modifier: Modifier, color: Color) {
    Canvas(modifier) {
        val bell = Path().apply {
            moveTo(size.width * .22f, size.height * .72f)
            quadraticTo(size.width * .32f, size.height * .59f, size.width * .32f, size.height * .39f)
            quadraticTo(size.width * .32f, size.height * .13f, size.width * .50f, size.height * .13f)
            quadraticTo(size.width * .68f, size.height * .13f, size.width * .68f, size.height * .39f)
            quadraticTo(size.width * .68f, size.height * .59f, size.width * .78f, size.height * .72f)
            close()
        }
        drawPath(bell, color, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
        drawLine(color, Offset(size.width * .22f, size.height * .74f), Offset(size.width * .78f, size.height * .74f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawArc(color, 205f, 130f, false, Offset(size.width * .41f, size.height * .73f), Size(size.width * .18f, size.height * .14f), style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
private fun BulbIcon(modifier: Modifier, color: Color) {
    Canvas(modifier) {
        drawCircle(color, radius = size.minDimension * .31f, center = Offset(size.width / 2f, size.height * .40f), style = Stroke(width = 2.dp.toPx()))
        drawLine(color, Offset(size.width * .37f, size.height * .63f), Offset(size.width * .40f, size.height * .77f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * .63f, size.height * .63f), Offset(size.width * .60f, size.height * .77f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * .40f, size.height * .77f), Offset(size.width * .60f, size.height * .77f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * .43f, size.height * .86f), Offset(size.width * .57f, size.height * .86f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width / 2f, size.height * .01f), Offset(size.width / 2f, size.height * .09f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * .08f, size.height * .24f), Offset(size.width * .17f, size.height * .29f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * .83f, size.height * .29f), Offset(size.width * .92f, size.height * .24f), 2.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun HomeCard(icon: String, title: String, description: String, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(112.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, color = GymGreen, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(title, color = Ink, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Text(description, color = Muted, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun QuoteCard(state: HomeUiState, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 132.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF173653), Color(0xFF101923))))
            .padding(18.dp)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val mountain = Path().apply {
                moveTo(size.width * .42f, size.height)
                lineTo(size.width * .70f, size.height * .18f)
                lineTo(size.width * .80f, size.height * .49f)
                lineTo(size.width * .89f, size.height * .33f)
                lineTo(size.width, size.height * .70f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(mountain, Color(0x553BA18A))
            drawRect(Brush.horizontalGradient(listOf(Color.Transparent, Color(0x66101923))), size = Size(size.width, size.height))
            drawCircle(Color(0x55FFFFFF), radius = 2.dp.toPx(), center = Offset(size.width * .86f, size.height * .18f))
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            when {
                state.loading -> {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = GymGreen, strokeWidth = 2.dp)
                        Text("Buscando inspiración…", color = Color.White)
                    }
                }
                state.error != null -> {
                    Text("No pudimos cargar la frase", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(state.error, color = Color(0xFFD6E2ED), style = MaterialTheme.typography.bodySmall)
                    TextButton(onClick = onRetry, contentPadding = PaddingValues(0.dp)) {
                        Text("Reintentar", color = Color(0xFF7DE8C2), fontWeight = FontWeight.Bold)
                    }
                }
                else -> {
                    Text("“${state.quote.orEmpty()}”", color = Color.White, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                    Text("— ${state.author ?: "Frase motivacional"}", color = Color(0xFFD6E2ED), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
