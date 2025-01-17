package com.lobox.technical_assignment.exceptions

class ImportException(field: String) :
    Exception("an exception occurred in importing the csv file because there is null value for non nullable field $field}")