package controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/upload", produces = {"application/json"})
@Slf4j
@CrossOrigin("*")
public class UploadFileController {

    private final   String pathArquivos;

    public UploadFileController(@Value("${app.path.arquivos}") String  pathArquivos){
        this.pathArquivos = pathArquivos;
    }


    @PostMapping(value = "/arquivo")
    public ResponseEntity<String> fileSave(@RequestParam ("file") MultipartFile file){

        log.info("recebendo Arquivo:", file.getOriginalFilename());

        var caminho = pathArquivos + UUID.randomUUID() + "." + extractExtesion(file.getOriginalFilename());

        log.info("nome do arquivo:" + caminho);
        try {

            Files.copy(file.getInputStream(), Path.of(caminho), StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity<>("{\"mensagem\": \"salvo com sucesso!\"}", HttpStatus.OK);

        }catch (Exception ex){
            log.error("erro ao processar arquivo", ex);
            return new ResponseEntity<>("{\"mensagem\": \"erro ao processar o arquivo\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }



    }

    private String extractExtesion(String fileName) {

        int i = fileName.lastIndexOf(".");
        return fileName.substring(i + 1);
    }
}
