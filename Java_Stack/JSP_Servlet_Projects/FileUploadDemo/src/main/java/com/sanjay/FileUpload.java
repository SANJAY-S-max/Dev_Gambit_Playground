package com.sanjay;

import java.io.IOException;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/FileUpload")
@MultipartConfig
public class FileUpload extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("file");

        String fileName = Paths.get(
                filePart.getSubmittedFileName()
        ).getFileName().toString();

        String uploadPath =
                "C:\\Users\\krnl\\Documents\\eclipe-workspace\\FileUploadDemo\\";

        filePart.write(uploadPath + fileName);

        response.getWriter().println("File uploaded successfully");
    }
}
