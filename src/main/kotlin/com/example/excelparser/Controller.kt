package com.example.excelparser

import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

@Controller
class Controller(
    private val service: Service,
) {

    @PostMapping("/upload")
    fun upload(@RequestParam file: MultipartFile, model: Model): String {
        val res = service.excelToModel(
            file,
            BasicSheetHeader::class.java.enumConstants
        )
        model.addAttribute("data", res)

        return "index"
    }

    @GetMapping("/download")
    @ResponseBody
    fun download(): ResponseEntity<Resource> {
        val resource = service.modelToExcel()

        return ResponseEntity.ok()
            .header(
                "Content-Disposition",
                "attachment;filename=example.xlsx"
            )
            .contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}
