package com.sixjeon.storey.global.s3;

// 테스트용 controller로서 추후 삭제

import com.sixjeon.storey.global.response.SuccessResponse;
import com.sixjeon.storey.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<SuccessResponse<?>> uploadImage(
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        String imageUrl = s3Service.uploadImage(file);
        return ResponseEntity.ok(SuccessResponse.ok(imageUrl));
    }
}
