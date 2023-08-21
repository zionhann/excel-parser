package com.example.excelparser

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class Service(
    private val repository: Repository,
) {

    fun excelToModel(file: MultipartFile, header: Array<BasicSheetHeader>): List<Model> {
        val wb = WorkbookFactory.create(file.inputStream)
        val rows = wb.getSheetAt(0).drop(1).toList()
        val models = rows.filter { row ->
            row
                .getCell(header[0].ordinal)
                .numericCellValue.toLong() != 0L
        }.map { row ->
            val id = row.getCell(header[0].ordinal).numericCellValue.toLong()
            val name = row.getCell(header[1].ordinal).stringCellValue
            val description = row.getCell(header[2].ordinal).stringCellValue

            Model(id, name, description)
        }
        repository.saveAll(models)

        return models
    }
}