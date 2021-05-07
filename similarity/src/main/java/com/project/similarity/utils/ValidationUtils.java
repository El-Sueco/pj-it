package com.project.similarity.utils;

import com.project.similarity.db.entity.File;
import com.project.similarity.db.entity.Type;
import com.project.similarity.exceptions.TypeMismatchException;
import com.project.similarity.exceptions.ValidationException;

public class ValidationUtils {
    public static void checkRightFileTypes(Type type, File f1, File f2) throws ValidationException {
        if(f1.getType() != type || f2.getType() != type) {
            throw new TypeMismatchException(String.format("Types of File %s and/or File %s are not matching with Type %s",
                            f1.getFileName(), f2.getFileName(), type.getName()));
        }
    }
}
