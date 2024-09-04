package musacart.ecommerce.controller;


import musacart.ecommerce.entity.File;
import musacart.ecommerce.helper.FileUploadHelper;
import musacart.ecommerce.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class Product {


    @Autowired
    FileUploadService fileUploadService;

    @GetMapping("/getProduct")
    public String getProduct() {
        return "getProduct";
    }


    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        return fileUploadService.fileUpload(file);


    }


    @GetMapping("/downloadFile")
    public ResponseEntity<Object> downloadFile(@RequestParam(name = "id") Integer id) throws IOException {
        if(id!=null) {
            return fileUploadService.fileDownload(id);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Id is not present");
    }
}
