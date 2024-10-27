package presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun ActionRow(
    textFieldHint: String,
    resultText: String,
    buttonText: String,
    onClickActionButton: (editTextValue: String) -> Unit,
) {
    var editTextValue by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TextField(
            value = editTextValue,
            onValueChange = { text ->
                editTextValue = text
            },
            placeholder = {
                Text(text = textFieldHint)
            },
        )
        Button(onClick = {
            onClickActionButton(editTextValue)
            editTextValue = ""
        }) {
            Text(text = buttonText)
        }
        Text(
            modifier = Modifier.clickable(onClick = resultText::copy),
            color = Color.Blue,
            text = resultText,
        )
    }
}