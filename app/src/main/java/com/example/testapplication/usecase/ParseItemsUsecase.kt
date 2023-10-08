package com.example.testapplication.usecase

import com.example.testapplication.data.models.dto.ItemDto
import com.example.testapplication.data.models.dto.ItemsDto
import com.example.testapplication.data.models.entities.ApiResponse
import com.example.testapplication.data.models.entities.Failure
import com.example.testapplication.data.models.entities.Item
import com.example.testapplication.data.models.entities.NoDataException
import com.example.testapplication.data.models.entities.Success
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import retrofit2.Response
import java.util.Locale

/**
 * Usecase for business logic of parsing api response of items
 */
class ParseItemsUsecase {

    fun execute(response: Response<ItemsDto>): ApiResponse<List<Item>> {
        if (response.isSuccessful) {
            return Success(parseItems(response))
        }
        return Failure(false, NoDataException)
    }

    private fun parseItems(response: Response<ItemsDto>): List<Item> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")

        return response.body()?.let { itemsDto ->
            return itemsDto.items.mapNotNull { itemDto ->
                newItemOrNull(itemDto, formatter)
            }
        } ?: kotlin.run {
            return emptyList()
        }
    }

    private fun newItemOrNull(
        itemDto: ItemDto,
        formatter: DateTimeFormatter?
    ) = if (itemDto.isValid()) {
        val formattedDate = getFormattedDate(itemDto, formatter)
        Item(
            title = itemDto.title!!,
            desc = itemDto.desc ?: String(),
            date = formattedDate ?: String(),
            image = itemDto.image
        )
    } else {
        null
    }

    private fun getFormattedDate(
        itemDto: ItemDto,
        formatter: DateTimeFormatter?
    ): String? {
        return if (itemDto.date != null) {
            parseDate(itemDto.date, formatter)
        } else {
            String()
        }
    }

    private fun parseDate(
        date: String,
        formatter: DateTimeFormatter?
    ): String? {
        try {
            val dateTime = LocalDateTime.parse(date, formatter)
            val outputFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
            return dateTime.format(outputFormatter)
        } catch (error: Exception) {
            error.printStackTrace()
        }
        return null
    }
}
