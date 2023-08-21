package com.example.excelparser

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream

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

    fun modelToExcel(): Resource {
        val all = repository.findAll()

        val wb = HSSFWorkbook()
        val sheet = wb.createSheet()
        val header = sheet.createRow(0)

        header.createCell(0).setCellValue("번호")
        header.createCell(1).setCellValue("이름")
        header.createCell(2).setCellValue("설명")

        all.forEachIndexed { index, model ->
            val row = sheet.createRow(index + 1)

            row.createCell(0).setCellValue(model.id.toString())
            row.createCell(1).setCellValue(model.name)
            row.createCell(2).setCellValue(model.description)
        }

        ByteArrayOutputStream().use { stream ->
            wb.write(stream)
            wb.close()
            return ByteArrayResource(stream.toByteArray())
        }
    }
}