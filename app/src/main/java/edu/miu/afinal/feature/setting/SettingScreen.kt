package edu.miu.afinal.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.miu.afinal.data.model.Setting

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    Column {
        Setting.entries.toList().forEach{
            Text(text = it.name)
        }
    }
}
