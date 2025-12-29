package com.hamet.profile.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.hamet.profile.configuration.AuthenticationRequestInterceptor;
import com.hamet.profile.dto.response.ApiResponse;
import com.hamet.profile.dto.response.FileResponse;

@FeignClient(name = "file-service", url = "${app.service.file-service}",
    configuration = { AuthenticationRequestInterceptor.class }
)
public interface FileClient {
    @PostMapping(value = "/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadMedia(@RequestPart("file") MultipartFile file);
}
