package com.sixjeon.storey.global.external.s3.service;

import com.sixjeon.storey.global.external.s3.exception.FileEmptyException;
import com.sixjeon.storey.global.external.s3.exception.FileSizeExceededException;
import com.sixjeon.storey.global.external.s3.exception.InvalidFileExtensionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    // S3 클라이언트, S3와 통신하기 위한 객체
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    // 허용할 파일 확장자 목록(수정 예정) -> 보안
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "png", "jpeg");

    // 최대 파일 업로드 크기 제한 (현재 10MB, 수정 예정)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // 이미지 파일을 S3에 업로드하고, 해당 파일 URL 반환
    public String uploadImage(MultipartFile file) throws IOException {
        validateFile(file);

        String fileName = generateFileName(file);

        try{
            // S3에 파일 업로드를 위한 요청 객체 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket) // 파일이 저장될 버킷 이름
                    .key(fileName) // 파일이 저장될 파일 이름
                    .contentType(file.getContentType())
                    .contentDisposition("inline")
                    .build();

            // 파일 업로드
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // 업로드된 파일의 전체 URL 생성
            return getFileUrl(fileName);
        } catch (S3Exception e){
            throw new IOException("파일 업로드에 실패했습니다.", e);
        }
    }

    // S3에 저장된 파일의 전체 URL을 생성하여 반환
    private String getFileUrl(String fileName) {
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(fileName))
                .toExternalForm();
    }

    // 업로드된 파일이 유효한지 검사
    private void validateFile(MultipartFile file) {
        // 파일 존재 여부 확인
        if (file == null || file.isEmpty()) {
            throw new FileEmptyException();
        }
        // 파일 크기 확인
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException();
        }
        // 파일 확장자 확인
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            throw new InvalidFileExtensionException();
        }
    }

    // 주어직 파일 이름의 확장자가 허용된 목룍에 포함됐는지 확인
    private boolean hasValidExtension(String fileName) {
        // 대소문자 없이 비교
        String lowercaseFileName = fileName.toLowerCase();
        // 허용된 확장자 목록 중 하나로 끝나는지 확인
        return ALLOWED_EXTENSIONS.stream()
                .anyMatch(lowercaseFileName::endsWith);
    }

    // S3에 저장할 고유한 파일명을 생성
    private String generateFileName(MultipartFile file) {
        // 원본 파일 이름에서 확장자 추출
        String originalFilename = file.getOriginalFilename();
        // 확장자 제거
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 파일 이름 중복 피하기 UUID(범용 고유 식별자) 생성
        String uniqueFileName = UUID.randomUUID().toString();

        return "storey/" + datePath + "/" + uniqueFileName + extension;
    }

}
