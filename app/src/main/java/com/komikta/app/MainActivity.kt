package com.komikta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*

private val Ink = Color(0xFF171717)
private val Paper = Color(0xFFF7F7F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KomiktaApp() }
    }
}

@Composable
private fun KomiktaApp() {
    val nav = rememberNavController()
    val tabs = listOf("home", "explore", "library", "profile")
    val titles = listOf("Home", "Explore", "Library", "Profile")
    MaterialTheme(colorScheme = lightColorScheme(primary = Ink, background = Paper, surface = Paper)) {
        Scaffold(
            containerColor = Paper,
            bottomBar = {
                NavigationBar(containerColor = Paper) {
                    tabs.forEachIndexed { i, route ->
                        NavigationBarItem(
                            selected = nav.currentDestination?.route == route,
                            onClick = { nav.navigate(route) { launchSingleTop = true; popUpTo("home") { saveState = true }; restoreState = true } },
                            icon = { Icon(when (i) { 0 -> Icons.Default.Home; 1 -> Icons.Default.Explore; 2 -> Icons.Default.Bookmark; else -> Icons.Default.Person }, titles[i]) },
                            label = { Text(titles[i]) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(padding)) {
                composable("home") { NativePage("Komikta", "Temukan cerita untuk dibaca.", listOf("Update terbaru", "Rekomendasi")) }
                composable("explore") { NativePage("Explore", "Cari komik berdasarkan judul, genre, atau format.", listOf("Manhwa", "Manhua", "Manga")) }
                composable("library") { NativePage("Library", "Riwayat dan koleksi bacaan tersimpan.", listOf("Belum ada komik tersimpan")) }
                composable("profile") { NativePage("Profile", "Pengaturan aplikasi dan preferensi membaca.", listOf("Pengaturan reader", "Tentang Komikta")) }
            }
        }
    }
}

@Composable
private fun NativePage(title: String, subtitle: String, rows: List<String>) {
    Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 18.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(24.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(rows) { row ->
                Surface(color = Color.White, shape = MaterialTheme.shapes.large) {
                    Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(row, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }
    }
}
