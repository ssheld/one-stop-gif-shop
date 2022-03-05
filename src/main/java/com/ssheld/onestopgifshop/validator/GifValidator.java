package com.ssheld.onestopgifshop.validator;

import com.ssheld.onestopgifshop.model.Gif;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Author: Stephen Sheldon
 **/

@Component
public class GifValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Gif.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Gif gif = (Gif)target;

        // Validate file only if the Gif's id is null (with a null id, the Gif must be a new Gif),
        // so that existing Gif can be updated without uploading a new file
        if (gif.getId() == null && (gif.getFile() == null || gif.getFile().isEmpty())) {
            errors.rejectValue("file", "file.required", "Please choose a file to upload");
        }

        String contentType = gif.getFile().getContentType();

        // Make sure file uploaded is a gif
        if (!gif.getFile().isEmpty() && contentType != null && !isSupportedContent(contentType)) {

            String[] fileType = contentType.split("/");
            StringBuilder fileExtension = new StringBuilder();
            fileExtension.append(".");
            fileExtension.append(fileType[1]);

            errors.rejectValue("file", "Incorrect file format", fileExtension.toString() + " is not supported file type. Please upload a .gif file.");
        }

        // TODO File size validation is neeeded - should probably occur on front-end

        // Validate description
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"description","description.empty","Please enter a description");

        // Validate category
        ValidationUtils.rejectIfEmpty(errors,"category","category.empty","Please choose a category");
    }

    private boolean isSupportedContent(String contentType) {
        return contentType.equals("image/gif");
    }
}
