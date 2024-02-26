package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import place.skillexchange.backend.file.FileStore;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/file/")
public class FileController {

    private final FileStore fileStore;

    /**
     * 이미지 보기
     */
    @ResponseBody
    @GetMapping("/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        //"file:C:/SpringStudy/file/8f9764ed-7bfb-40a2-80a4-b1c3162ba5ef.jpg"
        return new UrlResource("file:" + fileStore.getFullPath(filename)); //경로에 있는 파일에 접근하여 Stream으로 반환
    }
}
