package com.lobox.technical_assignment.exceptions

class ImportCsvException(field: String, line: Array<String>) :
    Exception(
        "an exception occurred in importing the csv file because there is null value for non nullable field $field - line: ${
            line.joinToString(
                ","
            )
        }"
    )
class ImportException(field: String, line: List<String>) :
    Exception(
        "an exception occurred in importing the csv file because there is null value for non nullable field $field - line: ${
            line.joinToString(
                ","
            )
        }"
    )