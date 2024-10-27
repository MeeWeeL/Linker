package domain.impl

import domain.model.Link
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LinkListService {

    private val _links = MutableStateFlow<List<Link>>(listOf())
    val links get() = _links.asStateFlow()

    fun updateLinkList(links: List<Link>) {
        _links.value = links
    }
}