import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import domain.impl.RepositoryImpl
import domain.model.Link
import domain.model.auth.Auth
import domain.model.auth.NoAuth
import domain.repository.Repository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    val repository: Repository = RepositoryImpl()
    val coroutineScope = rememberCoroutineScope()

    var currentUuid by remember { mutableStateOf<String?>(null) }
    var myLinks by remember { mutableStateOf<List<Link>?>(null) }
    var convertLinkResult by remember { mutableStateOf("") }
    var useShortLinkResult by remember { mutableStateOf("") }

    var toast by remember { mutableStateOf<String?>(null) }


    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (toast != null) {
                ToastNotification(
                    message = toast ?: "",
                    onDismiss = { toast = null },
                )
            }
            if (currentUuid == null) {
                ActionRow(
                    textFieldHint = "Введите UUID",
                    resultText = "",
                    buttonText = "Авторизоваться",
                    onClickActionButton = { editTextValue ->
                        repository.auth(editTextValue)
                    },
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    currentUuid?.let { uuid ->
                        Button(onClick = repository::logout) {
                            Text(text = "Выйти")
                        }
                        Text(
                            modifier = Modifier.clickable(onClick = uuid::copy),
                            color = Color.Blue,
                            text = uuid
                        )
                    }
                }
            }
            ActionRow(
                textFieldHint = "Введите полную ссылку",
                resultText = convertLinkResult,
                buttonText = "Создать короткую ссылку",
                onClickActionButton = { editTextValue ->
                    convertLinkResult = repository.convertLink(editTextValue)
                },
            )
            currentUuid?.let {
                ActionRow(
                    textFieldHint = "Введите короткую ссылку",
                    resultText = useShortLinkResult,
                    buttonText = "Перейти",
                    onClickActionButton = { editTextValue ->
                        useShortLinkResult = ""
                        val link = repository.decodeLink(editTextValue)
                        if (link != null) {
                            try {
                                link.browse()
                            } catch (e: Exception) {
                                useShortLinkResult = "Произошла ошибка"
                            }
                        } else {
                            useShortLinkResult = "Ссылка не найдена"
                        }
                    },
                )
            }
            myLinks?.let { links ->
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    links.forEach { link ->
                        LinkItem(
                            link = link,
                            onClickDelete = { repository.deleteLink(link.shortLink) },
                            onClickPlus = {
                                repository.updateLinkLimitCount(link, link.currentLimitCount + 1)
                            },
                            onClickMinus = {
                                repository.updateLinkLimitCount(link, link.currentLimitCount - 1)
                            },
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            repository.authState.collectLatest { authState ->
                currentUuid = when (authState) {
                    is Auth -> authState.user.uuid
                    is NoAuth -> null
                    else -> null
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            repository.getAllLinks().collectLatest { links ->
                myLinks = links
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            repository.messages.collectLatest { message ->
                toast = message
            }
        }
    }
}

@Composable
private fun LinkItem(
    link: Link,
    onClickDelete: () -> Unit,
    onClickPlus: () -> Unit,
    onClickMinus: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.clickable(onClick = link.shortLink::copy),
            color = Color.Blue,
            text = link.shortLink,
        )
        IconButton(onClick = onClickMinus) {
            Icon(
                painter = painterResource("drawable/minus.svg"),
                contentDescription = null,
            )
        }
        Text(text = "Limit: ${link.currentLimitCount}")
        IconButton(onClick = onClickPlus) {
            Icon(
                painter = painterResource("drawable/plus.svg"),
                contentDescription = null,
            )
        }
        Button(onClick = onClickDelete) {
            Text(text = "Удалить")
        }
    }
}

@Composable
private fun ToastNotification(
    message: String,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(message)
        Button(onClick = onDismiss) {
            Text(text = "закрыть")
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
