package com.example.excelparser;

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class Controller(
    private val service: Service,
) {

    @PostMapping("/results")
    fun upload(@RequestParam file: MultipartFile, model: Model): String {
        val res = service.excelToModel(
            file,
            BasicSheetHeader::class.java.enumConstants
        )
        model.addAttribute("data", res)

        return "index"
    }
}
