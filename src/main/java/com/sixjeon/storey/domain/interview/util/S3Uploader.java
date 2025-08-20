package com.sixjeon.storey.domain.interview.util;

import com.sixjeon.storey.domain.interview.exception.S3UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.time.Duration;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadPngAndGetUrl(byte[] png, String key) {
        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType("image/png")
                    .build();
            s3Client.putObject(putReq, RequestBody.fromBytes(png));
        } catch (Exception e) {
            throw new S3UploadException();
        }

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(getReq)
                .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignReq);
        return presigned.url().toString();
    }
}
