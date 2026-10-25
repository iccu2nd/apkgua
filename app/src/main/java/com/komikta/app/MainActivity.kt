package com.komikta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*

private val Ink = Color(0xFF000000)
private val Muted = Color(0xFF9A9A9F)
private val Paper = Color(0xFF000000)
private val Line = Color(0xFF2C2C2E)
private val White = Color(0xFFE8E8EA)
private val Panel = Color(0xFF1C1C1E)
private val Accent = Color(0xFFA0A0A5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KomiktaApp() }
    }
}

@Composable
private fun KomiktaApp() {
    val nav = rememberNavController()
    val current by nav.currentBackStackEntryAsState()
    val route = current?.destination?.route ?: "home"
    val tabs = listOf("home", "explore", "library", "profile")
    val labels = listOf("Home", "Explore", "Library", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.BookmarkBorder, Icons.Default.PersonOutline)

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Accent, onPrimary = Ink, background = Paper, surface = Panel,
            onSurface = White, secondary = Muted
        )
    ) {
        Scaffold(
            containerColor = Paper,
            bottomBar = {
                NavigationBar(containerColor = Color(0xE61C1C1E), tonalElevation = 0.dp) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = route == tab,
                            onClick = {
                                nav.navigate(tab) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icons[index], contentDescription = labels[index]) },
                            label = { Text(labels[index]) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Ink, selectedTextColor = Ink,
                                indicatorColor = Color(0xFF3A3A3C),
                                unselectedIconColor = Muted, unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(padding)) {
                composable("home") { HomeScreen(onExplore = { nav.navigate("explore") }) }
                composable("explore") { ExploreScreen() }
                composable("library") { LibraryScreen() }
                composable("profile") { ProfileScreen() }
            }
        }
    }
}

@Composable
private fun HomeScreen(onExplore: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("KOMIKTA", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = White)
                    Spacer(Modifier.height(4.dp))
                    Text("Baca cerita, temukan dunia.", style = MaterialTheme.typography.bodyMedium, color = Muted)
                }
                IconButton(onClick = onExplore) {
                    Icon(Icons.Default.Search, contentDescription = "Cari komik", tint = Ink)
                }
            }
        }
        item {
            Column(
                Modifier.fillMaxWidth()
                    .background(Panel, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Text("PILIHAN HARI INI", color = Color(0xFF9A9A9F), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text("Mulai petualangan\nbacamu berikutnya.", color = White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text("Jelajahi ribuan chapter dari cerita favoritmu.", color = Color(0xFFB5B5BA), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8E8EA), contentColor = Ink)) {
                    Text("Jelajahi komik")
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
        item { SectionHeader("Lanjutkan membaca", "Riwayat bacaan") }
        item {
            StoryRow("The Beginning After the End", "Chapter terakhir dibaca · Chapter 12", "12", "TBATE")
        }
        item { SectionHeader("Update terbaru", "Lihat semua") }
        items(listOf(
            Story("Solo Leveling", "Chapter 200 · 2 jam lalu", "SL"),
            Story("Omniscient Reader", "Chapter 188 · 5 jam lalu", "OR"),
            Story("The Beginning After the End", "Chapter 12 · kemarin", "TB")
        )) { story -> StoryRow(story.title, story.subtitle, story.badge, story.title.take(2).uppercase()) }
        item { SectionHeader("Temukan cerita", "Eksplorasi") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Action", "Fantasy", "Romance", "Comedy").forEach {
                    Surface(color = Panel, shape = RoundedCornerShape(50), modifier = Modifier.clickable(onClick = onExplore)) {
                        Text(it, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), color = Ink, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

private data class Story(val title: String, val subtitle: String, val badge: String)

@Composable
private fun SectionHeader(title: String, action: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
        Text(action, style = MaterialTheme.typography.labelMedium, color = Muted)
    }
}

@Composable
private fun StoryRow(title: String, subtitle: String, badge: String, initials: String) {
    Row(
        Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(width = 58.dp, height = 76.dp).background(Color(0xFF2C2C2E), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(initials.take(3), color = Ink, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Ink, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(5.dp))
            Text(subtitle, color = Muted, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
    }
}

@Composable
private fun ExploreScreen() {
    var query by remember { mutableStateOf("") }
    val genres = listOf("All", "Action", "Fantasy", "Romance", "Drama")
    var selected by remember { mutableStateOf("All") }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 22.dp)) {
        Text("Explore", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(6.dp))
        Text("Cari cerita yang cocok untukmu.", color = Muted)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cari judul komik…") }, leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true, shape = RoundedCornerShape(16.dp)
        )
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            genres.forEach { genre ->
                FilterChip(selected = selected == genre, onClick = { selected = genre }, label = { Text(genre) })
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("Populer sekarang", fontWeight = FontWeight.Bold, color = Ink, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(listOf(Story("Solo Leveling", "Action · Fantasy", "SL"), Story("Omniscient Reader", "Action · Drama", "OR"), Story("The Beginning After the End", "Fantasy · Adventure", "TB"))) { s ->
                StoryRow(s.title, s.subtitle, s.badge, s.badge)
            }
        }
    }
}

@Composable
private fun LibraryScreen() {
    Column(Modifier.fillMaxSize().padding(22.dp)) {
        Text("Library", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(6.dp))
        Text("Koleksi dan riwayat bacaanmu.", color = Muted)
        Spacer(Modifier.height(22.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = true, onClick = {}, label = { Text("Tersimpan") })
            FilterChip(selected = false, onClick = {}, label = { Text("Riwayat") })
            FilterChip(selected = false, onClick = {}, label = { Text("Download") })
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Muted, modifier = Modifier.size(42.dp))
                Spacer(Modifier.height(12.dp))
                Text("Belum ada komik tersimpan", color = Ink, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(5.dp))
                Text("Komik yang kamu simpan akan muncul di sini.", color = Muted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(22.dp)) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(6.dp))
        Text("Atur pengalaman membaca Komikta.", color = Muted)
        Spacer(Modifier.height(24.dp))
        ProfileItem(Icons.Default.Tune, "Pengaturan reader", "Mode baca, arah, dan preferensi")
        ProfileItem(Icons.Default.Download, "Download", "Kelola chapter offline")
        ProfileItem(Icons.Default.Info, "Tentang Komikta", "Versi aplikasi dan informasi")
    }
}

@Composable
private fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp).background(Panel, RoundedCornerShape(16.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Ink)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Ink, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(3.dp))
            Text(subtitle, color = Muted, style = MaterialTheme.typography.bodySmall)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
    }
}
