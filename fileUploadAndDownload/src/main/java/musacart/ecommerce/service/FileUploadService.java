package musacart.ecommerce.service;

import com.fasterxml.jackson.databind.util.NativeImageUtil;
import musacart.ecommerce.entity.File;
import musacart.ecommerce.helper.FileUploadHelper;
import musacart.ecommerce.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileUploadService {

    @Autowired
    FileUploadHelper fileUploadHelper;

    @Autowired
    FileRepository fileRepository;
    public ResponseEntity<String> fileUpload(MultipartFile file) {
        try {

            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please upload file");
            }
            if (!file.getContentType().equals("image/jpeg")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please upload image in jpeg format");
            }
            boolean f = fileUploadHelper.uploadFile(file);
            String UPLOAD_DIR = new ClassPathResource("/image").getFile().getAbsolutePath()+"\\"+file.getOriginalFilename();
            File fileData= fileRepository.save(File.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .filePath(UPLOAD_DIR).build());
            file.transferTo(new java.io.File(UPLOAD_DIR).toPath());
            if (fileData!=null) {


                return ResponseEntity.ok("File upload Successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went to wrong");
    }


    public ResponseEntity<Object> fileDownload(Integer id) throws IOException {
          System.out.println("id==="+id);
        Optional<File> fileData= fileRepository.findById(id);

        String path=fileData.get().getFilePath();
        byte[] content= Files.readAllBytes(new java.io.File(path).toPath());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+fileData.get().getName()+"\"").contentType(MediaType.valueOf("image/jpeg")).body(content);
    }
  
}
