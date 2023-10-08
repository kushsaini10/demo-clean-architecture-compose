package com.example.testapplication

import com.example.testapplication.data.models.dto.ItemDto
import com.example.testapplication.data.models.dto.ItemsDto
import com.example.testapplication.data.models.entities.Failure
import com.example.testapplication.data.models.entities.Success
import com.example.testapplication.usecase.ParseItemsUsecase
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class ParseItemsUsecaseTest {

    @Test
    fun test_parse_items_success() {

        val apiResponse = ParseItemsUsecase().execute(
            Response.success(
                ItemsDto(
                    items = listOf(
                        ItemDto("title", "desc", "", "")
                    )
                )
            )
        )

        assert(apiResponse is Success)
        val successfulResponse = apiResponse as Success
        assert(successfulResponse.data.size == 1)
        assert(successfulResponse.data[0].title == "title")
        assert(successfulResponse.data[0].desc == "desc")
        assert(successfulResponse.data[0].date == "")
        assert(successfulResponse.data[0].image == "")
    }

    @Test
    fun test_parse_items_failure() {

        val apiResponse = ParseItemsUsecase().execute(
            Response.error(400, String().toResponseBody())
        )

        assert(apiResponse is Failure)
    }

    @Test
    fun test_date_parsing() {

        val apiResponse = ParseItemsUsecase().execute(
            Response.success(
                ItemsDto(
                    items = listOf(
                        ItemDto("title", "desc", "2022-05-18T15:15:50Z", ""),
                        ItemDto("title", "desc", "2022-05-18T15:15:50Z", "")
                    )
                )
            )
        )

        assert(apiResponse is Success)
        val successfulResponse = apiResponse as Success
        assert(successfulResponse.data.size == 2)
        assert(successfulResponse.data[0].date == "May 18, 2022")
    }

    @Test
    fun test_item_dto_validation() {

        val itemDto = ItemDto("title", "desc", "2022-05-18T15:15:50Z", null)
        assert(itemDto.isValid())

        val itemDtoNoTitle = ItemDto("", "desc", "2022-05-18T15:15:50Z", "")
        assert(!itemDtoNoTitle.isValid())

        val itemDtoNoData = ItemDto("", "", "", null)
        assert(!itemDtoNoData.isValid())
    }
}